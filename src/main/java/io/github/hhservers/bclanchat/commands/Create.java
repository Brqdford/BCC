package io.github.hhservers.bclanchat.commands;

import io.github.hhservers.bclanchat.BClanChat;
import io.github.hhservers.bclanchat.config.MainPluginConfig;
import io.github.hhservers.bclanchat.util.Util;
import io.github.hhservers.bclanchat.util.objects.Clan;
import lombok.SneakyThrows;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.List;

public class Create implements CommandExecutor {
    @SneakyThrows
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            if(args.<String>getOne(Text.of("clanID")).isPresent()) {
                String clanID = args.<String>getOne(Text.of("clanID")).get();
                Player p = (Player) src;

                Clan clan = new Clan();
                clan.setOwnerUUID(p.getUniqueId());
                clan.setClanID(clanID);
                MainPluginConfig conf = BClanChat.getMainPluginConfig();

                List<Clan> tmpList = conf.getClanList();
                tmpList.add(clan);
                conf.setClanList(tmpList);

                BClanChat.getConfigHandler().saveConfig(conf);
            }
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .permission("bclanchat.user.base.create")
                .arguments(GenericArguments.string(Text.of("clanID")))
                .description(Text.of("Create Clan"))
                .executor(new Create())
                .build();
    }
}
