package io.github.hhservers.bclanchat.commands.admin;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AdminHelp implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player p = (Player)src;
            Util util = BClanChat.getInstance().util;
            List<Text> txt = new ArrayList<>();
            String prefix = BClanChat.getMainPluginConfig().getAdminCommandPrefix();
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " details &a-&b Details of all clans currently registered"));
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " add &a-&b Add user to a clan (takes clanID)"));
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " remove &a-&b Remove user from a clan (takes clanID)"));
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " get &a-&b Get a player's current clan (takes username)"));
            txt.add(util.textSerializer("&l&6-&d/" + prefix + " delete &a-&b Delete a clan (takes clanID)"));
            util.paginationBuilder(txt, src);
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .permission("bclanchat.admin.clan.help")
                .description(Text.of("Clan help"))
                .executor(new AdminHelp())
                .build();
    }
}
