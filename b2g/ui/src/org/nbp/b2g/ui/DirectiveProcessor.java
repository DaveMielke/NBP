package org.nbp.b2g.ui;

import java.util.regex.Pattern;

public abstract class DirectiveProcessor extends InputProcessor {
  private final Pattern OPERANDS_PATTERN = Pattern.compile("\\s+");

  @Override
  protected boolean processLine (String text, int number) {
    String[] operands = OPERANDS_PATTERN.split(text);
    int index = 0;

    if (index < operands.length) {
      if (operands[index].isEmpty()) {
        index += 1;
      }
    }

    if (index == operands.length) return true;
    String directive = operands[index++];
    if (directive.charAt(0) == '#') return true;

    return true;
  }

  public DirectiveProcessor () {
  }
}
