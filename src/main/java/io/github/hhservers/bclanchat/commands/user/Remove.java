package io.github.hhservers.bclanchat.commands.user;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.util.Util;
import io.github.hhservers.bclanchat.util.objects.Request;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class Remove implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player p = (Player)src;
            Util util = BClanChat.getInstance().util;
            if(util.getClanByOwner(p.getUniqueId()).isPresent()) {
                if (args.<User>getOne(Text.of("usernameToRemove")).isPresent()) {
                    User u = args.<User>getOne(Text.of("usernameToRemove")).get();
                    if(util.removeUserFromClan(u, p.getUniqueId())){ p.sendMessage(util.prefixSerializer("&bUser was removed from the Clan"));}
                    else {p.sendMessage(Text.of("false wat da fuck"));}
                }
            }
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .arguments(GenericArguments.user(Text.of("usernameToRemove")))
                .permission("bclanchat.user.clan.remove")
                .description(Text.of("Remove player from Clan"))
                .executor(new Remove())
                .build();
    }
}
