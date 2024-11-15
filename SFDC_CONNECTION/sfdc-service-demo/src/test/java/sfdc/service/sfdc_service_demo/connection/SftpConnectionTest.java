package sfdc.service.sfdc_service_demo.connection;

import com.jcraft.jsch.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.util.StringUtils.hasText;

public class SftpConnectionTest {

    /**
     * @SFTP 연결 테스트
     * 1. 연결 됐는지 확인
     * 2. 예외
        - 입력값이 null 인 경우
        - sftp 연결이 안된 경우
        - session 이 연결 안된 경우
        - 존재하지 않는  host 나 Port 일 경우
        - 연결 정보가 일치 하지 않는 경우
     */

    // ** 1. 연결 됐는지 확인
    @Test
    void sftp_justConnectionTest() {
        // ** 입력 깂이 4개 이상임으로 객체로 만드는 거 고려해보기
        SFTP_STATUS conn_status = common_connectTest("localhost",8080,"user","psdfasfds");
        assertEquals(conn_status,SFTP_STATUS.CONNECT);
    }

    private SFTP_STATUS  common_connectTest(String host,int port, String username, String password){
        SftpService service = new SftpService(this);
        try{
            ChannelSftp channelSftp =  service.createChannel(new SftpCredentials(host,port,username,password));
            return SFTP_STATUS.CONNECT;
        }
        catch (CustomSftpException e){
            return e.getStatus();
        }
    }

    @Test
    void sftp_hostIsNotNull() throws JSchException {
        assertThrows(IllegalArgumentException.class,()->{
            common_connectTest(null,8080,"user","psdfasfds");
        });

        assertThrows(IllegalArgumentException.class,()->{
            common_connectTest("",8080,"user","psdfasfds");
        });
        common_connectTest("localhost",8080,null,"psdfasfds");
    }

    @Test
    void sftp_CreateSession(){
        common_connectTest("localhost",8080,"user","psdfasfds");
    }


    @Test
    void sftp_LibraryException(){
        SFTP_STATUS conn_status = common_connectTest("localhost",8080,"user","psdfasfds");
//        assertEquals(conn_status,SFTP_STATUS.LIBRARY_ERROR);
    }


    @Test
    void credentialError(){
        SFTP_STATUS conn_status = common_connectTest("localhost",2222,"xiusk","ppppp");
        assertEquals(conn_status,SFTP_STATUS.AUTHENTICATION_ERROR);
    }


}
