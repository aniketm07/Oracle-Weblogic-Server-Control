package com.ofss.fc.serverstatus.BPDEnvServerStatus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.constants.ServerConstants;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ComponentType;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerInput;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerStats;
import com.ofss.fc.serverstatus.BPDEnvServerStatus.entity.ServerStatsDetail;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author - @asmhatre
 * This is a helper class for ServerStatService.
 */
public class ServerStatServiceHelper {

    /**
     * Converts JSON to List of ServerStat
     * @param body Json String response body
     * @return Returns JSON to List of ServerStats
     */
    public List<ServerStats> toServerStatObject(String body, String component, ServerInput formData, RestTemplate restTemplate, HttpEntity<String> headers) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(body).get("items");
            List<ServerStats> serverStatsList = new ArrayList<>();

            arrayNode.forEach(jsonNode -> {
                try {
                    ServerStats serverStat = objectMapper.readValue(jsonNode.toString(), ServerStats.class);
                    serverStat.setComponentType(ComponentType.fromValue(component));
                    if(ServerConstants.STARTING.equals(serverStat.getState())){
                        addServerProcessingTime(serverStat, formData, component, restTemplate, headers);
                    }
                    serverStatsList.add(serverStat);
                } catch (JsonProcessingException | NoSuchAlgorithmException | KeyManagementException e) {
                    e.printStackTrace();
                }
            });
            return serverStatsList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * addServerProcessingTime Fetches the startTime of the server and accordingly appends the processedTime in the state.
     * @param serverStat serverStat
     */
    private void addServerProcessingTime(ServerStats serverStat, ServerInput formData, String component, RestTemplate restTemplate, HttpEntity<String> headers) throws NoSuchAlgorithmException, KeyManagementException, JsonProcessingException {
        String URL = ServerConstants.StatDetailsURLTemplate.replace(ServerConstants.ENV_ID,formData.getEnvId());
        URL = URL.replace(ServerConstants.SERVER_NAME, serverStat.getName());
        ResponseEntity<String> response = restTemplate.exchange(URL.replace(ServerConstants.COMPONENT, component), HttpMethod.GET, headers, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(response.getBody()).get("items");
        arrayNode.forEach(jsonNode -> {
            try {
                ServerStatsDetail serverStatDetail = objectMapper.readValue(jsonNode.toString(), ServerStatsDetail.class);
                if(serverStatDetail.getServerName().equals(serverStat.getName())
                        && serverStatDetail.getCompleted().equalsIgnoreCase("false")
                        && serverStatDetail.getOperation().equals("start")){
                    String startTime = serverStatDetail.getStartTime().substring(0, 23).replace("T", " ");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                    Date parsedDate = dateFormat.parse(startTime);
                    Date currentDate = new Date();
                    long duration = currentDate.getTime() - parsedDate.getTime();
                    long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                    serverStat.setState(serverStat.getState() + " (" + diffInMinutes +" minutes)");
                }
            } catch (JsonProcessingException | ParseException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Adds selected server for API call
     * @param formData formData
     * @return List of servers
     */
    public List<String> getComponentsInput(ServerInput formData) {
        List<String> arrayOfServers = new ArrayList<>();
        if(null!= formData.getHostCheckBox() && formData.getHostCheckBox() ){
            arrayOfServers.add("obh");
        }
        if(null!= formData.getOsbCheckBox() && formData.getOsbCheckBox()){
            arrayOfServers.add("osb");
        }
        if(null!= formData.getSoaCheckBox() && formData.getSoaCheckBox()){
            arrayOfServers.add("soa");
        }
        if(null!= formData.getUiCheckBox() && formData.getUiCheckBox()){
            arrayOfServers.add("obu");
        }
        return arrayOfServers;
    }

    public boolean isAdmin(ServerInput formData, ServerStats serverStat) {
        return null != formData.getHide() && formData.getHide() && serverStat.getName().startsWith("Admin");
    }
}
