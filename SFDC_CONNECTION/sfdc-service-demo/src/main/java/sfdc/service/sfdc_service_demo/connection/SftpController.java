package sfdc.service.sfdc_service_demo.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class SftpController {

    private final SftpService sftpService;

    @PostMapping("/download")
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
    public ResponseEntity<InputStreamResource> download(@RequestBody  SftpHttpDto.DownloadRequest request){

        System.out.println("request.getPath() : "+request.getPath());
        InputStream     inputStream = sftpService.download(request.getPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }

    @PostMapping("/upload")
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
    public ResponseEntity<SftpHttpDto.UploadResponse> upload(@RequestParam("file") MultipartFile file){
        String path = sftpService.upload("/upload",file);
        System.out.println("path : "+path);
        return new ResponseEntity<>(
                SftpHttpDto.UploadResponse.builder()
                        .path(path)
                        .build(),HttpStatus.OK);
    }


}
