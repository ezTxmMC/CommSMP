package de.commsmp.smp.message;

import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;

@JsonClassConfig(path = "plugins/SMP", fileName = "messages.json")
public class Messages {
    @JsonClassElement
    public static String PREFIX;
    @JsonClassElement
    public static String NO_PERMISSION;
    @JsonClassElement
    public static String ERROR;
    @JsonClassElement
    public static String POSITION_USAGE;
    @JsonClassElement
    public static String POSITION_SET;
    @JsonClassElement
    public static String POSITION_LIST;
    @JsonClassElement
    public static String POSITION_DELETE;
    @JsonClassElement
    public static String POSITION_PARTICLES_ON;
    @JsonClassElement
    public static String POSITION_PARTICLES_OFF;
    @JsonClassElement
    public static String TEAMCHAT_FORMAT;
    @JsonClassElement
    public static String TEAMCHAT_USAGE;
    @JsonClassElement
    public static String UNLOCK_USAGE;
    @JsonClassElement
    public static String UNLOCK_SUCCESS;
    @JsonClassElement
    public static String UNLOCK_NOT_OWNED;
    @JsonClassElement
    public static String LOCK_USAGE;
    @JsonClassElement
    public static String LOCK_AUTO_LOCK;
}
