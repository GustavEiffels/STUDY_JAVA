package sfdc.service.sfdc_service_demo.connection;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


public class SftpCredentials {
    private String host;
    private int port;
    private String username;
    private String password;


    public SftpCredentials(String host, int port, String username, String password) {
        Assert.hasText(host, "host 는 필수 값입니다.");
        Assert.notNull(host, "host 는 필수 값입니다.");
        Assert.isInstanceOf(Integer.class, port, "port 는 숫자 값입니다.");
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
