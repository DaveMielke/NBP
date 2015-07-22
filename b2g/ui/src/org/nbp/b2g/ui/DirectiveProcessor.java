package org.nbp.b2g.ui;

import java.util.regex.Pattern;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class DirectiveProcessor extends InputProcessor {
  private final static String LOG_TAG = DirectiveProcessor.class.getName();

  private final Pattern OPERANDS_PATTERN = Pattern.compile("\\s+");

  public interface DirectiveHandler {
    public abstract boolean handleDirective (String[] operands);
  }

  private final Map<String, DirectiveHandler> directives = new HashMap<String, DirectiveHandler>();

  private static String normalizeName (String name) {
    return name.toLowerCase();
  }

  public boolean addDirective (String name, DirectiveHandler handler, boolean force) {
    name = normalizeName(name);

    if (!force) {
      if (directives.get(name) != null) {
        Log.w(LOG_TAG, "directive already defined: " + name);
        return false;
      }
    }

    directives.put(name, handler);
    return true;
  }

  public boolean addDirective (String name, DirectiveHandler handler) {
    return addDirective(name, handler, false);
  }

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
    String directive = operands[index];
    if (directive.charAt(0) == '#') return true;

    directive = normalizeName(directive);
    DirectiveHandler handler = directives.get(directive);

    if (handler != null) {
      operands = Arrays.copyOfRange(operands, index+1, operands.length);
      return handler.handleDirective(operands);
    }

    Log.w(LOG_TAG, "unknown directive: " + directive);
    return true;
  }

  public DirectiveProcessor () {
  }
}
