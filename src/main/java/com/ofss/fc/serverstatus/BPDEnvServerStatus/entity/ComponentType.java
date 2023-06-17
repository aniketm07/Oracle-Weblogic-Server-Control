package com.ofss.fc.serverstatus.BPDEnvServerStatus.entity;

public enum ComponentType {
    obh("Host"), soa("SOA"), osb("OSB"), obu("UI");
    private final String value;

    ComponentType(String value) {
        this.value = value;
    }

    public static String fromValue(String value) {

        for (ComponentType componentType : ComponentType.values()) {
            if (componentType.toString().equals(value)) {
                return componentType.value;
            }
        }
        return null;
    }
}
