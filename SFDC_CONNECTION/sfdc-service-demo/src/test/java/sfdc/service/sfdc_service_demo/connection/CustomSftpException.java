package sfdc.service.sfdc_service_demo.connection;

public class CustomSftpException extends RuntimeException {
    private final SFTP_STATUS status;


    public CustomSftpException(SFTP_STATUS status, String message) {
        super(message);
        this.status = status;
    }


    public CustomSftpException(SFTP_STATUS status) {
        this.status = status;
    }

    public SFTP_STATUS getStatus() {
        return status;
    }

}
