package sfdc.service.sfdc_service_demo.connection;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sftp")
@Getter @Setter
public class SftpConnectionProperties {
    private String  host;
    private int     port;
    private String  user;
    private String  password;

    public SftpCredentials connection(){
        return new SftpCredentials(this.host,this.port,this.user,this.password);
    }
}
