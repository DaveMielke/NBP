package org.nbp.common.dictionary;

import java.util.List;
import java.util.ArrayList;

public abstract class DictionaryOperands {
  private DictionaryOperands () {
  }

  private final static Character DOUBLE_QUOTE = '"';
  private final static Character SINGLE_QUOTE = '\'';

  public static String quoteString (String string) {
    StringBuilder operand = new StringBuilder();
    int length = string.length();

    Character currentQuote = null;
    boolean needQuote = false;
    boolean haveQuote = false;

    if (length == 0) {
      needQuote = true;
    } else {
      for (int index=0; index<length; index+=1) {
        char character = string.charAt(index);

        Character newQuote =
          (character == SINGLE_QUOTE)? DOUBLE_QUOTE:
          (character == DOUBLE_QUOTE)? SINGLE_QUOTE:
          null;

        if (newQuote != null) {
          if (currentQuote != null) {
            if (character == currentQuote) {
              if (!haveQuote) {
                operand.insert(0, currentQuote);
                haveQuote = true;
              }

              operand.append(currentQuote);
              operand.append(newQuote);
            }
          }

          currentQuote = newQuote;
          needQuote = true;
        } else if (!needQuote) {
          if (Character.isWhitespace(character)) needQuote = true;
        }

        operand.append(character);
      }
    }

    if (needQuote && !haveQuote) {
      if (currentQuote == null) currentQuote = DOUBLE_QUOTE;
      operand.insert(0, currentQuote);
    }

    if (currentQuote != null) operand.append(currentQuote);
    return operand.toString();
  }

  public static String[] splitString (String string) {
    List<String> operands = new ArrayList<String>();
    StringBuilder operand = new StringBuilder();
    Character quote = null;
    boolean escape = false;
    int length = string.length();

    for (int index=0; index<length; index+=1) {
      char character = string.charAt(index);

      if (escape) {
        switch (character) {
        }

        operand.append(character);
        escape = false;
        continue;
      }

      if (character == '\\') {
        escape = true;
        continue;
      }

      if (quote == null) {
        if (character == DOUBLE_QUOTE) {
          quote = DOUBLE_QUOTE;
          continue;
        } 

        if (character == SINGLE_QUOTE) {
          quote = SINGLE_QUOTE;
          continue;
        }

        if (Character.isWhitespace(character)) {
          if (operand.length() > 0) {
            operands.add(operand.toString());
            operand.setLength(0);
          }

          continue;
        }
      } else if (character == quote) {
        quote = null;
        continue;
      }

      operand.append(character);
    }

    if (operand.length() > 0) operands.add(operand.toString());
    return operands.toArray(new String[operands.size()]);
  }
}
