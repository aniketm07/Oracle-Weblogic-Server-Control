package com.ofss.fc.serverstatus.BPDEnvServerStatus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInput {
    String envId;
    Boolean hostCheckBox;
    Boolean uiCheckBox;
    Boolean soaCheckBox;
    Boolean osbCheckBox;
    Boolean hide;
    String username;
    String password;
}
