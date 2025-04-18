package de.commsmp.smp.config;

import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonClassConfig(path = "plugins/SMP/main", fileName = "config.json")
public class Config {

    @JsonClassElement
    private boolean netheriteEnabled;

    @JsonClassElement
    private String spawnLocation;

    public Config() {
        netheriteEnabled = false;
        spawnLocation = "world;0;0;0;0;0";
    }
}
