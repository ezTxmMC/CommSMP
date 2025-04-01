package de.commsmp.smp;

import de.commsmp.smp.backpack.BackpackManager;
import de.commsmp.smp.command.*;
import de.commsmp.smp.command.api.CommandAliases;
import de.commsmp.smp.command.api.SimpleCommandRegistry;
import de.commsmp.smp.config.*;
import de.commsmp.smp.generation.CustomChunkGen;
import de.commsmp.smp.listener.*;
import de.commsmp.smp.lock.LockListener;
import de.commsmp.smp.message.Messages;
import de.commsmp.smp.util.GraveStoneHandler;
import de.commsmp.smp.util.PlayerManager;
import de.commsmp.smp.util.Registry;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import org.bukkit.NamespacedKey;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.Nullable;

@Getter
public final class SMP extends JavaPlugin {
    @Getter
    private static SMP instance;

    private LuckPerms luckPerms;

    private Registry registry;
    private PlayerManager playerManager;
    private GraveStoneHandler graveStoneHandler;
    private BackpackManager backpackManager;

    @Getter
    private String prefix;

    @Getter
    private ConfigProvider configProvider;

    @Getter
    private Config mainConfig;
    @Getter
    private Messages messages;
    @Getter
    private BanConfig banConfig;
    @Getter
    private MuteConfig muteConfig;
    @Getter
    private LockConfig lockConfig;

    private BukkitTask checker;

    @Override
    public void onEnable() {
        instance = this;
        this.prefix = "<#005fff><bold> CommSMP <dark_gray>|</bold> <gray>";

        initConfig();

        this.luckPerms = LuckPermsProvider.get();

        registerCommands();

        this.registry = new Registry(instance);
        this.registry.registerListener(new JoinListener(this));
        this.registry.registerListener(new QuitListener(this));
        this.registry.registerListener(new ChatListener());
        this.registry.registerListener(new DeathListener());
        this.registry.registerListener(new LockListener(this));
        this.registry.registerListener(new HarvestListener());
        this.registry.registerListener(new SitListener());
        this.registry.registerListener(new PassiveListener());
        this.playerManager = new PlayerManager();
        this.graveStoneHandler = new GraveStoneHandler();
        this.backpackManager = new BackpackManager();
        this.backpackManager.registerCrafting();

        if (!(getMainConfig().isNetheriteEnabled())) {
            String[] recipes = { "netherite_ingot", "netherite_block", "netherite_upgrade_smithing_template",
                    "netherite_scrap", "netherite_helmet", "netherite_chestplate", "netherite_leggings",
                    "netherite_boots", "netherite_sword", "netherite_pickaxe", "netherite_axe", "netherite_shovel",
                    "netherite_hoe" };
            for (String recipe : recipes) {
                this.getServer().removeRecipe(NamespacedKey.minecraft(recipe));
            }
        }
    }

    private void initConfig() {
        configProvider = new ConfigProvider();
        configProvider.registerConfig(BanConfig.class);
        configProvider.registerConfig(MuteConfig.class);
        configProvider.registerConfig(Config.class);
        configProvider.registerConfig(Messages.class);

        this.banConfig = configProvider.getProcessor(BanConfig.class).getInstance();
        this.muteConfig = configProvider.getProcessor(MuteConfig.class).getInstance();
        this.mainConfig = configProvider.getProcessor(Config.class).getInstance();
        this.messages = configProvider.getProcessor(Messages.class).getInstance();

        this.lockConfig = new LockConfig();
    }

    private void registerCommands() {
        SimpleCommandRegistry commandRegistry = new SimpleCommandRegistry(this);
        commandRegistry.register("position", CommandAliases.of("setposition", "deleteposition", "delpos", "setpos",
                "pos", "poslist", "positionlist"), new PositionCommand());
        commandRegistry.register("teamchat", CommandAliases.of("tc"), new TeamchatCommand());
        commandRegistry.register("status", CommandAliases.of("none", "afk", "live", "rec"),
                new StatusCommand());
        commandRegistry.register("mode", CommandAliases.of("none", "passive", "roleplay"),
                new ModeCommand());
        commandRegistry.register("ban", CommandAliases.of("unban"), new BanCommand());
        commandRegistry.register("mute", CommandAliases.of("unmute"), new MuteCommand());
        commandRegistry.register("lock", CommandAliases.of("unlock", "trust", "untrust"), new LockCommand());
        commandRegistry.register("setspawn", CommandAliases.of(), new SetSpawnCommand());
    }

    @Override
    public void onDisable() {
        this.lockConfig.save();
        this.checker.cancel();
        this.checker = null;
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
