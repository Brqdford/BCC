package io.github.hhservers.bclanchat.commands;

import io.github.hhservers.bclanchat.BClanChat;
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

public class Base implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of("/bclans create clanID"));
        src.sendMessage(Text.of("/bclans add Player"));
        src.sendMessage(Text.of("/bclans remove Player"));
        src.sendMessage(Text.of("/bclans delete clanID"));

        if(src instanceof Player){
            Player p = (Player)src;
            List<Player> currentlyChatting = new ArrayList<>();
            currentlyChatting = BClanChat.getInstance().getCurrentlyClanChatting();
            currentlyChatting.add(p);
            BClanChat.getInstance().setCurrentlyClanChatting(currentlyChatting);
            p.sendMessage(Text.of("You are now clan chatting"));
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .child(Create.build(), "child")
                //.arguments(GenericArguments.string(Text.of("StringArg")), GenericArguments.integer(Text.of("IntArg")))
                .permission("bclanchat.user.base")
                .description(Text.of("Base command"))
                .executor(new Base())
                .build();
    }
}
