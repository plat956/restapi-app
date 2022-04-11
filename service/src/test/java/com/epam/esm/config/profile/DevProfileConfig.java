package com.epam.esm.config.profile;

import org.apache.commons.dbcp2.BasicDataSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import repository.GiftCertificateRepository;
import repository.TagRepository;

@Configuration
@Profile("dev")
@ComponentScan("com.epam.esm")
public class DevProfileConfig {

    @Bean //todo
    public BasicDataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/gift_cert");
        ds.setUsername("postgres");
        ds.setPassword("A@dmin123321");
        ds.setInitialSize(10);
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SimpleJdbcInsert simpleJdbcInsert() {
        return new SimpleJdbcInsert(jdbcTemplate());
    }

    @Bean
    public TagRepository tagRepository() {
        return Mockito.mock(TagRepository.class);
    }

    @Bean
    public GiftCertificateRepository giftCertificateRepository() {
        return Mockito.mock(GiftCertificateRepository.class);
    }
}
