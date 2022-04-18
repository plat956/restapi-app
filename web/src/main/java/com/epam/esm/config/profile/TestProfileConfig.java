package com.epam.esm.config.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@Profile("test")
@ComponentScan("com.epam.esm")
public class TestProfileConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/schema.sql")
                .addScript("classpath:sql/data.sql")
                .build();
    }

//    @Bean
//    public JdbcTemplate jdbcTemplate() {
//        return new JdbcTemplate(dataSource());
//    }
//
//    @Bean
//    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
//    public SimpleJdbcInsert simpleJdbcInsert() {
//        return new SimpleJdbcInsert(jdbcTemplate());
//    }
//
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
