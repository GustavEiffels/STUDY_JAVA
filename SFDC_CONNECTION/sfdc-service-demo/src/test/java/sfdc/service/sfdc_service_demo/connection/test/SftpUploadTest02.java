package sfdc.service.sfdc_service_demo.connection.test;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sfdc.service.sfdc_service_demo.connection.CustomSftpException;
import sfdc.service.sfdc_service_demo.connection.SFTP_STATUS;
import sfdc.service.sfdc_service_demo.connection.SftpService;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class SftpUploadTest02 {

    private SftpService service = new SftpService();



    /**
     *  1. 입력 확인 => file 이름 반환, extension 반환
     *  2. 파일 확인
     *  3. 새로운 경로 생성
     *  4. upload
     *  5. 새로운 경로 반환
     */
    @Test
    void fileInfoExtractor(){
        SftpFileInfo fileInfo = service.extractFileInfo(generateMultiPartFile(FILE_TYPE.JPEG,"test.jpeg","파일Refactoring"));
        assertEquals(fileInfo.getExtension(),"jpeg");
        assertEquals(fileInfo.getContentType(),"image/jpeg");
    }

    @Test
    void whenFileNotMatchingException(){
        CustomSftpException exception = assertThrows(CustomSftpException.class,()->{
            service.extractFileInfo(generateMultiPartFile(FILE_TYPE.PNG,"test.jpeg","파일Refactoring"));
        });
        assertEquals(exception.getStatus(), SFTP_STATUS.NOT_ALLOW_FILE_TYPE);
    }

    @Test
    void mainTest(){
        System.out.println(
                generateMultiPartFile(FILE_TYPE.JPEG,"test","파일Refactoring").getOriginalFilename()        );
    }





    /**
     * MultiPartFile Generator => 파일 생성기
     * @param type
     * @param fileName
     * @param fileContents
     * @return
     */
    private MultipartFile generateMultiPartFile(FILE_TYPE type, String fileName, String fileContents){
        byte[] fileContent = fileContents.getBytes();

        switch (type){
            case JPG, JPEG ->{
                return new MockMultipartFile("file",fileName,"image/jpeg",fileContent);
            }
            case PNG ->{
                return new MockMultipartFile("file",fileName,"image/png",fileContent);
            }
            case TEXT ->{
                return new MockMultipartFile("file",fileName,"text/plain",fileContent);
            }
            default -> throw new NotTestTypeException();
        }
    }

    private enum FILE_TYPE {
        JPG,JPEG,PNG,TEXT
    }

    private class NotTestTypeException extends RuntimeException {
    }
}
