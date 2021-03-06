package com.infamous.zod.ftp.um.config;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.ftp.FTPServerConfigProperties;
import com.infamous.zod.ftp.model.FTPPassword;
import com.infamous.zod.ftp.model.FTPUser;
import com.infamous.zod.ftp.model.FTPUserName;
import com.infamous.zod.ftp.um.FTPDataStore;
import com.infamous.zod.ftp.um.FTPUserManager;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddAdminUserConfiguration {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(AddAdminUserConfiguration.class, "ftp.server");

    private final FTPServerConfigProperties m_adminProp;
    private final FTPUserManager m_ftpUserManager;
    private final FTPDataStore m_ds;
    private final ApplicationContext m_appContext;

    @Autowired
    public AddAdminUserConfiguration(FTPServerConfigProperties serverConfig, FTPUserManager um, FTPDataStore ds,
                                     ApplicationContext appContext) {
        this.m_adminProp = serverConfig;
        this.m_ftpUserManager = um;
        this.m_ds = ds;
        this.m_appContext = appContext;
    }

    @PostConstruct
    public void doPostConstruct() {
        try {
            if (!m_ds.isOpen()) {
                throw new IllegalStateException("Can not open connection to DB!");
            }
            FTPPassword passwordObj = new FTPPassword(m_adminProp.getPassword());
            String hashed = m_ftpUserManager.hashPassword(passwordObj.getPassword());
            FTPUser admin = FTPUser.builder()
                .username(new FTPUserName(m_adminProp.getUsername()).getUsername())
                .password(hashed)
                .enabled(true)
                .isAdmin(true)
                .idleTime(m_adminProp.getIdleTime())
                .maxConcurrentLogins(m_adminProp.getMaxConcurrentLogins())
                .maxDownloadRate(m_adminProp.getMaxDownloadRate())
                .maxUploadRate(m_adminProp.getMaxUploadRate())
                .workspace(m_adminProp.getRootFolder() + "/" + m_adminProp.getWorkspace())
                .build();
            admin.addDefaultAuthorities();
            m_ftpUserManager.save(admin);
            LOGGER.info("Added or updated admin user [" + m_adminProp.getUsername() + "] successfully");
        } catch (Exception e) {
            LOGGER.error("Error while adding or updating FTP admin user. Stopping application", e);
            SpringApplication.exit(m_appContext, () -> 1);
        }
    }
}
