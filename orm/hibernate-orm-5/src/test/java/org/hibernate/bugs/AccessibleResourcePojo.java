package org.hibernate.bugs;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class AccessibleResourcePojo implements Serializable {
    private static final long serialVersionUID = 6564236646548707230L;

    private String protocol;
    private String host = "localhost";
    private int port;
    private String username;
    private String password;
    private String path;
    private Map<String, String> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private Map<String, String> publicParams = Collections.unmodifiableMap(params);

    public AccessibleResourcePojo() {
        super();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        int len = (path == null) ? 0 : path.length();
        char lastChar = (len <= 1) ? '\0' : path.charAt(len - 1);
        if ((lastChar == '/') || (lastChar == File.separatorChar)) {
            this.path = path.substring(0, len - 1);
        } else {
            this.path = path;
        }
    }

    /**
     * @return An <U>un-modifiable</U> case <U>insensitive</U> parameters map
     */
    public Map<String, String> getParams() {
        return publicParams;
    }

    public void clearParameters() {
        params.clear();
    }

    public String setParameter(String name, Object value) {
        String s = Objects.toString(value, null);
        if (s == null) {
            return removeParameter(name);
        }

        return params.put(name, s);
    }

    public String removeParameter(String name) {
        return params.remove(name);
    }

    public void setParams(Map<String, String> params) {
        this.params.clear();
        if (params != null) {
            this.params.putAll(params);
        }
    }

    @Override
    public int hashCode() {
        int portValue = getPort();
        return Objects.hash(getProtocol(), getHost(), getUsername(), getPassword(), getPath())
            + ((portValue <= 0) ? 0 : 37 * portValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        AccessibleResourcePojo other = (AccessibleResourcePojo) obj;
        return Objects.equals(getProtocol(), other.getProtocol())
            && Objects.equals(getHost(), other.getHost())
            && (getPort() == other.getPort())
            && Objects.equals(getUsername(), other.getUsername())
            && Objects.equals(getPassword(), other.getPassword())
            && Objects.equals(getPath(), other.getPath())
            ;
    }

    @Override
    public String toString() {
        return getProtocol() + "://" + getHost() + ":" + getPort() + "/" + getPath();
    }
}
