package net.archasmiel.dndbot.listener;

import java.util.ArrayList;
import java.util.List;
import net.archasmiel.dndbot.command.basic.Command;
import net.archasmiel.dndbot.command.basic.HelpCommand;
import net.archasmiel.dndbot.command.manaops.CastCommand;
import net.archasmiel.dndbot.command.manaops.ChangeStatCommand;
import net.archasmiel.dndbot.command.manaops.NewDayCommand;
import net.archasmiel.dndbot.command.userops.AddUserCommand;
import net.archasmiel.dndbot.command.userops.GetManaUsersCommand;
import net.archasmiel.dndbot.command.userops.SetManaUserCommand;
import net.archasmiel.dndbot.database.ManaController;
import net.archasmiel.dndbot.database.objects.DiscordUser;
import net.archasmiel.dndbot.util.config.BotConfiguration;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

/**
 * Bot message listener.
 */
public class MessageListener extends ListenerAdapter {

  private final List<Command> commands = List.of(
      new HelpCommand(),
      new CastCommand(), new NewDayCommand(),
      new AddUserCommand(), new ChangeStatCommand(),
      new GetManaUsersCommand(), new SetManaUserCommand()
  );
  private final List<SlashCommandData> commandData = commands.stream()
      .map(Command::data).toList();
  private final List<String> guilds = BotConfiguration.guilds();

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    commands.forEach(e -> {
      if (e.name().equals(event.getCommandPath())) {
        SlashCommandInteraction sci = event.getInteraction();
        String discordUserId = sci.getUser().getId();

        ManaController.INSTANCE.discordUsers.computeIfAbsent(discordUserId,
            id -> new DiscordUser(id, new ArrayList<>(), null));
        ManaController.INSTANCE.saveDiscordUser(discordUserId);

        e.process(sci);
      }
    });
  }

  @Override
  public void onGuildReady(GuildReadyEvent event) {
    Guild guild = event.getGuild();
    if (guilds.contains(guild.getId())) {
      guild.updateCommands()
          .addCommands(commandData)
          .queue();
    }
  }

}
