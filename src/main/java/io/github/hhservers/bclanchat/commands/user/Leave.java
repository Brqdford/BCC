package io.github.hhservers.bclanchat.commands.user;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.util.Util;
import io.github.hhservers.bclanchat.util.objects.Clan;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class Leave implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Util util = BClanChat.getInstance().util;

        if(src instanceof Player){
            Player p = (Player)src;
            if(util.getPlayerClan(p.getUniqueId()).isPresent()){
                Clan clan = util.getPlayerClan(p.getUniqueId()).get();
            }
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .permission("bclanchat.user.clan.leave")
                .description(Text.of("Leave clan command"))
                .executor(new Leave())
                .build();
    }
}
