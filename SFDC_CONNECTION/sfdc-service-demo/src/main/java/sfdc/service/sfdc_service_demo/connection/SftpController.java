package sfdc.service.sfdc_service_demo.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class SftpController {

    private final SftpService sftpService;

    @PostMapping("/download")
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
    public ResponseEntity<InputStreamResource> download(){
        SftpCredentials credentials = new SftpCredentials("localhost",2222,"xiuk","ppppp");
        InputStream     inputStream = sftpService.download(credentials,"/upload/4f967092-ab40-4f31-9880-e6dde3f3c4b6.jpg");

        System.out.println("file down load");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "test.png" + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }

    @PostMapping("/upload")
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file){
        SftpCredentials credentials = new SftpCredentials("localhost",2222,"xiuk","ppppp");
        String path = sftpService.upload(credentials,"/upload",file);
        System.out.println("file uploading");
        return ResponseEntity.ok()
                .body(path);
    }


}
