package io.github.hhservers.bclanchat.commands.admin;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.commands.user.Remove;
import io.github.hhservers.bclanchat.util.Util;
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

public class AdminAdd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(args.<User>getOne(Text.of("usernameToAdd")).isPresent()){
            if(args.<String>getOne(Text.of("clanID")).isPresent()) {
                Util util = BClanChat.getInstance().util;
                String clanID = args.<String>getOne(Text.of("clanID")).get();
                User u = args.<User>getOne(Text.of("usernameToAdd")).get();
                if(!util.getPlayerClan(u.getUniqueId()).isPresent()) {
                    if (util.addUserToClan(u, clanID))
                        src.sendMessage(util.prefixSerializer("&bUser &d" + u.getName() + "&b added to the Clan &d" + clanID));
                    else
                        src.sendMessage(util.prefixSerializer("&bError occurred when trying to add User to Clan"));
                } else {
                    src.sendMessage(util.prefixSerializer("&bUser is already part of another Clan."));
                }
            }
        }

        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .arguments(GenericArguments.user(Text.of("usernameToAdd")), GenericArguments.string(Text.of("clanID")))
                .permission("bclanchat.admin.clan.add")
                .description(Text.of("Add player to Clan"))
                .executor(new AdminAdd())
                .build();
    }
}
