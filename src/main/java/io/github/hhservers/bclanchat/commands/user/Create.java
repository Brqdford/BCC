package io.github.hhservers.bclanchat.commands.user;

import io.github.hhservers.bclanchat.util.Util;
import io.github.hhservers.bclanchat.util.objects.Clan;
import lombok.SneakyThrows;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class Create implements CommandExecutor {
    @SneakyThrows
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            if(args.<String>getOne(Text.of("clanID")).isPresent()) {
                Util util = new Util();
                String clanID = args.<String>getOne(Text.of("clanID")).get();
                Player p = (Player) src;
                if(!util.isClanMember(p.getUniqueId())) {
                    if(!util.clanIDExists(clanID)) {
                        Clan clan = new Clan();
                        clan.setClanID(clanID);
                        clan.setOwnerUUID(p.getUniqueId());
                        clan.addPlayer(p.getUniqueId());
                        util.addClanToList(clan);
                        p.sendMessage(util.prefixSerializer("&bYour clan &d"+clanID+" &bhas been created!"));
                    } else { p.sendMessage(util.prefixSerializer("&bThe &l&8{&r&a" + clanID +"&l&8}&r&b Clan ID already exists! Please pick a different Clan ID.")); }
                } else {
                    p.sendMessage(Text.of("You are already a member of a clan!"));
                }
                /*MainPluginConfig conf = BClanChat.getMainPluginConfig();
                conf.getClanList().add(clan);
                BClanChat.getConfigHandler().saveConfig(conf);*/
            }
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .permission("bclanchat.user.clan.create")
                .arguments(GenericArguments.string(Text.of("clanID")))
                .description(Text.of("Create Clan"))
                .executor(new Create())
                .build();
    }
}
