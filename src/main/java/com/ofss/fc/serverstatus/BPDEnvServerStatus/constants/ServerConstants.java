package com.ofss.fc.serverstatus.BPDEnvServerStatus.constants;

public final class ServerConstants {
    // Common Constants
    public static final String STARTING = "starting";
    public static final String ENV_ID = "/EnvName/";
    public static final String SERVER_NAME = "/ServerName/";
    public static final String COMPONENT = "/Component/";
    public static final String ENVIRONMENT = "/environment";
    // URL Template for BPD Environments
    public static final String StatURLTemplate = "<hostURL>/management/wls/latest/servers/"; // replace <hostURL> with the URL having Component and EnvName
    public static final String StartURLTemplate = "<hostURL>/management/weblogic/latest/domainRuntime/serverLifeCycleRuntimes/" + ServerConstants.SERVER_NAME + "/start";
    public static final String ShutdownURLTemplate = "<hostURL>/management/weblogic/latest/domainRuntime/serverLifeCycleRuntimes/" + ServerConstants.SERVER_NAME + "/forceShutdown?links=none";
    public static final String remoteDirectoryHost = "<pathToServerLogs>"; //TODO
    public static final String StatDetailsURLTemplate = "<hostURL>/management/weblogic/latest/domainRuntime/serverLifeCycleRuntimes//ServerName//tasks/?fields=completed,progress,serverName,operation,startTime,taskStatus&links=none";
    // HTML Constants
    public static final String INDEX = "index";
    public static final String ERROR_CRED = "errorCred";
    public static final String INVALID_ERROR_CRED = "invalidErrorCred";
}
