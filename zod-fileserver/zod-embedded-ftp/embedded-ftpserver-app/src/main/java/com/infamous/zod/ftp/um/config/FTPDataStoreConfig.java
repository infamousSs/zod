package com.infamous.zod.ftp.um.config;

import com.infamous.framework.file.FileService;
import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.sensitive.service.ClearTextPasswordEncryptor;
import com.infamous.framework.sensitive.service.DefaultPasswordEncryptor;
import com.infamous.framework.sensitive.service.PasswordEncryptor;
import com.infamous.framework.sensitive.service.SaltedPasswordEncryptor;
import com.infamous.zod.base.jpa.JPACommonUtils;
import com.infamous.zod.ftp.FTPServerConfigProperties;
import com.infamous.zod.ftp.um.FTPDataStore;
import com.infamous.zod.ftp.um.FTPUserDAO;
import com.infamous.zod.ftp.um.FTPUserManager;
import com.infamous.zod.ftp.um.impl.EncryptStrategy;
import com.infamous.zod.ftp.um.impl.FTPUserDAOImpl;
import com.infamous.zod.ftp.um.impl.FTPUserManagerRepository;
import java.nio.file.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class FTPDataStoreConfig {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(FTPDataStoreConfig.class, "ftp.server");

    @Bean(name = "ftpServerEMF")
    public LocalContainerEntityManagerFactoryBean createEmf() {
        return JPACommonUtils.createEntityManagerFactory((emf) -> {
            emf.setPersistenceUnitName("ftp-ds");
            emf.setPersistenceXmlLocation("classpath:META-INF/persistence-ftp.xml");
        });
    }

    @Bean
    public FTPDataStore createFTPDataStore(DataStoreManager dataStoreManager) {
        FTPDataStore dataStore = new FTPDataStore();
        dataStoreManager.register(FTPDataStore.DS_NAME, dataStore);
        return dataStore;
    }

    @Bean
    public FTPUserDAO createFTPUserDAO(DataStoreManager dsm) {
        return new FTPUserDAOImpl(dsm);
    }

    @Bean
    public PasswordEncryptor createPasswordEncryptor(FTPServerConfigProperties config) {
        EncryptStrategy et = EncryptStrategy.find(config.getEncryptorStrategy());
        LOGGER.info("Using '" + et + "' strategy for password encryptor");
        if (et == EncryptStrategy.SALTED) {
            return new SaltedPasswordEncryptor();
        } else if (et == EncryptStrategy.CLEAR) {
            return new ClearTextPasswordEncryptor();
        } else {
            return new DefaultPasswordEncryptor();
        }
    }

    @Bean
    public FTPUserManager createUserManger(FTPUserDAO dao, PasswordEncryptor pe, FileService fileService,
                                           FTPServerConfigProperties serverConfig) {
        Path rootPath = fileService.createDirectory(serverConfig.getRootFolder());
        fileService.setRootFolder(rootPath);
        return new FTPUserManagerRepository(dao, pe, serverConfig);
    }
}
