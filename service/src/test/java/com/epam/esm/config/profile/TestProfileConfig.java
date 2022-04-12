package com.epam.esm.config.profile;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import repository.GiftCertificateRepository;
import repository.TagRepository;

@Configuration
@Profile("test")
@ComponentScan("com.epam.esm")
public class TestProfileConfig {
    @Bean
    public TagRepository tagRepository() {
        return Mockito.mock(TagRepository.class);
    }

    @Bean
    public GiftCertificateRepository giftCertificateRepository() {
        return Mockito.mock(GiftCertificateRepository.class);
    }
}
