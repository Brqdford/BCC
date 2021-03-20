package io.github.hhservers.bclanchat.util;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.config.MainPluginConfig;
import io.github.hhservers.bclanchat.util.objects.Clan;
import io.github.hhservers.bclanchat.util.objects.Request;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.util.*;

public class Util {

    private final BClanChat instance = BClanChat.getInstance();

    public Text prefixSerializer(String content) {
        return TextSerializers.FORMATTING_CODE.deserialize("&l&8[&r&bBClanChat&l&8]&r " + content);
    }

    public Text textSerializer(String content) {
        return TextSerializers.FORMATTING_CODE.deserialize(content);
    }

    public Boolean invertBoolean(Boolean value) {
        Boolean change;
        change = !value;
        return change;
    }

    public Optional<User> getUser(UUID uuid) {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        return userStorage.get().get(uuid);
    }

    public Boolean addUserToClan(User u, UUID owner) {
        if (getClanByOwner(owner).isPresent()) {
            return getClanByOwner(owner).get().addPlayer(u.getUniqueId());
        }
        return false;
    }

    public Boolean addUserToClan(User u, String clanID) {
        if (getClanByOwner(clanID).isPresent()) {
            return getClanByOwner(clanID).get().addPlayer(u.getUniqueId());
        }
        return false;
    }

    public Boolean removeUserFromClan(User u, String clanID) {
        if (getClanByOwner(clanID).isPresent()) {
            BClanChat.getInstance().getLogger().info("clan owner clan is present");
            if (getClanByOwner(clanID).get().removePlayer(u.getUniqueId())) {
                BClanChat.getInstance().getLogger().info("all worked");
                return true;
            }
        }
        return false;
    }

    public void sendClanRequest(User sender, User receiver, Request req) {
        if (receiver.getPlayer().isPresent()) {
            Player rec = receiver.getPlayer().get();
            Player send = sender.getPlayer().get();

            rec.sendMessage(prefixSerializer("&bYou have received a Clan request from &d" + sender.getName() + "&b."));

            rec.sendMessage(Text.builder()

                    .append(Text.builder().append(textSerializer("&l&8[&r&aACCEPT&l&8]&r "))
                            .onClick(TextActions.executeCallback(commandSource -> {
                                if (!getPlayerClan(rec.getUniqueId()).isPresent()) {
                                    if (!BClanChat.getInstance().getRequests().get(BClanChat.getInstance().getRequests().indexOf(req)).getAcknowledged()) {
                                        if(addUserToClan(receiver, sender.getUniqueId())){
                                        BClanChat.getInstance().getRequests().get(BClanChat.getInstance().getRequests().indexOf(req)).setAcknowledged(true);
                                        rec.sendMessage(prefixSerializer("&bYou are now a member of "+send.getName()+"&b's clan"));
                                        send.sendMessage(prefixSerializer("&d"+send.getName()+"&b has accepted your request to join the clan."));
                                        }
                                    } else {rec.sendMessage(prefixSerializer("&bYou have already responded to this request"));}
                                } else {
                                    rec.sendMessage(prefixSerializer("&bYou are already a member of a clan!"));
                                }
                            }))
                            .build())

                    .append(Text.builder().append(textSerializer("&l&8[&r&cDENY&l&8]&r"))
                            .onClick(TextActions.executeCallback(commandSource -> {
                                if (!BClanChat.getInstance().getRequests().get(BClanChat.getInstance().getRequests().indexOf(req)).getAcknowledged()) {
                                    BClanChat.getInstance().getRequests().get(BClanChat.getInstance().getRequests().indexOf(req)).setAcknowledged(true);
                                    ((Player) sender).sendMessage(prefixSerializer("&bYour request to Clan with &d" + receiver.getName() + "&b has been denied."));
                                } else {receiver.getPlayer().get().sendMessage(prefixSerializer("&bYou have already responded to this request"));}
                            }))
                            .build())


                    .build());
        }
    }

