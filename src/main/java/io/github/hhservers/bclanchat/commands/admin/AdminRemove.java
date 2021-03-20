package io.github.hhservers.bclanchat.commands.admin;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.commands.user.Remove;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class AdminRemove implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        BClanChat.getInstance().getLogger().info("wtf");
        if(args.<User>getOne(Text.of("usernameToRemove")).isPresent()){
            if(args.<String>getOne(Text.of("clanID")).isPresent()) {
                String clanID = args.<String>getOne(Text.of("clanID")).get();
                User u = args.<User>getOne(Text.of("usernameToRemove")).get();
                if(BClanChat.getInstance().util.removeUserFromClan(u,clanID))
                    src.sendMessage(BClanChat.getInstance().util.prefixSerializer("&bUser &d" + u.getName() + "&b removed from the Clan &d"+clanID));
                else
                    src.sendMessage(BClanChat.getInstance().util.prefixSerializer("&bError occurred when trying to remove User from Clan"));
            }
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .arguments(GenericArguments.user(Text.of("usernameToRemove")), GenericArguments.string(Text.of("clanID")))
                .permission("bclanchat.admin.clan.remove")
                .description(Text.of("Remove player from Clan"))
                .executor(new AdminRemove())
                .build();
    }
}
