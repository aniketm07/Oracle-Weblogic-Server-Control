package com.ofss.fc.serverstatus.BPDEnvServerStatus.controller;

import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerInput;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerStats;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerStatsForReload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Author - @asmhatre
 * This is a helper class for ServerStatController.
 */
public class ServerStatControllerHelper {

    public List<ServerStatsForReload> createServerStatsForReload(List<ServerStats> listOfServerStats) {
        List<ServerStatsForReload> serverStatsForReloadList = new ArrayList<>();
        listOfServerStats.forEach(server -> {
            ServerStatsForReload serverReload = new ServerStatsForReload();
            serverReload.setState(server.getState());
            serverReload.setName(server.getName());
            serverReload.setComponentType(server.getComponentType());
            serverStatsForReloadList.add(serverReload);
        });
        return serverStatsForReloadList;
    }

    public boolean checkUsernamePassword(ServerInput formData) {
        return Objects.isNull(formData.getPassword()) || Objects.isNull(formData.getUsername()) || formData.getPassword().equals("") || formData.getUsername().equals("");
    }

    public ServerInput assembleFormData(String[] tableInput) {
        ServerInput formData = new ServerInput();
        formData.setEnvId(tableInput[0]);
        Arrays.stream(tableInput).forEach(input -> {
            switch(input){
                case "Host": formData.setHostCheckBox(true);
                    break;
                case "UI": formData.setUiCheckBox(true);
                    break;
                case "OSB": formData.setOsbCheckBox(true);
                    break;
                case "SOA": formData.setSoaCheckBox(true);
                    break;
                default:
            }
        });
        return formData;
    }
}
