package com.epam.esm.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import repository.GiftCertificateRepository;
import repository.TagRepository;
import repository.impl.GiftCertificateRepositoryImpl;
import repository.impl.TagRepositoryImpl;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.epam.esm")
public class RootConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

    @Bean
    public TagRepository tagRepository() {
        return new TagRepositoryImpl();
    }

    @Bean
    public GiftCertificateRepository giftCertificateRepository() {
        return new GiftCertificateRepositoryImpl();
    }
}
