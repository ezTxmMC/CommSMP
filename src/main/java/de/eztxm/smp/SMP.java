package de.eztxm.smp;

import de.eztxm.smp.config.LockConfig;
import de.eztxm.smp.listener.ChatListener;
import de.eztxm.smp.listener.JoinListener;
import de.eztxm.smp.listener.QuitListener;
import de.eztxm.smp.lock.LockListener;
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

    @Getter
    private String prefix;
    @Getter
    private LockConfig lockConfig;

    @Override
    public void onEnable() {
        this.lockConfig = LockConfig.load();

        instance = this;
        this.luckPerms = LuckPermsProvider.get();
        this.registry = new Registry(instance);
        this.registry.registerListener(new JoinListener(this));
        this.registry.registerListener(new QuitListener(this));
        this.registry.registerListener(new ChatListener());
        this.registry.registerListener(new LockListener(this));
        this.playerManager = new PlayerManager();
    }

    @Override
    public void onDisable() {
        this.lockConfig.save();
        instance = null;
    }

}
