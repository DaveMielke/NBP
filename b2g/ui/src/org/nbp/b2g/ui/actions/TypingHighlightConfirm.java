package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypingHighlightConfirm extends Action {
  private final void appendTerm (StringBuilder sb, int term) {
    if (sb.length() > 0) sb.append(' ');
    sb.append(getString(term));
  }

  private final void appendTerm (StringBuilder sb, int term, boolean isActive) {
    if (isActive) appendTerm(sb, term);
  }

  @Override
  public boolean performAction () {
    StringBuilder sb = new StringBuilder();

    appendTerm(sb,
      R.string.DescribeHighlighting_bold,
      ApplicationSettings.TYPING_BOLD
    );

    appendTerm(sb,
      R.string.DescribeHighlighting_italic,
      ApplicationSettings.TYPING_ITALIC
    );

    appendTerm(sb,
      R.string.DescribeHighlighting_strike,
      ApplicationSettings.TYPING_STRIKE
    );

    appendTerm(sb,
      R.string.DescribeHighlighting_underline,
      ApplicationSettings.TYPING_UNDERLINE
    );

    if (sb.length() == 0) appendTerm(sb, R.string.DescribeHighlighting_none);
    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public TypingHighlightConfirm (Endpoint endpoint) {
    super(endpoint, false);
  }
}
