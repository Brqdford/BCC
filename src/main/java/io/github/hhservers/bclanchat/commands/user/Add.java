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

import java.util.UUID;

public class Add implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Util util = BClanChat.getInstance().util;
        if(src instanceof Player) {
            Player sender = (Player)src;
            if(util.getClanByOwner(sender.getUniqueId()).isPresent()) {
                if (args.<User>getOne(Text.of("usernameToAdd")).isPresent()) {
                    User u = args.<User>getOne(Text.of("usernameToAdd")).get();
                /*if (!util.getPlayerClan(u.getUniqueId()).isPresent()) {
                    if (util.addUserToClan(u, ((Player) src).getUniqueId()))
                        src.sendMessage(util.prefixSerializer("&bUser was added to the Clan"));
                } else src.sendMessage(util.prefixSerializer("&bUser is already part of another Clan."));*/
                    if (u.isOnline()) {
                        Request req = new Request(UUID.randomUUID(), sender.getUniqueId(), u.getUniqueId(), false);
                        BClanChat.getInstance().getRequests().add(req);
                        util.sendClanRequest((User) src, u, req);
                        src.sendMessage(util.prefixSerializer("&bYou have sent a group request to &3" + u.getName() + "&b."));
                    } else
                        src.sendMessage(util.prefixSerializer("&bUser must be online before sending a group request."));

                }
            } else sender.sendMessage(util.prefixSerializer("&bYou must be the owner of a group to send requests!"));
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .permission("bclanchat.user.clan.add")
                .arguments(GenericArguments.user(Text.of("usernameToAdd")))
                .description(Text.of("Add player to group"))
                .executor(new Add())
                .build();
    }
}