    public Boolean removeUserFromClan(User u, UUID owner) {
        if (getClanByOwner(owner).isPresent()) {
            BClanChat.getInstance().getLogger().info("clan owner clan is present");
            return getClanByOwner(owner).get().removePlayer(u.getUniqueId());
        }
        return false;
    }

    public UUID getClanOwner(String clanID) {
        if (getClanByOwner(clanID).isPresent()) {
            return getClanByOwner(clanID).get().getOwnerUUID();
        }
        return UUID.randomUUID();
    }

    public Boolean getPlayerToggle(UUID uuid) {
        if (BClanChat.chatToggle.containsKey(uuid)) {
            return BClanChat.chatToggle.get(uuid);
        }
        return false;
    }

    public PaginationList paginationBuilder(List<Text> text, CommandSource src) {
        return PaginationList.builder()
                .padding(TextSerializers.FORMATTING_CODE.deserialize("&b="))
                .contents(text)
                .title(TextSerializers.FORMATTING_CODE.deserialize("&l&8[&r&bBClanChat&l&8]&r"))
                .sendTo(src);
    }

    public PaginationList allClanDetails(CommandSource src) {
        List<Text> listText = new ArrayList<>();
        Text separator = textSerializer("----------");
        Text.Builder builder = separator.toBuilder();
        for (Clan clan : BClanChat.clanList) {
            builder.append(textSerializer("\n&aID: &b" + clan.getClanID() + "\n"));
            builder.append(
                    textSerializer("\n&aOwner: &b" + getUser(clan.getOwnerUUID()).get().getName()))
                    .append(textSerializer("\n&aMembers: "));
            for (UUID member : clan.getPlayerList()) {
                builder.append(textSerializer("\n&l&6-&r&b" + getUser(member).get().getName()));
            }
            builder.append(separator);
        }
        listText.add(builder.build());
        return PaginationList.builder()
                .padding(TextSerializers.FORMATTING_CODE.deserialize("&b="))
                .contents(listText)
                .title(TextSerializers.FORMATTING_CODE.deserialize("&l&8[&r&bBClanChat&l&8]&r"))
                .sendTo(src);
    }

    public void showClanDetails(CommandSource src, String clanID) {
        List<Text> clanText = new ArrayList<>();
        if (getClanByOwner(clanID).isPresent()) {
            Clan clan = getClanByOwner(clanID).get();
            User owner = getUser(clan.getOwnerUUID()).get();
            List<User> members = new ArrayList<>();
            clanText.add(textSerializer("\n&aID: &b" + clan.getClanID() + "\n"));
            clanText.add(textSerializer("&bClan owner: &a" + owner.getName()));
            for (UUID uuid : clan.getPlayerList()) {
                members.add(getUser(uuid).get());
            }
            //Text.Builder memberText = TextSerializers.FORMATTING_CODE.deserialize("&bMembers:&r ").toBuilder();
            clanText.add(textSerializer("&bMembers:&r "));
            for (User user : members) {
                clanText.add(textSerializer("&l&6-&r&a" + user.getName()));
            }
            //clanText.add(memberText.build());
            paginationBuilder(clanText, src);
        }
    }

    public void showClanDetails(Player p) {
        List<Text> clanText = new ArrayList<>();
        if (getPlayerClan(p.getUniqueId()).isPresent()) {
            Clan clan = getPlayerClan(p.getUniqueId()).get();
            User owner = getUser(clan.getOwnerUUID()).get();
            List<User> members = new ArrayList<>();
            clanText.add(textSerializer("&bClan owner: &a" + owner.getName()));
            for (UUID uuid : clan.getPlayerList()) {
                members.add(getUser(uuid).get());
            }
            Text.Builder memberText = TextSerializers.FORMATTING_CODE.deserialize("&bMembers: &r").toBuilder();
            for (User user : members) {
                memberText.append(textSerializer("\n&l&6-&r&a" + user.getName()));
            }
            clanText.add(memberText.build());
            paginationBuilder(clanText, p);
        }
    }

