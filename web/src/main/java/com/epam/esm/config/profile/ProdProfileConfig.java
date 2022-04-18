package com.epam.esm.config.profile;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@ComponentScan("com.epam.esm")
//@PropertySource("classpath:application.properties")
public class ProdProfileConfig {

//    private Environment env;
//
//    @Autowired
//    public void setEnv(Environment env) {
//        this.env = env;
//    }
//
//    @Bean
//    public BasicDataSource dataSource() {
//        BasicDataSource ds = new BasicDataSource();
//        ds.setDriverClassName(env.getProperty("db.driverClassName"));
//        ds.setUrl(env.getProperty("db.url"));
//        ds.setUsername(env.getProperty("db.user"));
//        ds.setPassword(env.getProperty("db.password"));
//        ds.setInitialSize(Integer.valueOf(env.getProperty("db.initialSize")));
//        return ds;
//    }
//
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
}
