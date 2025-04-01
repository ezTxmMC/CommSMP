package de.commsmp.smp.util;

import java.util.UUID;

import de.commsmp.smp.config.BanConfig;
import de.commsmp.smp.config.MuteConfig;
import de.commsmp.smp.config.data.BannedPlayer;
import de.commsmp.smp.config.data.MutedPlayer;

public class CheckUtil {

    public static boolean isBanned(BanConfig config, UUID uniqueId) {
        boolean banned = false;
        for (BannedPlayer bannedPlayer : config.getBannedPlayers()) {
            if (!bannedPlayer.getUniqueId().equalsIgnoreCase(uniqueId.toString())) {
                continue;
            }
            banned = true;
        }
        return banned;
    }

    public static boolean isMuted(MuteConfig config, UUID uniqueId) {
        boolean banned = false;
        for (MutedPlayer mutedPlayer : config.getMutedPlayers()) {
            if (!mutedPlayer.getUniqueId().equalsIgnoreCase(uniqueId.toString())) {
                continue;
            }
            banned = true;
        }
        return banned;
    }
}
