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
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " create &a-&b Create a clan"));
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " details &a-&b Current clan details"));
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " add &a-&b Add user to a clan you own"));
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " remove &a-&b Remove user from a clan you own"));
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " leave &a-&b Leave your current clan"));
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " delete &a-&b Delete a clan you own"));
            util.paginationBuilder(txt, src);
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .permission("bclanchat.user.clan.help")
                .description(Text.of("Clan help"))
                .executor(new Help())
                .build();
    }
}
