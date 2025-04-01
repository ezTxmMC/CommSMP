package de.commsmp.smp.config;

import java.util.ArrayList;
import java.util.List;

import de.commsmp.smp.config.data.MutedPlayer;
import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonClassConfig(path = "plugins/SMP", fileName = "mute.json")
public class MuteConfig {
    @JsonClassElement
    private List<MutedPlayer> mutedPlayers;

    public MuteConfig() {
        this.mutedPlayers = new ArrayList<>();
    }
}
