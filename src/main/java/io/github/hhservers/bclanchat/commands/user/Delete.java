package io.github.hhservers.bclanchat.commands.user;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.util.Util;
import lombok.SneakyThrows;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class Delete implements CommandExecutor {
    @SneakyThrows
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            if(src instanceof Player) {
                Player p = (Player)src;
                Util util = BClanChat.getInstance().util;
                if (util.getClanByOwner(p.getUniqueId()).isPresent()) {
                    util.deleteClan(util.getClanByOwner(p.getUniqueId()).get());
                    p.sendMessage(util.prefixSerializer("&bClan deleted!"));
                } else { p.sendMessage(util.prefixSerializer("&bYou must first own a clan before you delete it!"));}
            }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .permission("bclanchat.user.clan.delete")
                .description(Text.of("Delete Clan"))
                .executor(new Delete())
                .build();
    }
}
