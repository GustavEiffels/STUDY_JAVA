package sfdc.service.sfdc_service_demo.connection.test;

public class SftpFileInfo {
    private String extension;
    private String contentType;

    public SftpFileInfo(){}
    public SftpFileInfo(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
