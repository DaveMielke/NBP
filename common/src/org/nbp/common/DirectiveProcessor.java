package org.nbp.common;

import java.util.regex.Pattern;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class DirectiveProcessor extends InputProcessor {
  private final static String LOG_TAG = DirectiveProcessor.class.getName();

  private final static Pattern OPERANDS_PATTERN = Pattern.compile("\\s+");

  public interface DirectiveHandler {
    public abstract boolean handleDirective (String[] operands);
  }

  DirectiveHandler unknownDirectiveHandler = new DirectiveHandler() {
    @Override
    public boolean handleDirective (String[] operands) {
      Log.w(LOG_TAG, "unknown directive: " + operands[0]);
      return true;
    }
  };

  public final void setUnknownDirectiveHandler (DirectiveHandler handler) {
    unknownDirectiveHandler = handler;
  }

  private final Map<String, DirectiveHandler> directives = new HashMap<String, DirectiveHandler>();

  private static String normalizeName (String name) {
    return name.toLowerCase();
  }

  public final boolean addDirective (String name, DirectiveHandler handler, boolean force) {
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

  public final boolean addDirective (String name, DirectiveHandler handler) {
    return addDirective(name, handler, false);
  }

  @Override
  protected final boolean handleLine (CharSequence text, int number) {
    String[] operands = OPERANDS_PATTERN.split(text.toString());
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
    } else {
      operands = Arrays.copyOfRange(operands, index, operands.length);
      handler = unknownDirectiveHandler;
    }

    return handler.handleDirective(operands);
  }

  private final boolean processNestedInput (String[] operands) {
    int index = 0;

    if (index == operands.length) {
      Log.w(LOG_TAG, "asset name not specified");
      return true;
    }

    String assetName = operands[index++];

    if (index < operands.length) {
      Log.w(LOG_TAG, "too many operands");
    }

    return processInput(assetName);
  }

  private final void addDirectives () {
    addDirective("include", new DirectiveProcessor.DirectiveHandler() {
      @Override
      public boolean handleDirective (String[] operands) {
        return processNestedInput(operands);
      }
    });
  }

  public DirectiveProcessor () {
    addDirectives();
  }
}
