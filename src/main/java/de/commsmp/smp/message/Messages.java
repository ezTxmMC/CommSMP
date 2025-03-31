package de.commsmp.smp.message;

import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import lombok.Getter;

@Getter
@JsonClassConfig(path = "plugins/SMP", fileName = "messages.json")
public class Messages {
    @JsonClassElement
    public String prefix;
    @JsonClassElement
    public String noPermission;
    @JsonClassElement
    public String error;
    @JsonClassElement
    public String positionUsage;
    @JsonClassElement
    public String positionSet;
    @JsonClassElement
    public String positionList;
    @JsonClassElement
    public String positionDelete;
    @JsonClassElement
    public String positionParticlesOn;
    @JsonClassElement
    public String positionParticlesOff;
    @JsonClassElement
    public String teamchatFormat;
    @JsonClassElement
    public String teamchatUsage;
    @JsonClassElement
    public String unlockUsage;
    @JsonClassElement
    public String unlockSuccess;
    @JsonClassElement
    public String unlockNotOwned;
    @JsonClassElement
    public String lockUsage;
    @JsonClassElement
    public String lockAutoLock;

    public Messages() {
        prefix = "";
        noPermission = "";
        error = "";
        positionUsage = "";
        positionSet = "";
        positionList = "";
        positionDelete = "";
        positionParticlesOn = "";
        positionParticlesOff = "";
        teamchatFormat = "";
        teamchatUsage = "";
        unlockUsage = "";
        unlockSuccess = "";
        unlockNotOwned = "";
        lockUsage = "";
        lockAutoLock = "";
    }
}
