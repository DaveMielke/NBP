package org.nbp.b2g.ui;

public abstract class DescriptionAction extends Action {
  protected abstract void makeDescription (StringBuilder sb);

  protected final static String toWords (String string) {
    return string.trim()
                 .replace('_', ' ')
                 .replaceAll("(\\S)([[:upper:]])", "$1 $2")
                 .toLowerCase();
  }

  protected final static String toWords (CharSequence text) {
    return toWords(text.toString());
  }

  protected final static void appendString (StringBuilder sb, String string) {
    sb.append(string);
  }

  protected final static void appendString (StringBuilder sb, int string) {
    appendString(sb, getString(string));
  }

  protected final static void startLine (StringBuilder sb, int label) {
    if (sb.length() > 0) sb.append('\n');
    appendString(sb, label);
    sb.append(":");
  }

  @Override
  public final boolean performAction () {
    StringBuilder sb = new StringBuilder();
    makeDescription(sb);
    if (sb.length() == 0) return false;
    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  protected DescriptionAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}
