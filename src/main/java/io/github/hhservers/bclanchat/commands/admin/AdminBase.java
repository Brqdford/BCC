package io.github.hhservers.bclanchat.commands.admin;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.commands.user.Add;
import io.github.hhservers.bclanchat.commands.user.Delete;
import io.github.hhservers.bclanchat.commands.user.Details;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class AdminBase implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(BClanChat.getInstance().util.prefixSerializer("&bCheck &d/" + BClanChat.getMainPluginConfig().getAdminCommandPrefix() + " help&b for more commands"));
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .child(AdminDetails.build(), "details")
                .child(AdminDelete.build(), "delete")
                .child(AdminAdd.build(), "add")
                .child(AdminRemove.build(), "remove")
                .child(AdminGet.build(), "get")
                .child(AdminHelp.build(), "help")
                .permission("bclanchat.admin.clan")
                .description(Text.of("Admin base command"))
                .executor(new AdminBase())
                .build();
    }

}
