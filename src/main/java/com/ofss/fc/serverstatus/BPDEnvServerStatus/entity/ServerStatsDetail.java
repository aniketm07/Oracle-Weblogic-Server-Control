package com.ofss.fc.serverstatus.BPDEnvServerStatus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerStatsDetail {
    private String progress;
    private String serverName;
    private String operation;
    private String taskStatus;
    private String completed;
    private String startTime;
}
