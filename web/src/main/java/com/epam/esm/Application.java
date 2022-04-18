package com.epam.esm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate; //todo

	@Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SimpleJdbcInsert simpleJdbcInsert() {
        return new SimpleJdbcInsert(jdbcTemplate); //todo
    }
}
