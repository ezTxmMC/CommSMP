package de.commsmp.smp;

import de.commsmp.smp.backpack.BackpackManager;
import de.commsmp.smp.command.PositionCommand;
import de.commsmp.smp.command.TeamchatCommand;
import de.commsmp.smp.command.api.CommandAliases;
import de.commsmp.smp.command.api.SimpleCommandRegistry;
import de.commsmp.smp.config.Config;
import de.commsmp.smp.config.LockConfig;
import de.commsmp.smp.generation.CustomChunkGen;
import de.commsmp.smp.listener.ChatListener;
import de.commsmp.smp.listener.DeathListener;
import de.commsmp.smp.listener.JoinListener;
import de.commsmp.smp.listener.QuitListener;
import de.commsmp.smp.lock.LockListener;
import de.commsmp.smp.message.Messages;
import de.commsmp.smp.util.GraveStoneHandler;
import de.commsmp.smp.util.PlayerManager;
import de.commsmp.smp.util.Registry;
import de.eztxm.ezlib.config.reflect.JsonProcessor;
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
    private GraveStoneHandler graveStoneHandler;
    private BackpackManager backpackManager;

    @Getter
    private String prefix;
    @Getter
    private Config mainConfig;
    @Getter
    private Messages messages;
    @Getter
    private LockConfig lockConfig;

    @Override
    public void onEnable() {
        instance = this;
        this.prefix = "<#005fff><bold> CommSMP <dark_gray>|</bold> <gray>";
        this.mainConfig = JsonProcessor.loadConfiguration(Config.class).getInstance();
//        this.messages = JsonProcessor.loadConfiguration(Messages.class).getInstance();
        this.lockConfig = JsonProcessor.loadConfiguration(LockConfig.class).getInstance();
        this.luckPerms = LuckPermsProvider.get();

        registerCommands();

        this.registry = new Registry(instance);
        this.registry.registerListener(new JoinListener(this));
        this.registry.registerListener(new QuitListener(this));
        this.registry.registerListener(new ChatListener());
        this.registry.registerListener(new DeathListener());
        this.registry.registerListener(new LockListener(this));
        this.playerManager = new PlayerManager();
        this.graveStoneHandler = new GraveStoneHandler();
        this.backpackManager = new BackpackManager();
        //this.backpackManager.registerCrafting();

        if (!(getMainConfig().isNetheriteEnabled())) {
            String[] recipes = {"netherite_ingot", "netherite_block", "netherite_upgrade_smithing_template", "netherite_scrap", "netherite_helmet", "netherite_chestplate", "netherite_leggings", "netherite_boots", "netherite_sword", "netherite_pickaxe", "netherite_axe", "netherite_shovel", "netherite_hoe"};
            for (String recipe : recipes) {
                this.getServer().removeRecipe(NamespacedKey.minecraft(recipe));
            }
        }
    }

    private void registerCommands() {
        SimpleCommandRegistry commandRegistry = new SimpleCommandRegistry(this);
        commandRegistry.register("position", CommandAliases.of("setposition", "deleteposition", "delpos", "setpos", "pos", "poslist", "positionlist"), new PositionCommand());
        commandRegistry.register("teamchat", CommandAliases.of("tc"), new TeamchatCommand());
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
