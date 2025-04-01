package de.commsmp.smp.config;

import java.time.Instant;
import java.util.*;

import de.commsmp.smp.config.data.BannedPlayer;
import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import de.eztxm.ezlib.config.reflect.JsonProcessor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonClassConfig(path = "plugins/SMP", fileName = "ban.json")
public class BanConfig {

    private JsonProcessor<BanConfig> processor;

    private Map<UUID, BannedPlayer> cache = new HashMap<>();

    @JsonClassElement
    private List<BannedPlayer> bannedPlayers;

    public BanConfig() {
        this.bannedPlayers = Collections.synchronizedList(new ArrayList<>());
    }

    public boolean isBanned(UUID uniqueId) {
        BannedPlayer bannedPlayer = cache.get(uniqueId);
        if (bannedPlayer == null) {
            return false;
        }

        if (bannedPlayer.getDuration() < 0 || Instant.parse(bannedPlayer.getTimestamp())
                .plusSeconds(bannedPlayer.getDuration()).isBefore(Instant.now())) {
            return true;
        }
        cache.remove(uniqueId);
        bannedPlayers.remove(bannedPlayer);
        processor.saveConfiguration();
        return false;
    }

    private void onLoad() {
        for (BannedPlayer bannedPlayer : getBannedPlayers()) {
            cache.put(UUID.fromString(bannedPlayer.getUniqueId()), bannedPlayer);
        }
    }
}
