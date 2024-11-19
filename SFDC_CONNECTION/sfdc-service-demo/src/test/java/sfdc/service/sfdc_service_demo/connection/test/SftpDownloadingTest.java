package sfdc.service.sfdc_service_demo.connection.test;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sfdc.service.sfdc_service_demo.connection.CustomSftpException;
import sfdc.service.sfdc_service_demo.connection.SFTP_STATUS;
import sfdc.service.sfdc_service_demo.connection.SftpCredentials;
import sfdc.service.sfdc_service_demo.connection.SftpService;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class SftpDownloadingTest {

    /**
     * @Sftp 파일 다운로드 테스트
     * 1. 파일 성공
     *  경로에 파일이 존재하는지
     *  파일 내려 받기 까지 완료
     * 2. 파일 실패 -> 예외의 경우
     *  경로가 존재하지 않는 경우 ( 파일이 존재하지 않는 경우 )
     *  SFTP 연결이 끊기는 경우
     *  I/O 가 끊기는 경우
     *  네트워크가 불안정한 경우 ?
     *
     *  Return type 은 InputStream
     *
     *  -> TEST 진행방식
     *   Upload 를 사용해서 파일 존재하는지
     */

    private String getUploadedFilePath(String path,String contents){
        byte[] content = contents.getBytes();

        MultipartFile   file        =
                new MockMultipartFile("file", "test.png", "image/png", content);

        SftpCredentials credentials =
                new SftpCredentials("localhost",2222,"xiuk","ppppp");

        return new SftpService().upload(credentials,path,file);
    }

    @Test
    void file_download_success() throws IOException {
        // ** Announce Contents
        String fileContentsString = "This is DownloadTest File";

        // ** Upload and Get new File Path
        String filePath = getUploadedFilePath("/upload",fileContentsString);

        // ** Call Out Service
        SftpService sftpService = new SftpService();

        // **
        SftpCredentials credentials = new SftpCredentials("localhost",2222,"xiuk","ppppp");

        InputStream fileInputStream = sftpService.download(credentials,filePath);

        // ** Convert InputStream to String
        String downloadedContents = new String(fileInputStream.readAllBytes());

        // ** Assert That Contents Match
        assertEquals(fileContentsString, downloadedContents, "Downloaded contents do not match original file contents.");
    }

    @Test
    void file_download_notExistFileOrPath(){
        CustomSftpException exception =  assertThrows(CustomSftpException.class,()->{
            // ** Call Out Service
            SftpService sftpService = new SftpService();
            SftpCredentials credentials = new SftpCredentials("localhost",2222,"xiuk","ppppp");
            InputStream fileInputStream = sftpService.download(credentials,"/test");
        });

        assertEquals(exception.getStatus(), SFTP_STATUS.NOT_FOUND_FILE);
    }
}
