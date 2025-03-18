package de.eztxm.smp;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SMP extends JavaPlugin {
    @Getter
    private static SMP instance;
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        instance = this;
        this.luckPerms = LuckPermsProvider.get();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
