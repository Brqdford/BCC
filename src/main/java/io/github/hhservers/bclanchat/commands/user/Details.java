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
                String clanID = args.<String>getOne(Text.of("clanID")).get();
                util.showClanDetails(p, clanID);
            } else {util.showClanDetails(p);}
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
