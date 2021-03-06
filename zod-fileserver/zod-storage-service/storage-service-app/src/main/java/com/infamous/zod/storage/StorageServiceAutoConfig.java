package com.infamous.zod.storage;

import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.zod.base.jpa.JPACommonUtils;
import com.infamous.zod.storage.converter.StorageFileConverter;
import com.infamous.zod.storage.repository.StorageFileDataStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
public class StorageServiceAutoConfig {


    @Bean(name = "storageFileEMF")
    public LocalContainerEntityManagerFactoryBean createStorageFileEmf() {
        return JPACommonUtils.createEntityManagerFactory((emf) -> {
            emf.setPersistenceUnitName("fileserver-ds");
            emf.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
        });
    }

    @Bean
    public StorageFileDataStore createStorageFileDataStore(DataStoreManager dataStoreManager) {
        StorageFileDataStore dataStore = new StorageFileDataStore();
        dataStoreManager.register(StorageFileDataStore.DS_NAME, dataStore);
        return dataStore;
    }

    @Bean
    public StorageFileConverter createConverter() {
        return new StorageFileConverter();
    }
}
