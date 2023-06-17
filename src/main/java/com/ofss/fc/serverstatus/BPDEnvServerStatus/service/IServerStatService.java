package com.ofss.fc.serverstatus.BPDEnvServerStatus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerInput;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerStats;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface IServerStatService {

    List<ServerStats> getServerStats(ServerInput formData) throws NoSuchAlgorithmException, KeyManagementException, JsonProcessingException;
    String controlServer(ServerInput formData, String action) throws NoSuchAlgorithmException, KeyManagementException;
    void getLogs(ServerInput formData);
}
