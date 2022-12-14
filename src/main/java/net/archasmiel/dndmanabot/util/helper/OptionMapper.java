package net.archasmiel.dndmanabot.util.helper;

import java.util.Optional;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

/**
 * Command options' mapper.
 */
public class OptionMapper {

  private OptionMapper() {

  }

  /**
   * Get optional of Integer from OptionMapping.
   *
   * @param mapping int mapping
   * @return optional of Integer
   */
  public static Optional<Integer> mapToInt(OptionMapping mapping) {
    if (mapping == null || mapping.getType() != OptionType.INTEGER) {
      return Optional.empty();
    }
    return Optional.of(mapping.getAsInt());
  }

  /**
   * Get optional of String from OptionMapping.
   *
   * @param mapping int mapping
   * @return optional of String
   */
  public static Optional<String> mapToStr(OptionMapping mapping) {
    if (mapping == null || mapping.getType() != OptionType.STRING) {
      return Optional.empty();
    }
    return Optional.of(mapping.getAsString());
  }

}
