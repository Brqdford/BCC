package io.github.hhservers.bclanchat.commands.admin;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.commands.user.Remove;
import lombok.SneakyThrows;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class AdminDelete implements CommandExecutor {
    @SneakyThrows
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(args.<String>getOne(Text.of("clanID")).isPresent()){
            if(BClanChat.getInstance().util.deleteClan(args.<String>getOne(Text.of("clanID")).get()))
                src.sendMessage(BClanChat.getInstance().util.prefixSerializer("&bClan deleted."));
            else
                src.sendMessage(BClanChat.getInstance().util.prefixSerializer("&bError removing Clan"));
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .arguments(GenericArguments.string(Text.of("clanID")))
                .permission("bclanchat.admin.clan.delete")
                .description(Text.of("Delete Clan"))
                .executor(new AdminDelete())
                .build();
    }
}