    public void toggleClanChat(Player p) {
        if (BClanChat.chatToggle.containsKey(p.getUniqueId())) {
            Boolean choice = BClanChat.chatToggle.get(p.getUniqueId());
            Boolean invert = invertBoolean(choice);
            BClanChat.chatToggle.replace(p.getUniqueId(), invert);
            p.sendMessage(prefixSerializer("&bYou have toggle clan chat to: &l&6" + invert));
        } else {
            BClanChat.chatToggle.put(p.getUniqueId(), true);
            p.sendMessage(prefixSerializer("&bYou have toggle clan chat to: &l&6" + true));
        }
    }

    public Boolean addClanToList(Clan clan) throws IOException, ObjectMappingException {
        if (!BClanChat.clanList.contains(clan)) {
            instance.getLogger().info("Current list does not contain clan");
            if (!clanIDExists(clan.getClanID())) {
                instance.getLogger().info("clan ID does not exist");
                BClanChat.clanList.add(clan);
                MainPluginConfig conf = BClanChat.getConfigHandler().getPluginConf();
                conf.setClanList(BClanChat.clanList);
                BClanChat.getConfigHandler().saveConfig(conf);
                return true;
            }
            //saveClansConfig();
        }
        return false;
    }

    public Boolean isClanMember(UUID uuid) {
        for (Clan clan : BClanChat.clanList) {
            if (clan.getPlayerList().contains(uuid)) {
                return true;
            }
        }
        return false;
    }

    public Boolean clanIDExists(String clanID) {
        for (Clan clan : BClanChat.clanList) {
            if (clan.getClanID() == clanID) {
                return true;
            }
        }
        return false;
    }


    public void deleteClan(Clan clan) throws IOException, ObjectMappingException {
        BClanChat.clanList.remove(clan);
        MainPluginConfig conf = BClanChat.getMainPluginConfig();
        conf.getClanList().remove(clan);
        BClanChat.getConfigHandler().saveConfig(conf);
    }

    public Boolean deleteClan(String clanID) throws IOException, ObjectMappingException {
        List<Clan> tempList = new ArrayList<>(BClanChat.clanList);
        Iterator<Clan> iterator = tempList.iterator();
        while (iterator.hasNext()) {
            Clan clan = iterator.next();
            if (clan.getClanID().equals(clanID)) {
                BClanChat.clanList.remove(clan);
                return true;
            }
        }
        saveClansConfig();
        return false;
    }

    public Optional<Clan> getClanByOwner(UUID ownerUUID) {
        List<Clan> tempList = new ArrayList<>(BClanChat.clanList);
        Iterator<Clan> iterator = tempList.iterator();
        while (iterator.hasNext()) {
            Clan clan = iterator.next();
            if (clan.getOwnerUUID().equals(ownerUUID)) {
                return Optional.of(clan);
            }
        }
        return Optional.empty();
    }

    public Optional<Clan> getClanByOwner(String clanID) {
        List<Clan> tempList = new ArrayList<>(BClanChat.clanList);
        Iterator<Clan> iterator = tempList.iterator();
        while (iterator.hasNext()) {
            Clan clan = iterator.next();
            if (clan.getClanID().equals(clanID)) {
                return Optional.of(clan);
            }
        }
        return Optional.empty();
    }

    public Optional<Clan> getPlayerClan(UUID uuid) {
        for (Clan clan : BClanChat.clanList) {
            if (clan.getPlayerList().contains(uuid)) {
                return Optional.of(clan);
            }
        }
        return Optional.empty();
    }

    public void saveClansConfig() throws IOException, ObjectMappingException {
        MainPluginConfig conf = BClanChat.getConfigHandler().getPluginConf();
        List<Clan> tmpList = new ArrayList<>(BClanChat.clanList);
        conf.setClanList(tmpList);
        BClanChat.getConfigHandler().saveConfig(conf);
    }
}
