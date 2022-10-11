package net.archasmiel.dndbot.listener;

import net.archasmiel.dndbot.command.factory.DndCommandFactory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (!event.getAuthor().isBot() && event.getGuild().getIdLong() == 917510566283718727L) {
      DndCommandFactory.produce(event).execute();
    }
  }

}