package com.ofss.fc.serverstatus.BPDEnvServerStatus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerStatsForReload {

    private String name;
    private String state;
    private String componentType;
}
