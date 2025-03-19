package de.eztxm.smp;

import de.eztxm.smp.util.PlayerManager;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SMP extends JavaPlugin {
    @Getter
    private static SMP instance;
    private LuckPerms luckPerms;

    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        instance = this;
        this.luckPerms = LuckPermsProvider.get();
        this.playerManager = new PlayerManager();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
