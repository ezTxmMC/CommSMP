package de.eztxm.smp;

import de.eztxm.ezlib.config.reflect.JsonProcessor;
import de.eztxm.smp.chunk.CustomChunkGen;
import de.eztxm.smp.command.PositionCommand;
import de.eztxm.smp.command.api.CommandAliases;
import de.eztxm.smp.command.api.SimpleCommandRegistry;
import de.eztxm.smp.config.Config;
import de.eztxm.smp.config.LockConfig;
import de.eztxm.smp.listener.ChatListener;
import de.eztxm.smp.listener.DeathListener;
import de.eztxm.smp.listener.JoinListener;
import de.eztxm.smp.listener.QuitListener;
import de.eztxm.smp.lock.LockListener;
import de.eztxm.smp.util.PlayerManager;
import de.eztxm.smp.util.Registry;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.Nullable;

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
    @Getter
    private Config mainConfig;

    @Override
    public void onEnable() {
        instance = this;
        this.prefix = "<#005fff><bold> CommSMP <dark_gray>|</bold> <gray>";
        this.mainConfig = JsonProcessor.loadConfiguration(Config.class).getInstance();
        this.lockConfig = new LockConfig();
        this.luckPerms = LuckPermsProvider.get();

        registerCommands();

        this.registry = new Registry(instance);
        this.registry.registerListener(new JoinListener(this));
        this.registry.registerListener(new QuitListener(this));
        this.registry.registerListener(new ChatListener());
        this.registry.registerListener(new DeathListener());
        this.registry.registerListener(new LockListener(this));
        this.playerManager = new PlayerManager();
        String[] recipes = {
                "netherite_ingot",
                "netherite_block",
                "netherite_upgrade_smithing_template",
                "netherite_scrap",
                "netherite_helmet",
                "netherite_chestplate",
                "netherite_leggings",
                "netherite_boots",
                "netherite_sword",
                "netherite_pickaxe",
                "netherite_axe",
                "netherite_shovel",
                "netherite_hoe"
        };
        for (String recipe : recipes) {
            this.getServer().removeRecipe(NamespacedKey.minecraft(recipe));
        }
    }

    private void registerCommands() {
        SimpleCommandRegistry commandRegistry = new SimpleCommandRegistry(this);
        commandRegistry.register("position", CommandAliases.of("setposition", "deleteposition", "delpos", "setpos", "pos", "poslist", "positionlist"), new PositionCommand());
    }

    @Override
    public void onDisable() {
        this.lockConfig.save();
        instance = null;
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(String worldName, @Nullable String id) {
        if (worldName.toLowerCase().contains("nether")) {
            return new CustomChunkGen();
        }
        return super.getDefaultWorldGenerator(worldName, id);
    }
}
