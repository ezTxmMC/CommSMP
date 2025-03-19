package de.eztxm.smp;

import de.eztxm.smp.listener.ChatListener;
import de.eztxm.smp.listener.JoinListener;
import de.eztxm.smp.listener.QuitListener;
import de.eztxm.smp.util.PlayerManager;
import de.eztxm.smp.util.Registry;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SMP extends JavaPlugin {
    @Getter
    private static SMP instance;

    private LuckPerms luckPerms;

    private Registry registry;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        instance = this;
        this.luckPerms = LuckPermsProvider.get();
        this.registry = new Registry(instance);
        this.registry.registerListener(new JoinListener());
        this.registry.registerListener(new QuitListener());
        this.registry.registerListener(new ChatListener());
        this.playerManager = new PlayerManager();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
