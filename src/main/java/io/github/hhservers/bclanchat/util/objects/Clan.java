package io.github.hhservers.bclanchat.util.objects;

import lombok.Data;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConfigSerializable
public class Clan {

    @Setting("playerList")
    private List<UUID> playerList = new ArrayList<>();

    @Setting("ownerUUID")
    private UUID ownerUUID = UUID.randomUUID();

    @Setting("clanID")
    private String clanID = "placeholder";

    public List<UUID> getPlayerList() {
        return playerList;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public String getClanID() {
        return clanID;
    }

    public void setPlayerList(List<UUID> playerList) {
        this.playerList = playerList;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public void setClanID(String clanID) {
        this.clanID = clanID;
    }

    public Boolean addPlayer(UUID uuid){
        if(playerList.contains(uuid)){
            return false;
        }
        playerList.add(uuid);
        return true;
    }

    public Boolean removePlayer(UUID uuid){
        if(!playerList.contains(uuid)){
            return false;
        }
        playerList.remove(uuid);
        return true;
    }

}
