package de.commsmp.smp.config;

import java.util.ArrayList;
import java.util.List;

import de.commsmp.smp.config.data.BannedPlayer;
import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonClassConfig(path = "plugins/SMP", fileName = "ban.json")
public class BanConfig {
    @JsonClassElement
    private List<BannedPlayer> bannedPlayers;

    public BanConfig() {
        this.bannedPlayers = new ArrayList<>();
    }
}
