package cn.ideal.mybatis.cfg;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private String driver;
    private String url;
    private String username;
    private String password;
    private Map<String, Mapper> mappers = new HashMap<String, Mapper>();

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Mapper> getMappers() {
        return mappers;
    }

    //特别说明：setMappers 需要使用putALL的追加写入方式，不能直接赋值，不然旧的就会被新的覆盖掉
    public void setMappers(Map<String, Mapper> mappers) {
        this.mappers.putAll(mappers); // 追加的方式
    }
}
