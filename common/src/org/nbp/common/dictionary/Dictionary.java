package org.nbp.common.dictionary;

public abstract class Dictionary {
  private Dictionary () {
  }

  public static void end () {
    new QuitCommand();
  }
}
