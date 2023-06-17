package com.ofss.fc.serverstatus.BPDEnvServerStatus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerStats {
    private String activeHttpSessionCount;
    private Health health;
    private Long usedPhysicalMemory;
    private Long jvmProcessorLoad;
    private Long heapFreeCurrent;
    private Long heapSizeCurrent;
    private Long activeThreadCount;
    private String name;
    private String state;
    private String componentType;
}
