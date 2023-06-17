package com.ofss.fc.serverstatus.BPDEnvServerStatus.service;

import com.ofss.fc.serverstatus.BPDEnvServerStatus.constants.ServerConstants;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerInput;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerStats;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Author - @asmhatre
 * This is a service which makes a REST call to the BPD Environment Weblogic API
 */
@Service
@Slf4j
public class ServerStatService implements IServerStatService{

    @Autowired
    RestTemplateService restTemplateService;
    ServerStatServiceHelper helper = new ServerStatServiceHelper();

    @Override
    public List<ServerStats> getServerStats(ServerInput formData) throws NoSuchAlgorithmException, KeyManagementException {

        RestTemplate restTemplate = restTemplateService.getRestTemplate();
        List<String> arrayOfServers = helper.getComponentsInput(formData);
        List<ServerStats> serverStats = new ArrayList<>();
        String URL = ServerConstants.StatURLTemplate.replace(ServerConstants.ENV_ID,formData.getEnvId());
        arrayOfServers.forEach(server -> {
            // Add ServerStat Object for the servers selected using radiobutton
            HttpEntity<String> headers = restTemplateService.createHeaders(formData.getUsername(), formData.getPassword());
            ResponseEntity<String> response = restTemplate.exchange(URL.replace(ServerConstants.COMPONENT, server),
                    HttpMethod.GET, headers, String.class);
            List<ServerStats> serverStatsOfComponent = helper.toServerStatObject(response.getBody(), server, formData, restTemplate, headers);
            if(Objects.nonNull(serverStatsOfComponent)){
                serverStatsOfComponent.removeIf(serverStat -> helper.isAdmin(formData, serverStat));
                serverStats.addAll(serverStatsOfComponent);
            }
        });
        return serverStats;
    }

    @Override
    public String controlServer(ServerInput formData, String action) throws NoSuchAlgorithmException, KeyManagementException {
        RestTemplate restTemplate = restTemplateService.getRestTemplate();
        List<String> arrayOfServers = helper.getComponentsInput(formData);

        String URL = "";
        if(action.equals("START")){
            URL = ServerConstants.StartURLTemplate.replace(ServerConstants.ENV_ID, formData.getEnvId());
        }
        else if(action.equals("SHUTDOWN")){
            URL = ServerConstants.ShutdownURLTemplate.replace(ServerConstants.ENV_ID, formData.getEnvId());
        }
        formData.setHide(true);
        List<ServerStats> serverStatsList =  getServerStats(formData);
        log.info(serverStatsList.toString());
        for(ServerStats serverStats : serverStatsList) {
            String finalURL = URL.replace(ServerConstants.COMPONENT, arrayOfServers.get(0));
            log.info(finalURL);
            // Start or Shutdown based on the URL
            ResponseEntity<String> response =
                    restTemplate.exchange(finalURL.replace(ServerConstants.SERVER_NAME, serverStats.getName()), HttpMethod.POST, restTemplateService.createHeadersForControl(formData.getUsername(), formData.getPassword()), String.class);
            return response.getBody();
        }
        return null;
    }

    @Override
    public void getLogs(ServerInput formData) {
        // TODO Get Logs using JSch Library
    }
}

