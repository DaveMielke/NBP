package org.nbp.common.dictionary;

public abstract class Dictionary {
  private Dictionary () {
  }

  public static void endSession () {
    new QuitCommand();
  }
}
