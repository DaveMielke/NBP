package org.nbp.common;

import java.util.regex.Pattern;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class DirectiveProcessor extends InputProcessor {
  private final static String LOG_TAG = DirectiveProcessor.class.getName();

  private boolean skipCommentLines = false;
  private boolean trimTrailingComments = false;

  public final DirectiveProcessor setSkipCommentLines (boolean yes) {
    skipCommentLines = yes;
    return this;
  }

  public final DirectiveProcessor setTrimTrailingComments (boolean yes) {
    trimTrailingComments = yes;
    return this;
  }

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

  public final DirectiveProcessor setUnknownDirectiveHandler (DirectiveHandler handler) {
    unknownDirectiveHandler = handler;
    return this;
  }

  private final Map<String, DirectiveHandler> directives = new HashMap<String, DirectiveHandler>();

  private static String normalizeName (String name) {
    return name.toLowerCase();
  }

  public final DirectiveProcessor addDirective (String name, DirectiveHandler handler) {
    name = normalizeName(name);

    if (directives.get(name) == null) {
      directives.put(name, handler);
    } else {
      Log.w(LOG_TAG, ("directive already defined: " + name));
    }

    return this;
  }

  public final static char COMMENT_CHARACTER = '#';
  private final static String COMMENT_OPERAND = Character.toString(COMMENT_CHARACTER);

  @Override
  protected final boolean handleLine (CharSequence text, int number) {
    String[] operands = OPERANDS_PATTERN.split(text.toString());
    int from = 0;
    int to = operands.length;

    if (from < to) {
      if (operands[from].isEmpty()) {
        from += 1;
      }
    }

    if (trimTrailingComments) {
      for (int index=from; index<to; index+=1) {
        if (operands[index].equals(COMMENT_OPERAND)) {
          to = index;
          break;
        }
      }
    }

    if (from == to) return true;
    String directive = operands[from];

    if (skipCommentLines) {
      if (directive.charAt(0) == COMMENT_CHARACTER) {
        return true;
      }
    }

    directive = normalizeName(directive);
    DirectiveHandler handler = directives.get(directive);

    if (handler != null) {
      from += 1;
    } else {
      handler = unknownDirectiveHandler;
    }

    operands = Arrays.copyOfRange(operands, from, to);
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
    addDirective("include",
      new DirectiveHandler() {
        @Override
        public boolean handleDirective (String[] operands) {
          return processNestedInput(operands);
        }
      }
    );
  }

  public DirectiveProcessor () {
    addDirectives();
  }
}
