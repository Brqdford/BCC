package io.github.hhservers.bclanchat.util;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.config.ConfigHandler;
import io.github.hhservers.bclanchat.config.MainPluginConfig;
import io.github.hhservers.bclanchat.util.objects.Clan;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Util {

    private BClanChat instance = BClanChat.getInstance();

    /*private Boolean addToClan(String clanID, UUID uuid){
        if(isClanMember(uuid)){
            return false;
        } else {
            for(Clan clan: instance.getClanList()){
                if(clan.getClanID()==clanID){
                    clan.getPlayerList().add(uuid);
                    return true;
                }
            }
        }
        return false;
    }*/

    public Boolean checkConflictingClandID(String clanID){
        List<Clan> currentList = BClanChat.clanList;
        Boolean result = false;
        for(Clan clan : currentList){
            if(clan.getClanID()==clanID){
                result = true;
            }
        }
        return result;
    }

    public Boolean addClanToList(Clan clan){
        List<Clan> currenList = BClanChat.clanList;
        if(!currenList.contains(clan)) {
                if(!checkConflictingClandID(clan.getClanID())){
                    currenList.add(clan);
                    return true;
                }
                BClanChat.clanList = currenList;
        }
        return false;
    }

    public Boolean isClanMember(UUID uuid){
        for(Clan clan : BClanChat.clanList){
            if(clan.getPlayerList().contains(uuid)){
                return true;
            }
        }
        return false;
    }

    public void saveClansTask(){
        instance.getPluginTasks().add(Task.builder()
                .interval(BClanChat.getMainPluginConfig().getSaveTimer(), TimeUnit.MINUTES)
                .delay(BClanChat.getMainPluginConfig().getSaveTimer(), TimeUnit.MINUTES)
                .execute(() -> {
                    try {
                        saveClansConfig();
                    } catch (IOException|ObjectMappingException e) {
                        e.printStackTrace();
                    }
                })
                .submit(instance));
    }

    public void saveClansConfig() throws IOException, ObjectMappingException {
        //BClanChat.getInstance().getLogger().info(BClanChat.clanList.get(0).getClanID());
        MainPluginConfig conf = BClanChat.getConfigHandler().getPluginConf();
        conf.getClans().setClanList(BClanChat.clanList);
        BClanChat.getConfigHandler().saveConfig(conf);
    }
}
