package sfdc.service.sfdc_service_demo.connection.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sfdc.service.sfdc_service_demo.connection.CustomSftpException;
import sfdc.service.sfdc_service_demo.connection.SFTP_STATUS;
import sfdc.service.sfdc_service_demo.connection.SftpCredentials;
import sfdc.service.sfdc_service_demo.connection.SftpService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SftpUploadTest {



    /**
     * Upload Test
     *
     * 조건들 부터 적기
     * 1. upload 할 파일이 존재하지 않으면 에러   o
     * 2. 경로가 존재하지 않으면 => 예외 발생      o
     * 3. 업로드 성공 시, 경로를 반환           O
     * 4. 파일이름 UUID 로 다시 생성하기         O
     * 5. 연결 이후 disconnection 하기
     * 6. upload 하는 파일은 이미지 형태만 가능 하게       o
     * 7. upload parameter 는 multipartfile 일것     o
     */


    // ** level 1 -> upload 성공 시 경로를 반환함
    /**
     *     public String upload() {
     *         return "test";
     *     }
     */
    @Test
    void upload_success(){

    }

    // ** level 2 -> 경로 발견 못하면 에러 발생
    /**
     *     public String upload(SftpCredentials credentials,String path) {
     *         ChannelSftp channel = createChannel(credentials);
     *
     *         try {
     *             channel.ls(path);
     *         }
     *         catch (SftpException e){
     *             throw new CustomSftpException(SFTP_STATUS.NOT_VALID_PATH,e.getLocalizedMessage());
     *         }
     *
     *         return "test";
     *     }
     */
    @Test
    void upload_pathNotExist_Exception(){
        MultipartFile       file            = createTestMultiPartFile("image/jpeg","test.jpg");
        SftpService service                 = new SftpService();

        final SftpCredentials credentials    = new SftpCredentials("localhost",2222,"xiuk","ppppp");

        CustomSftpException exception =  assertThrows(CustomSftpException.class,()->{
            String filePath = service.upload(credentials,"/test/path",file);
            assertNotNull(filePath);
        });

        Assertions.assertEquals(SFTP_STATUS.NOT_VALID_PATH,exception.getStatus());
    }

    // ** level 3 -> 파일이 존재하지 않으면 에러가 발생

    /**
     *     public String upload(SftpCredentials credentials, String path, MultipartFile file) {
     *
     *         if(file == null) throw new CustomSftpException(SFTP_STATUS.NOT_FOUND_FILE);
     *
     *         ChannelSftp channel = createChannel(credentials);
     *
     *         if( !isValidPath(channel,path) ) throw new CustomSftpException(SFTP_STATUS.NOT_VALID_PATH);
     *         return "test";
     *     }
     */
    @Test
    void upload_fileIsNotNull_Exception(){
        String fileName = "testfile.txt";
        byte[] content = "This is a test file".getBytes();
        MockMultipartFile multipartFile     = new MockMultipartFile("file", fileName, "text/plain", content);
        SftpService service                 = new SftpService();

        final SftpCredentials credentials    = new SftpCredentials("localhost",2222,"xiuk","ppppp");

        CustomSftpException exception =  assertThrows(CustomSftpException.class,()->{
            String filePath = service.upload(credentials,"/test/path",null);
            assertNotNull(filePath);
        });

        Assertions.assertEquals(SFTP_STATUS.NOT_AVAILABLE_PATH,exception.getStatus());
    }


    /**
     * TEST CODE REFACTORING
     */
    private MultipartFile createTestMultiPartFile(String contentType,String fileName){
        byte[] content = "This is a test file".getBytes();
        return new MockMultipartFile("file", fileName, contentType, content);
    }

    private SftpCredentials credentials(){
        return new SftpCredentials("localhost",2222,"xiuk","ppppp");
    }


    // ** level 4. 파일의 타입은 이미지만 가능하게
    // image 타입인경우 정상적으로 경로 반환
    // image 타입이 아닌 경우 예외 발생

    /**
     *     public String upload(SftpCredentials credentials, String path, MultipartFile file) {
     *
     *         if(file == null)                            throw new CustomSftpException(SFTP_STATUS.NOT_FOUND_FILE);
     *         if(!isValidType(file.getContentType()))     throw new CustomSftpException(SFTP_STATUS.ONLY_IMAGE);
     *
     *         ChannelSftp channel = createChannel(credentials);
     *
     *         if( !isValidPath(channel,path) ) throw new CustomSftpException(SFTP_STATUS.NOT_VALID_PATH);
     *         return "test";
     *     }
     *
     *     private boolean isValidType(String contentType) {
     *         return contentType.equals("image/jpeg") || contentType.equals("image/png");
     *     }
     */
    @Test
    void upload_onlyImageFile(){
        MultipartFile       file            = createTestMultiPartFile("image/png","test.png");
        SftpCredentials     credentials    = credentials();
        new SftpService().upload(credentials,"/",file);

        MultipartFile       file_Txt         = createTestMultiPartFile("text/plain","test.png");
        CustomSftpException exception       = assertThrows(CustomSftpException.class,()->{
            new SftpService().upload(credentials,"/",file_Txt);
        });

        assertEquals(SFTP_STATUS.ONLY_IMAGE,exception.getStatus());
    }


    // ** level 5. 파일 업로드 시 파일이름 변경하기
    // - 통과 조건 : uuid 로 변경하여 경로 변환시 통과

    /**
     *     public String upload(SftpCredentials credentials, String path, MultipartFile file) {
     *
     *         // ** file 이 존재하는지 확인
     *         if(file == null)                                                    throw new CustomSftpException(SFTP_STATUS.NOT_FOUND_FILE);
     *         // ** file 이 jpeg,jpg,png 인지 확인
     *         if(!isValidType(Objects.requireNonNull(file.getContentType())))     throw new CustomSftpException(SFTP_STATUS.ONLY_IMAGE);
     *
     *
     *         ChannelSftp channel = createChannel(credentials);
     *
     *         if( !isValidPath(channel,path.isEmpty() ? "/":path) ) throw new CustomSftpException(SFTP_STATUS.NOT_VALID_PATH);
     *
     *         // ** convert file name
     *         String currentFileName = file.getOriginalFilename();
     *         if( currentFileName == null || !currentFileName.contains(".") ){
     *             throw new CustomSftpException(SFTP_STATUS.NOT_ALLOW_FILE_TYPE);
     *         }
     *
     *         // ** extract Extension of the file
     *         String extension   = currentFileName.substring(currentFileName.lastIndexOf("."));
     *         String newFileName = UUID.randomUUID()+extension;
     *         String newFilePath = path+"/"+newFileName;
     *
     *         // ** upload
     *         try(InputStream inputStream = file.getInputStream()){
     *             channel.put(inputStream,newFilePath);
     *         }
     *         catch (IOException ioE){
     *             throw new CustomSftpException(SFTP_STATUS.IOE,ioE.getLocalizedMessage());
     *         }
     *         catch (SftpException sftpE){
     *             throw new CustomSftpException(SFTP_STATUS.SFTP_ERROR,sftpE.getLocalizedMessage());
     *         }
     *
     *         return newFilePath;
     *     }
     */
    @Test
    void upload_upload(){
        MultipartFile       file            = createTestMultiPartFile("image/png","test.png");
        SftpCredentials     credentials    = credentials();
        String              filePath        = new SftpService().upload(credentials,"/upload",file);
        try{
            System.out.println("filePath : "+filePath);
            UUID.fromString(filePath.replace("/","").replace(".png",""));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


        MultipartFile       file2            = createTestMultiPartFile("image/png","test");
        SftpCredentials     credentials2    = credentials();

        CustomSftpException exception = assertThrows(CustomSftpException.class,()->{
            new SftpService().upload(credentials2,"",file2);
        });
        assertEquals(exception.getStatus(),SFTP_STATUS.NOT_ALLOW_FILE_TYPE);
    }
}
