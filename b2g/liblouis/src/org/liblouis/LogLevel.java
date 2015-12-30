package org.liblouis;

public enum LogLevel {
  ALL  ('A'),
  DEBUG('D'),
  INFO ('I'),
  WARN ('W'),
  ERROR('E'),
  FATAL('F'),
  OFF  ('O'),
  ;

  private final char character;

  public final char getCharacter () {
    return character;
  }

  LogLevel (char character) {
    this.character = character;
  }
}
