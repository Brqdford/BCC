package io.github.hhservers.bclanchat.commands.user;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.config.MainPluginConfig;
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

import java.util.ArrayList;
import java.util.List;

public class Help implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player p = (Player)src;
            Util util = BClanChat.getInstance().util;
            List<Text> txt = new ArrayList<>();
            String prefix = BClanChat.getMainPluginConfig().getCommandPrefix();
            txt.add(util.textSerializer("&3/" + prefix + " &7-&b Toggle Group Chat"));
            txt.add(util.textSerializer("&3/" + prefix + " create &7-&b Create a group"));
            txt.add(util.textSerializer("&3/" + prefix + " details &7-&b Current group details"));
            txt.add(util.textSerializer("&3/" + prefix + " add &7-&b Add user to a group you own"));
            txt.add(util.textSerializer("&3/" + prefix + " remove &7-&b Remove user from a group you own"));
            txt.add(util.textSerializer("&3/" + prefix + " leave &7-&b Leave your current group"));
            txt.add(util.textSerializer("&3/" + prefix + " delete &7-&b Delete a group you own"));
            util.paginationBuilder(txt, src);
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .permission("bclanchat.user.clan.help")
                .description(Text.of("Group help"))
                .executor(new Help())
                .build();
    }
}
