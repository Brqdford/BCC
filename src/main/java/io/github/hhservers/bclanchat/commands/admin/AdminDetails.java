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
import org.spongepowered.api.text.Text;

public class AdminDetails implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Util util = BClanChat.getInstance().util;
        if (args.<String>getOne(Text.of("clanID")).isPresent()) {
            util.showClanDetails(src, args.<String>getOne(Text.of("clanID")).get());
        } else
            util.allClanDetails(src);
        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                //.arguments(GenericArguments.optional(GenericArguments.choices(Text.of("clanID"), new Util().genChoices())))
                .arguments(GenericArguments.choices(Text.of("clanID"), new Util.ClanSupplier(), new Util().getClanIDFunction()))
                .permission("bclanchat.admin.clan.details")
                .description(Text.of("Details from clan"))
                .executor(new AdminDetails())
                .build();
    }
}
