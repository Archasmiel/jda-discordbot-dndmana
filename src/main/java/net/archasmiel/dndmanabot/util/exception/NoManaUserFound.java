package net.archasmiel.dndmanabot.util.exception;

/**
 * Exception for no user in database.
 */
public class NoManaUserFound extends Exception {

  public NoManaUserFound() {
    super("Не найдено персонажа в системе");
  }

}
