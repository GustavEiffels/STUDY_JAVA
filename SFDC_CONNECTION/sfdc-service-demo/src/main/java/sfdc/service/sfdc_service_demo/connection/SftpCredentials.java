package sfdc.service.sfdc_service_demo.connection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Getter
@Setter
@Builder
public class SftpCredentials {
    private String host;
    private int port;
    private String username;
    private String password;

    public SftpCredentials(String host, int port, String username, String password) {
        Assert.notNull(host, "host 는 필수 값입니다.");
        Assert.isInstanceOf(Integer.class, port, "port 는 숫자 값입니다.");
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public SftpCredentials(SftpConnectionProperties properties) {
        Assert.hasText(properties.getHost(), "host 는 필수 값입니다.");
        Assert.isInstanceOf(Integer.class, properties.getPort(), "port 는 숫자 값입니다.");
        this.host = properties.getHost();
        this.port = properties.getPort();
        this.username = properties.getUser();
        this.password = properties.getPassword();
    }
}
