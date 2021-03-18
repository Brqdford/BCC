package io.github.hhservers.bclanchat.util.objects;

import lombok.Data;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConfigSerializable
@Data
public class Clan {

    private List<UUID> playerList = new ArrayList<>();

    private UUID ownerUUID = UUID.randomUUID();

    private String clanID = "placeholder";

    public Boolean addPlayer(UUID uuid){
        if(playerList.contains(uuid)){
            return false;
        }
        playerList.add(uuid);
        return true;
    }

}
