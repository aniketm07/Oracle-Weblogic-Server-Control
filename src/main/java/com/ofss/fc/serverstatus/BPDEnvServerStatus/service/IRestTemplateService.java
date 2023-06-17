package com.ofss.fc.serverstatus.BPDEnvServerStatus.service;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface IRestTemplateService {

    public RestTemplate getRestTemplate() throws NoSuchAlgorithmException, KeyManagementException;

    public HttpEntity<String> createHeaders(String username, String password);

    public HttpEntity<String> createHeadersForControl(String username, String password);
}
