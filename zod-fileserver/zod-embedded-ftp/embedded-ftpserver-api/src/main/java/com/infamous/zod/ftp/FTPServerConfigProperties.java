package com.infamous.zod.ftp;

import org.springframework.beans.factory.annotation.Value;

public class FTPServerConfigProperties {

    @Value("${ftp.salted:wanderer}")
    private String m_salted;

    @Value("${ftp.root}")
    private String m_rootFolder;

    @Value("${ftp.port}")
    private int m_port;

    @Value("${ftp.user.admin.username}")
    private String m_username;

    @Value("${ftp.user.admin.password}")
    private String m_password;

    @Value("${ftp.user.admin.workspace}")
    private String m_workspace;

    @Value("${ftp.user.admin.idleTime}")
    private int m_idleTime;

    @Value("${ftp.user.admin.maxDownloadRate}")
    private int m_maxDownloadRate;

    @Value("${ftp.user.admin.maxUploadRate}")
    private int m_maxUploadRate;

    @Value("${ftp.user.admin.maxConcurrentLogins}")
    private int m_maxConcurrentLogins;

    @Value("${ftp.numOfLoginPerIp:2}")
    private int m_numOfLoginPerIp;

    @Value("${ftp.encryptor-strategy:salted}")
    private String m_encryptorStrategy;

    public String getUsername() {
        return m_username;
    }

    public String getPassword() {
        return m_password;
    }

    public String getWorkspace() {
        return m_workspace;
    }

    public int getIdleTime() {
        return m_idleTime;
    }

    public int getMaxDownloadRate() {
        return m_maxDownloadRate;
    }

    public int getMaxUploadRate() {
        return m_maxUploadRate;
    }

    public int getMaxConcurrentLogins() {
        return m_maxConcurrentLogins;
    }

    public int getNumOfLoginPerIp() {
        return m_numOfLoginPerIp;
    }

    public String getRootFolder() {
        return m_rootFolder;
    }

    public String getSalted() {
        return m_salted;
    }

    public int getPort() {
        return m_port;
    }

    public String getEncryptorStrategy() {
        return m_encryptorStrategy;
    }
}
