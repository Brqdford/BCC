package io.github.hhservers.bclanchat.commands.user;

import io.github.hhservers.bclanchat.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class Base implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player p = (Player)src;
            new Util().toggleClanChat(p);
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .child(Create.build(), "create")
                .child(Delete.build(), "delete")
                .child(Details.build(), "details")
                .child(Add.build(), "add")
                .child(Remove.build(), "remove")
                .child(Leave.build(), "leave")
                .child(Help.build(), "help")
                .permission("bclanchat.user.clan")
                .description(Text.of("Base command"))
                .executor(new Base())
                .build();
    }

}
