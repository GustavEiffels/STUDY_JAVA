package sfdc.service.sfdc_service_demo.connection;

import lombok.*;

public interface SftpHttpDto {

    @Getter
    @Setter
    @Builder
    class UploadRequest{
    }

    @Getter
    @Setter
    @Builder
    class UploadResponse{
        private String path;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class DownloadRequest{
        private String path;
    }

    @Getter
    @Setter
    @Builder
    class DownloadResponse{
    }
}
