package inventorymanagement.backend.model;

public class RedisConfig {
    private String host;
    private int port;
    private int timeout;
    private String password;
    private int maxPool;
    private int maxIdlePool;
    private int minIdlePool;

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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxPool() {
        return maxPool;
    }

    public void setMaxPool(int maxPool) {
        this.maxPool = maxPool;
    }

    public int getMaxIdlePool() {
        return maxIdlePool;
    }

    public void setMaxIdlePool(int maxIdlePool) {
        this.maxIdlePool = maxIdlePool;
    }

    public int getMinIdlePool() {
        return minIdlePool;
    }

    public void setMinIdlePool(int minIdlePool) {
        this.minIdlePool = minIdlePool;
    }
}
