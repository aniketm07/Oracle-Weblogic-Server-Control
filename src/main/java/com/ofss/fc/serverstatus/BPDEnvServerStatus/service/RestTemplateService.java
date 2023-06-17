package com.ofss.fc.serverstatus.BPDEnvServerStatus.service;

import com.ofss.fc.serverstatus.BPDEnvServerStatus.util.SSLUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Author - @asmhatre
 * This is a service which formulates RestTemplate object.
 */
@Service
@Slf4j
public class RestTemplateService implements IRestTemplateService{

    private static final String defaultUsername = "readonly";
    private static final String defaultPassword = "readonly1";
    private static final String PROXY = "<proxy>";

    /**
     * This method returns the RestTemplate which would be used to call the REST API
     * @return RestTemplate
     * @throws NoSuchAlgorithmException NoSuchAlgorithm Exception
     * @throws KeyManagementException KeyManagement Exception
     */
    @Override
    public RestTemplate getRestTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY, 80));
        requestFactory.setConnectTimeout(10000);
        requestFactory.setProxy(proxy);
        SSLUtil.turnOffSslChecking();
        return new RestTemplate(requestFactory);
    }

    /**
     * This method creates HTTP headers and add Authorization header we just created
     * @return Headers
     */
    @Override
    public HttpEntity<String> createHeaders(String username, String password){

        String credentials = defaultUsername+":"+defaultPassword;
        String base64Credentials = new String(Base64.encodeBase64(credentials.getBytes(),false));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Credentials);
        return new HttpEntity<>(headers);
    }

    @Override
    public HttpEntity<String> createHeadersForControl(String username, String password){

        String credentials = username+":"+password;
        String base64Credentials = new String(Base64.encodeBase64(credentials.getBytes(),false));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Requested-By", "Developer");
        headers.add("Authorization", "Basic " + base64Credentials);
        log.info(base64Credentials);
        return new HttpEntity<>("{}",headers);
    }
}
