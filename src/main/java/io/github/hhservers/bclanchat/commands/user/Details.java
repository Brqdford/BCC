package io.github.hhservers.bclanchat.commands.user;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class Details implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Util util = BClanChat.getInstance().util;

        if(src instanceof Player){
            Player p = (Player)src;
            if(args.<String>getOne(Text.of("clanID")).isPresent()) {
                if(p.hasPermission("bclanchat.user.clan.details.other")) {
                    String clanID = args.<String>getOne(Text.of("clanID")).get();
                    if(util.clanIDExists(clanID))
                        util.showClanDetails(p, clanID);
                }
            } else {
                if(util.getPlayerClan(p.getUniqueId()).isPresent())
                    util.showClanDetails(p);
                else
                    p.sendMessage(util.prefixSerializer("&bYou need to be in a clan before you can see clan details."));
            }
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .arguments(GenericArguments.optional(GenericArguments.string(Text.of("clanID"))))
                .permission("bclanchat.user.clan.details")
                .description(Text.of("Details of clan command"))
                .executor(new Details())
                .build();
    }
}
