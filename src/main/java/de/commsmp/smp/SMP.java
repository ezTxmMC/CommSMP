package de.commsmp.smp;

import de.commsmp.smp.backpack.BackpackManager;
import de.commsmp.smp.command.BanCommand;
import de.commsmp.smp.command.ModeCommand;
import de.commsmp.smp.command.PositionCommand;
import de.commsmp.smp.command.StatusCommand;
import de.commsmp.smp.command.TeamchatCommand;
import de.commsmp.smp.command.api.CommandAliases;
import de.commsmp.smp.command.api.SimpleCommandRegistry;
import de.commsmp.smp.config.BanConfig;
import de.commsmp.smp.config.Config;
import de.commsmp.smp.config.LockConfig;
import de.commsmp.smp.config.MuteConfig;
import de.commsmp.smp.generation.CustomChunkGen;
import de.commsmp.smp.listener.*;
import de.commsmp.smp.lock.LockListener;
import de.commsmp.smp.message.Messages;
import de.commsmp.smp.util.GraveStoneHandler;
import de.commsmp.smp.util.PlayerManager;
import de.commsmp.smp.util.Registry;
import de.eztxm.ezlib.config.reflect.JsonProcessor;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import org.bukkit.Bukkit;
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
    private Config mainConfig;
    @Getter
    private Messages messages;
    @Getter
    private JsonProcessor<BanConfig> banProcessor;
    @Getter
    private JsonProcessor<MuteConfig> muteProcessor;
    @Getter
    private LockConfig lockConfig;

    private BukkitTask checker;

    @Override
    public void onEnable() {
        instance = this;
        this.prefix = "<#005fff><bold> CommSMP <dark_gray>|</bold> <gray>";
        JsonProcessor<Config> mainProcessor = JsonProcessor.loadConfiguration(Config.class);
        JsonProcessor<Messages> messagesProcessor = JsonProcessor.loadConfiguration(Messages.class);
        banProcessor = JsonProcessor.loadConfiguration(BanConfig.class);
        muteProcessor = JsonProcessor.loadConfiguration(MuteConfig.class);
        mainProcessor.saveConfiguration();
        messagesProcessor.saveConfiguration();
        banProcessor.saveConfiguration();
        muteProcessor.saveConfiguration();
        this.mainConfig = mainProcessor.getInstance();
        this.messages = messagesProcessor.getInstance();
        this.lockConfig = new LockConfig();
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

        checker = Bukkit.getScheduler().runTaskTimerAsynchronously(instance, () -> {

        }, 20L, 10 * 20L);
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
