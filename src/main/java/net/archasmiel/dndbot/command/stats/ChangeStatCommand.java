package net.archasmiel.dndbot.command.stats;

import java.util.Optional;
import net.archasmiel.dndbot.command.basic.Command;
import net.archasmiel.dndbot.database.ManaController;
import net.archasmiel.dndbot.database.ManaUser;
import net.archasmiel.dndbot.exception.IllegalParameters;
import net.archasmiel.dndbot.exception.NoManaUserFound;
import net.archasmiel.dndbot.util.Classes;
import net.archasmiel.dndbot.util.ManaQuad;
import net.archasmiel.dndbot.util.OptionMapper;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * Command for stat value changing.
 */
public class ChangeStatCommand extends Command {

  /**
   * Constructor for command.
   * Used for MessageListener one-time creation and other purposes.
   */
  public ChangeStatCommand() {
    super(
        Commands.slash("35changestat", "Команда для зміни параметра").addOptions(
            new OptionData(OptionType.STRING, "name", "Назва характеристики", true)
              .addChoice("level", "level").addChoice("param", "param"),
            new OptionData(OptionType.INTEGER, "value", "Кількість очків/рівнів", true)
      )
    );
  }

  @Override
  public void process(SlashCommandInteraction interaction) {
    String id = interaction.getUser().getId();
    String msg;

    try {
      Optional<ManaUser> manaUser = ManaController.INSTANCE.get(id);
      manaUser.orElseThrow(NoManaUserFound::new);
      ManaUser user = manaUser.get();

      Optional<String> statName = OptionMapper.INSTANCE.mapToStr(interaction.getOption("name"));
      Optional<Integer> value = OptionMapper.INSTANCE.mapToInt(interaction.getOption("value"));
      if (statName.isEmpty() || value.isEmpty()) {
        throw new IllegalParameters();
      }

      statCapping(user, statName.get(), value.get());
      Optional<ManaQuad> manaQuad = Classes.valueOf(user.getClassName())
          .getMana(user.getLevel(), user.getParam());
      manaQuad.orElseThrow(IllegalParameters::new);
      user.setMana(manaQuad.get().getMaxMana());
      ManaController.INSTANCE.writeUsers();

      msg = String.format("<@%s>, операцію завершено, мана тепер %d + %d = %d",
          id, manaQuad.get().getSpellPoints(),
          manaQuad.get().getBonusSpellPoints(), manaQuad.get().getMaxMana());
    } catch (Exception e) {
      msg = e.getMessage();
    }

    interaction.reply(msg).queue();
  }

  private void statCapping(ManaUser user, String statName, int diff) throws IllegalParameters {
    if (statName.equals("level")) {
      user.setLevel(Math.min(Math.max(1, user.getLevel() + diff), 20));
      return;
    }
    if (statName.equals("param")) {
      user.setParam(Math.min(Math.max(12, user.getParam() + diff), 51));
      return;
    }
    throw new IllegalParameters();
  }

}
