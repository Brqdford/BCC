package io.github.hhservers.bclanchat.config;

import io.github.hhservers.bclanchat.util.objects.Clan;
import lombok.Data;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable @Data
public class MainPluginConfig {
    @Setting(value = "saveTimer")
    private int saveTimer = 10;

    @Setting(value = "Clans")
    private MainPluginConfig.ClanList clans = new ClanList();

    @ConfigSerializable @Data
    public static class ClanList {
        @Setting(value = "clanList")
        private List<Clan> clanList;
    }
}
