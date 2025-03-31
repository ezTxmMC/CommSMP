package de.commsmp.smp.config;

import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import de.eztxm.ezlib.config.object.JsonArray;
import lombok.Getter;

@Getter
@JsonClassConfig
public class GraveStoneConfig {
    @JsonClassElement
    private final JsonArray graveStones;

    public GraveStoneConfig() {
        this.graveStones = new JsonArray();
    }
}
