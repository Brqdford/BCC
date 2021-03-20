package io.github.hhservers.bclanchat.config;

import io.github.hhservers.bclanchat.util.objects.Clan;
import lombok.Data;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class MainPluginConfig {

    @Setting(value = "commandPrefix")
    private final String commandPrefix = "clan";

    @Setting(value = "adminCommandPrefix")
    private final String adminCommandPrefix = "bca";

    @Setting(value = "saveTimer")
    private int saveTimer = 10;

    @Setting(value = "clanList")
    private List<Clan> clanList = new ArrayList<>();

    public int getSaveTimer() {
        return saveTimer;
    }

    public List<Clan> getClanList() {
        return clanList;
    }

    public void setSaveTimer(int saveTimer) {
        this.saveTimer = saveTimer;
    }

    public void setClanList(List<Clan> clanList) {
        this.clanList = clanList;
    }

    public void deleteClanFromList(Clan clan){
        clanList.remove(clan);
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public String getAdminCommandPrefix() {
        return adminCommandPrefix;
    }
}
