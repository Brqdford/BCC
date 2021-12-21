package io.github.hhservers.bclanchat;

import com.google.inject.Inject;
import io.github.hhservers.bclanchat.commands.admin.AdminBase;
import io.github.hhservers.bclanchat.commands.user.Base;
import io.github.hhservers.bclanchat.config.ConfigHandler;
import io.github.hhservers.bclanchat.config.MainPluginConfig;
import io.github.hhservers.bclanchat.util.Util;
import io.github.hhservers.bclanchat.util.objects.Clan;
import io.github.hhservers.bclanchat.util.objects.Request;
import lombok.Getter;
import lombok.Setter;
import ninja.leaping.configurate.objectmapping.GuiceObjectMapperFactory;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.TeamMember;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    private static MainPluginConfig mainPluginConfig;
    private final GuiceObjectMapperFactory factory;
    private final File configDir;
    private static ConfigHandler configHandler;

    public static Map<UUID, Boolean> chatToggle = new HashMap<>();

    public static List<Clan> clanList = new ArrayList<>();

    @Getter
    private final List<Task> pluginTasks = new ArrayList<>();
    @Getter
    private final List<Request> requests = new ArrayList<>();

    @Getter
    public Util util = new Util();


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
        List<Clan> tmpList = configHandler.getPluginConf().getClanList();
        clanList = tmpList;
        Sponge.getCommandManager().register(instance, Base.build(), getMainPluginConfig().getCommandPrefix());
        Sponge.getCommandManager().register(instance, AdminBase.build(), getMainPluginConfig().getAdminCommandPrefix());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        Task.builder()
                .interval(getMainPluginConfig().getSaveTimer(), TimeUnit.MINUTES)
                .delay(getMainPluginConfig().getSaveTimer(), TimeUnit.MINUTES)
                .execute(() -> {
                    getInstance().getLogger().info("saving groups task...");
                    try {
                        util.saveClansConfig();
                    } catch (IOException|ObjectMappingException ex) {
                        ex.printStackTrace();
                    }
                })
                .submit(getInstance());
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent e) throws IOException, ObjectMappingException {
        util.saveClansConfig();
    }

    @Listener
    public void onGameReload(GameReloadEvent e) throws IOException, ObjectMappingException {
        reloadConfig();
    }


    @Listener
    public void onMessageChannel(MessageChannelEvent.Chat e, @First Player p) {

        Optional<Team> team = p.getScoreboard().getMemberTeam(p.getTeamRepresentation());
        String textString = e.getRawMessage().toPlain();
        if (chatToggle.containsKey(p.getUniqueId())) {
            if (chatToggle.get(p.getUniqueId())) {
                if (util.getPlayerClan(p.getUniqueId()).isPresent()) {
                    Clan playerClan = util.getPlayerClan(p.getUniqueId()).get();
                    for (UUID uuid : playerClan.getPlayerList()) {
                        if (Sponge.getServer().getPlayer(uuid).isPresent()) {
                            if (p.hasPermission(SubjectData.GLOBAL_CONTEXT, "tabmanager.group.pink")) {
                                String msg = TextSerializers.FORMATTING_CODE.serialize(e.getRawMessage());
                                e.setCancelled(true);
                                Sponge.getServer().getPlayer(uuid).get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §d") + p.getName() + "§f: " + msg.replace("grp!", "")));
                                Sponge.getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §d") + p.getName() + "§f: " + msg));
                            } else if (p.hasPermission(SubjectData.GLOBAL_CONTEXT, "tabmanager.group.blue")) {
                                String msg = TextSerializers.FORMATTING_CODE.serialize(e.getRawMessage());
                                e.setCancelled(true);
                                Sponge.getServer().getPlayer(uuid).get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §b") + p.getName() + "§f: " + msg.replace("grp!", "")));
                                Sponge.getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §b") + p.getName() + "§f: " + msg));
                            } else {
                                String msg = TextSerializers.FORMATTING_CODE.serialize(e.getRawMessage());
                                e.setCancelled(true);
                                Sponge.getServer().getPlayer(uuid).get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] ") + p.getName() + ": " + msg.replace("grp!", "")));
                                Sponge.getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] ") + p.getName() + ": " + msg));
                            }
                        }
                    }
                } else {
                    p.sendMessage(util.prefixSerializer("You cannot send group messages if you are not in a group!"));
                    e.setCancelled(true);
                }
            }
        }

        if (textString.contains("grp!")) {
            if (util.getPlayerClan(p.getUniqueId()).isPresent()) {
                if (chatToggle.containsKey(p.getUniqueId())) {
                    if (chatToggle.get(p.getUniqueId())) {
                        return;
                    } else {
                        Clan playerClan = util.getPlayerClan(p.getUniqueId()).get();
                        for (UUID uuid : playerClan.getPlayerList()) {
                            if (Sponge.getServer().getPlayer(uuid).isPresent()) {
                                if (p.hasPermission(SubjectData.GLOBAL_CONTEXT, "tabmanager.group.pink")) {
                                    String msg = TextSerializers.FORMATTING_CODE.serialize(e.getRawMessage());
                                    e.setCancelled(true);
                                    Sponge.getServer().getPlayer(uuid).get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §d") + p.getName() + "§f: " + msg.replace("grp!", "")));
                                    Sponge.getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §d") + p.getName() + "§f: " + msg));
                                } else if (p.hasPermission(SubjectData.GLOBAL_CONTEXT, "tabmanager.group.blue")) {
                                    String msg = TextSerializers.FORMATTING_CODE.serialize(e.getRawMessage());
                                    e.setCancelled(true);
                                    Sponge.getServer().getPlayer(uuid).get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §b") + p.getName() + "§f: " + msg.replace("grp!", "")));
                                    Sponge.getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §b") + p.getName() + "§f: " + msg));
                                } else {
                                    String msg = TextSerializers.FORMATTING_CODE.serialize(e.getRawMessage());
                                    e.setCancelled(true);
                                    Sponge.getServer().getPlayer(uuid).get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] ") + p.getName() + ": " + msg.replace("grp!", "")));
                                    Sponge.getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] ") + p.getName() + ": " + msg));
                                }
                            }
                        }
                    }
                } else {
                    Clan playerClan = util.getPlayerClan(p.getUniqueId()).get();
                    for (UUID uuid : playerClan.getPlayerList()) {
                        if (Sponge.getServer().getPlayer(uuid).isPresent()) {
                            if (p.hasPermission(SubjectData.GLOBAL_CONTEXT, "tabmanager.group.pink")) {
                                String msg = TextSerializers.FORMATTING_CODE.serialize(e.getRawMessage());
                                e.setCancelled(true);
                                Sponge.getServer().getPlayer(uuid).get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §d") + p.getName() + "§f: " + msg.replace("grp!", "")));
                                Sponge.getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §d") + p.getName() + "§f: " + msg));
                            } else if (p.hasPermission(SubjectData.GLOBAL_CONTEXT, "tabmanager.group.blue")) {
                                String msg = TextSerializers.FORMATTING_CODE.serialize(e.getRawMessage());
                                e.setCancelled(true);
                                Sponge.getServer().getPlayer(uuid).get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §b") + p.getName() + "§f: " + msg.replace("grp!", "")));
                                Sponge.getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] §b") + p.getName() + "§f: " + msg));
                            } else {
                                String msg = TextSerializers.FORMATTING_CODE.serialize(e.getRawMessage());
                                e.setCancelled(true);
                                Sponge.getServer().getPlayer(uuid).get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] ") + p.getName() + ": " + msg.replace("grp!", "")));
                                Sponge.getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&8[&9" + playerClan.getClanID() + "&8]&r &f" + team.get().getPrefix().toPlain().replace("[", "§f[§3").replace("]", "§f] ") + p.getName() + ": " + msg));
                            }
                        }
                    }
                }
            }else if(chatToggle.containsKey(p.getUniqueId())){
                if (chatToggle.get(p.getUniqueId())){
                    return;
                }else {
                    p.sendMessage(util.prefixSerializer("You cannot send group messages if you are not in a group!"));
                    e.setCancelled(true);
                }
            } else{
                p.sendMessage(util.prefixSerializer("You cannot send group messages if you are not in a group!"));
                e.setCancelled(true);
            }
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

    public static MainPluginConfig getMainPluginConfig() {
        return mainPluginConfig;
    }

    public static ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
