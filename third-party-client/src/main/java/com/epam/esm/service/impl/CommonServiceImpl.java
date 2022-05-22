package com.epam.esm.service.impl;

import com.epam.esm.service.CommonService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    @Override
    @SneakyThrows
    public JsonNode getRemoteData(String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return objectMapper.readTree(response.getBody());
    }

    @Override
    @SneakyThrows
    public JsonNode receiveAccessToken(String code) {
        String access_token_url = env.getProperty("oauth.server_token_url");
        access_token_url += "?client_id=" + env.getProperty("oauth.client_id");
        access_token_url += "&client_secret=" + env.getProperty("oauth.client_secret");
        access_token_url += "&code=" + code;
        access_token_url += "&grant_type=authorization_code";
        access_token_url += "&redirect_uri=" + env.getProperty("oauth.redirect_uri");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
        return objectMapper.readTree(response.getBody());
    }
}
