package io.github.hhservers.bclanchat.commands.admin;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class AdminGet implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Util util = BClanChat.getInstance().util;
        if(args.<User>getOne(Text.of("username")).isPresent()){
            User u = args.<User>getOne(Text.of("username")).get();
            if(util.getPlayerClan(u.getUniqueId()).isPresent())
                util.showClanDetails(src, util.getPlayerClan(u.getUniqueId()).get().getClanID());
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .arguments(GenericArguments.user(Text.of("username")))
                .permission("bclanchat.admin.clan")
                .description(Text.of("Admin base command"))
                .executor(new AdminGet())
                .build();
    }

}
