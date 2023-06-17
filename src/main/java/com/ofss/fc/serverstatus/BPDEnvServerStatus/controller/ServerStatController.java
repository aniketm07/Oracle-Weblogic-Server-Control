package com.ofss.fc.serverstatus.BPDEnvServerStatus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.constants.ServerConstants;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerInput;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerStats;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerStatsForReload;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.service.IServerStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;

/**
 * Author - @asmhatre
 * This is a controller for server stats.
 */
@Controller
@Slf4j
public class ServerStatController {

    @Autowired
    IServerStatService serverStatService;

    ServerStatControllerHelper helper = new ServerStatControllerHelper();

    String response = "";

    @PostMapping(value = ServerConstants.ENVIRONMENT, consumes = {MULTIPART_FORM_DATA}, params="action=status")
    public String getEnvStatsForEnvName(@ModelAttribute ServerInput formData, Model model) throws NoSuchAlgorithmException, KeyManagementException, JsonProcessingException {

        List<ServerStats> listOfServerStats = serverStatService.getServerStats(formData);
        model.addAttribute("listOfServerStats", listOfServerStats);
        model.addAttribute("envId", "Servers - BPD"+formData.getEnvId());
        return ServerConstants.INDEX;
    }

    @PostMapping(value = "/environmentReload")
    public ResponseEntity<String> getEnvStatsForEnvName(@RequestBody String[] tableInput) throws NoSuchAlgorithmException, KeyManagementException, JsonProcessingException {

        ServerInput formData = helper.assembleFormData(tableInput);
        formData.setHide(true);
        List<ServerStats> listOfServerStats = serverStatService.getServerStats(formData);
        List<ServerStatsForReload> responseList = helper.createServerStatsForReload(listOfServerStats);
        ObjectMapper objectMapper = new ObjectMapper();
        response = objectMapper.writeValueAsString(responseList);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = ServerConstants.ENVIRONMENT, consumes = {MULTIPART_FORM_DATA}, params="action=shutdown")
    public String shutdownServer(@ModelAttribute ServerInput formData, Model model) throws NoSuchAlgorithmException, KeyManagementException, JsonProcessingException {

        if(helper.checkUsernamePassword(formData)) {
            return ServerConstants.ERROR_CRED;
        }
        try {
            response = serverStatService.controlServer(formData, "SHUTDOWN");
        }catch(HttpClientErrorException exception) {
            if(exception.getMessage() != null && exception.getMessage().contains("Unauthorized")){
                return ServerConstants.INVALID_ERROR_CRED;
            }
        }catch (Exception exception) {
            formData.setHide(true);
            List<ServerStats> listOfServerStats = serverStatService.getServerStats(formData);
            if(!listOfServerStats.get(0).getState().equalsIgnoreCase("shutdown")){
                throw exception;
            }
        }

        model.addAttribute("envDetails", "BPD" + formData.getEnvId() + " server shutdown completed.");
        return ServerConstants.INDEX;
    }

    @PostMapping(value = ServerConstants.ENVIRONMENT, consumes = {MULTIPART_FORM_DATA}, params="action=start")
    public String startServer(@ModelAttribute ServerInput formData, Model model) throws NoSuchAlgorithmException, KeyManagementException, JsonProcessingException {

        if(helper.checkUsernamePassword(formData)) {
            return ServerConstants.ERROR_CRED;
        }

        try {
            response = serverStatService.controlServer(formData, "START");
        }catch(HttpClientErrorException exception) {
            if(exception.getMessage() != null && exception.getMessage().contains("Unauthorized")){
                return ServerConstants.INVALID_ERROR_CRED;
            }
        }catch (Exception exception) {
            formData.setHide(true);
            List<ServerStats> listOfServerStats = serverStatService.getServerStats(formData);
            if(!listOfServerStats.get(0).getState().equalsIgnoreCase("starting")){
                throw exception;
            }
        }

        model.addAttribute("envDetails", "Server starting - BPD"+formData.getEnvId());
        return ServerConstants.INDEX;
    }

    @PostMapping(value = ServerConstants.ENVIRONMENT, consumes = {MULTIPART_FORM_DATA}, params="action=getLogs")
    public String getLogs(@ModelAttribute ServerInput formData, Model model) throws NoSuchAlgorithmException, KeyManagementException, JsonProcessingException {

        if(helper.checkUsernamePassword(formData)) {
            return ServerConstants.ERROR_CRED;
        }
        try {
            serverStatService.getLogs(formData);
        }catch(HttpClientErrorException exception) {
            if(exception.getMessage() != null && exception.getMessage().contains("Unauthorized")){
                return ServerConstants.INVALID_ERROR_CRED;
            }
        }

        model.addAttribute("envDetails", "BPD" + formData.getEnvId() + " server shutdown completed.");
        return ServerConstants.INDEX;
    }
}
