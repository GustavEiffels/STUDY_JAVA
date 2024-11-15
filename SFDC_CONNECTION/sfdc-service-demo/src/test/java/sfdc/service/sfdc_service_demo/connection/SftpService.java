package sfdc.service.sfdc_service_demo.connection;

import com.jcraft.jsch.*;
import org.springframework.web.multipart.MultipartFile;
import sfdc.service.sfdc_service_demo.connection.test.SftpConnectionTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import static org.springframework.util.StringUtils.hasText;

public class SftpService {

    private final ThreadLocal<Session> sessionHolder = new ThreadLocal<>();
    private final ThreadLocal<ChannelSftp> channelHolder = new ThreadLocal<>();



    public ChannelSftp createChannel(SftpCredentials credentials) {
        Session sftpSession = sessionHolder.get();
        ChannelSftp channel = channelHolder.get();

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
            } else {
                throw new CustomSftpException(SFTP_STATUS.DISCONNECT);
            }
        } catch (JSchException jSchException) {
            String errorMessage = jSchException.getMessage();
            if (errorMessage.contains("Auth fail")) {
                System.out.println("Authentication failed: Check username/password.");
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
        } catch (Exception e) {
            throw new CustomSftpException(SFTP_STATUS.DISCONNECT,e.getMessage());
        }
    }

    private Session createSession(SftpCredentials credentials) throws JSchException {
        JSch jSchObj = new JSch();

        // ** Session 초기 생성
        Session sftpSession = jSchObj.getSession(credentials.getUserName(), credentials.getHost());

        if (credentials.getPort() > 0) sftpSession.setPort(credentials.getPort());             // ** Port 가 존재하면 port 설정
        if (hasText(credentials.getPassword()))
            sftpSession.setPassword(credentials.getPassword());     // ** Password 존재하면 password 설정

        // ** Session Properties 설정
        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");

        sftpSession.setConfig(properties);
        sftpSession.connect(15000);

        return sftpSession;
    }

    public String upload(SftpCredentials credentials, String path, MultipartFile file) {

        if(file == null)                                                    throw new CustomSftpException(SFTP_STATUS.NOT_FOUND_FILE);
        if(!isValidType(Objects.requireNonNull(file.getContentType())))     throw new CustomSftpException(SFTP_STATUS.ONLY_IMAGE);


        ChannelSftp channel = createChannel(credentials);
        if( !isValidPath(channel,path) ) throw new CustomSftpException(SFTP_STATUS.NOT_VALID_PATH);

        // ** upload
        try(FileInputStream fis = new FileInputStream(path)){
            channel.put(fis,path);
        }
        catch (IOException ioE){
            throw new CustomSftpException(SFTP_STATUS.IOE,ioE.getLocalizedMessage());
        }
        catch (SftpException sftpE){
            throw new CustomSftpException(SFTP_STATUS.SFTP_ERROR,sftpE.getLocalizedMessage());
        }

        return "test";
    }

    private boolean isValidType(String contentType) {
        return contentType.equals("image/jpeg") || contentType.equals("image/png");
    }

    private boolean isValidPath(ChannelSftp channel, String path){
        try{
            channel.ls(path);
            return true;
        }
        catch (SftpException e){
            return false;
        }
    }
}
