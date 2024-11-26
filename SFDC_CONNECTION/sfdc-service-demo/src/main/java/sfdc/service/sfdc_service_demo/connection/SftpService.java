package sfdc.service.sfdc_service_demo.connection;

import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import static org.springframework.util.StringUtils.hasText;

@Service
@Slf4j
@RequiredArgsConstructor
class SftpService {
    private final SftpConnectionProperties properties;
    private final ThreadLocal<Session>      sessionHolder = new ThreadLocal<>();
    private final ThreadLocal<ChannelSftp>  channelHolder = new ThreadLocal<>();

    public ChannelSftp createChannel(SftpCredentials credentials) {
        Session sftpSession = sessionHolder.get();
        ChannelSftp channel = channelHolder.get();

        if(channel == null ||  !channel.isConnected()||  sftpSession == null||!sftpSession.isConnected() ){
            try {
                // ** CREATE SESSION
                sftpSession = createSession(credentials);

                // ** CREATE CHANNEL
                channel = (ChannelSftp) sftpSession.openChannel("sftp");
                channel.connect(15000);

                if (channel.isConnected()) {
                    sessionHolder.set(sftpSession);
                    channelHolder.set(channel);
                    return channel;
                }
                throw new CustomSftpException(SFTP_STATUS.DISCONNECT);
            }
            catch (JSchException jSchException) {
                String errorMessage = jSchException.getMessage();
                if (errorMessage.contains("Auth fail")) {
                    throw new CustomSftpException(SFTP_STATUS.AUTHENTICATION_ERROR);
                } else if (errorMessage.contains("UnknownHostException")) {
                    System.out.println("Unknown host: Check the host address.");
                    throw new CustomSftpException(SFTP_STATUS.UNKNOWN_HOST);
                } else if (errorMessage.contains("Connection refused")) {
                    System.out.println("Connection refused: Check the host/port or firewall settings.");
                    throw new CustomSftpException(SFTP_STATUS.CONNECTION_REFUSED);
                } else {
                    System.out.println("Library error: " + errorMessage);
                    throw new CustomSftpException(SFTP_STATUS.LIBRARY_ERROR);
                }
            }
            catch (Exception e) {
                throw new CustomSftpException(SFTP_STATUS.DISCONNECT,e.getLocalizedMessage());
            }
        }
        return channel;
    }

    // description : create Sftp Session
    private Session createSession(SftpCredentials credentials) throws JSchException {
        JSch jSchObj = new JSch();

        // ** Session 초기 생성
        Session sftpSession = jSchObj.getSession(credentials.getUsername(), credentials.getHost());

        if (credentials.getPort() > 0) sftpSession.setPort(credentials.getPort());       // ** Port 가 존재하면 port 설정
        if (hasText(credentials.getPassword()))
            sftpSession.setPassword(credentials.getPassword());                          // ** Password 존재하면 password 설정

        // ** Session Properties 설정
        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");

        sftpSession.setConfig(properties);
        sftpSession.connect(15000);

        return sftpSession;
    }

    // description: disconnect sftp session, channel
    private void disconnect(){
        Session     session = sessionHolder.get();
        ChannelSftp channel = channelHolder.get();

        if(channel!=null&&channel.isConnected()){
            channel.disconnect();
            channelHolder.remove();
        }

        if(session!=null&&session.isConnected()){
            session.disconnect();
            sessionHolder.remove();
        }
    }


    /**
     *  1. 입력 확인
     *  2. 파일 확인
     *  3. 새로운 경로 생성
     *  4. upload
     *  5. 새로운 경로 반환
     *
     * @param path
     * @param file
     * @return
     */
    public String upload(String path, MultipartFile file) {

        // ** 파일 정보 확인
        SftpFileInfo fileInfo   = extractFileInfo(file);

        // ** SFTP 채널 생성
        ChannelSftp channel     = createChannel( new SftpCredentials(properties));

        // ** 새로운 파일 경로 생성
        String newFilePath      = generateFilePath(channel,path,fileInfo.getExtension());

        // ** upload
        try(InputStream inputStream = file.getInputStream()){
            channel.put(inputStream,newFilePath);
        }
        catch (IOException ioE){
            throw new CustomSftpException(SFTP_STATUS.IOE,ioE.getLocalizedMessage());
        }
        catch (SftpException sftpE){
            throw new CustomSftpException(SFTP_STATUS.SFTP_ERROR,sftpE.getLocalizedMessage());
        }
        finally {
            disconnect();
        }
        return newFilePath;
    }

        /**
         *
         * @param multipartFile
         * @return
         */
        public SftpFileInfo extractFileInfo(MultipartFile multipartFile) {
            assert  multipartFile != null;
            String fileOriginalNm = multipartFile.getOriginalFilename();    // FILE ORIGINAL
            String fileType       = multipartFile.getContentType();         // FILE TYPE
            assert fileOriginalNm != null;
            String fileExtension  = fileOriginalNm
                    .substring(fileOriginalNm.lastIndexOf(".") + 1)
                    .toLowerCase();

            boolean isAlloyFileType = false;

            switch (fileType){
                case "image/jpeg" ->{
                    if( fileExtension.equals("jpg") || fileExtension.equals("jpeg") )
                        isAlloyFileType = true;
                }
                case "image/png" ->{
                    if( fileExtension.equals("png"))
                        isAlloyFileType = true;
                }
            }
            if(!isAlloyFileType)
                throw new CustomSftpException(SFTP_STATUS.NOT_ALLOW_FILE_TYPE);

            return new SftpFileInfo(fileExtension,fileType);
        }


        /**
         *
         * @param channel
         * @param path
         * @param extension
         * @return
         */
        private String generateFilePath(ChannelSftp channel,String path,String extension){
            // ** Path 수정
            path.trim()
                    .replaceAll("/{2,}", "/")
                    .replaceAll("/$", "");
            try {
                channel.ls(path);
                return path+"/"+UUID.randomUUID()+"."+extension;
            }
            catch (SftpException e){
                throw new CustomSftpException(SFTP_STATUS.NOT_AVAILABLE_PATH);
            }
        }

        /**
         *
         * @param channel
         * @param path
         * @return
         */
        private boolean isValidPath(ChannelSftp channel, String path){
            try{
                channel.ls(path);
                return true;
            }
            catch (SftpException e){
                System.out.println("SFTP Exception : "+e.getLocalizedMessage());
                return false;
            }
        }

    // description: disconnect sftp session, channel
    public InputStream download(String source) {
        // ** Conn ect with Sftp Server
        ChannelSftp channelSftp =  createChannel( new SftpCredentials(properties) );

        // ** check file exist
        if(isValidPath(channelSftp,source)){
            try{
                return channelSftp.get(source);
            }
            catch (SftpException sftpException){
                throw new CustomSftpException(SFTP_STATUS.SFTP_ERROR,sftpException.getLocalizedMessage());
            }
        }
        else{
            throw new CustomSftpException(SFTP_STATUS.NOT_AVAILABLE_PATH);
        }
    }

}
