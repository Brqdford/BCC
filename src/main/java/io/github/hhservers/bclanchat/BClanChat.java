package io.github.hhservers.bclanchat;

import com.google.inject.Inject;
import io.github.hhservers.bclanchat.commands.Base;
import io.github.hhservers.bclanchat.config.ConfigHandler;
import io.github.hhservers.bclanchat.config.MainPluginConfig;
import io.github.hhservers.bclanchat.util.Util;
import io.github.hhservers.bclanchat.util.objects.Clan;
import lombok.Getter;
import lombok.Setter;
import ninja.leaping.configurate.objectmapping.GuiceObjectMapperFactory;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Plugin(
        id = "bclanchat",
        name = "BClanChat",
        description = "ClanChat plugin",
        authors = {
                "blvxr"
        }
)
public class BClanChat {

    @Getter
    private static BClanChat instance;
    @Getter
    @Inject
    private Logger logger;
    @Getter
    private static MainPluginConfig mainPluginConfig;
    private final GuiceObjectMapperFactory factory;
    private final File configDir;
    @Getter
    private static ConfigHandler configHandler;
    public static List<Clan> clanList = new ArrayList<>();
    @Getter
    private List<Task> pluginTasks = new ArrayList<>();
    @Getter
    @Setter
    private List<Player> currentlyClanChatting = new ArrayList<>();
    private Util util = new Util();


    @Inject
    public BClanChat(GuiceObjectMapperFactory factory, @ConfigDir(sharedRoot = false) File configDir) {
        this.factory=factory;
        this.configDir=configDir;
        instance=this;
    }

    @Listener
    public void onGamePreInit(GamePreInitializationEvent e) throws IOException, ObjectMappingException {
        reloadConfig();
    }

    @Listener
    public void onGameInit(GameInitializationEvent e){
        instance = this;
        Sponge.getCommandManager().register(instance, Base.build(), "bclans", "clan", "clans");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        clanList = configHandler.getPluginConf().getClanList();
    }

    @Listener
    public void onGameReload(GameReloadEvent e) throws IOException, ObjectMappingException {
        reloadConfig();
    }

    @Listener
    public void onMessageChannel(MessageChannelEvent.Chat e, @First Player p){
        if(currentlyClanChatting.contains(p)){
            p.sendMessage(Text.of("Yay you are clan chatting!!!"));
        }
    }

    public void reloadConfig() throws IOException, ObjectMappingException {
        configHandler=new ConfigHandler(this);
        if (configHandler.loadConfig()) {mainPluginConfig = configHandler.getPluginConf(); pluginTasks.forEach(task -> task.cancel()); /*util.saveClansTask();*/}
    }

    public GuiceObjectMapperFactory getFactory() {
        return factory;
    }

    public File getConfigDir() {
        return configDir;
    }
}
