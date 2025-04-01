package de.commsmp.smp.config;

import java.time.Instant;
import java.util.*;

import de.commsmp.smp.config.data.MutedPlayer;
import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import de.eztxm.ezlib.config.reflect.JsonProcessor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonClassConfig(path = "plugins/SMP", fileName = "mute.json")
public class MuteConfig {
    private JsonProcessor<MuteConfig> processor;

    private Map<UUID, MutedPlayer> cache = new HashMap<>();

    @JsonClassElement
    private List<MutedPlayer> mutedPlayers;

    public MuteConfig() {
        this.mutedPlayers = Collections.synchronizedList(new ArrayList<>());
    }

    public boolean isMuted(UUID uniqueId) {
        MutedPlayer mutedPlayer = cache.get(uniqueId);
        if(mutedPlayer == null) {
            return false;
        }

        if(mutedPlayer.getDuration() < 0 || Instant.parse(mutedPlayer.getTimestamp()).plusSeconds(mutedPlayer.getDuration()).isBefore(Instant.now())) {
            return true;
        }
        cache.remove(uniqueId);
        mutedPlayers.remove(mutedPlayer);
        processor.saveConfiguration();
        return false;
    }

    private void onLoad() {
        for (MutedPlayer mutedPlayer : getMutedPlayers()) {
            cache.put(UUID.fromString(mutedPlayer.getUniqueId()), mutedPlayer);
        }
    }
}
