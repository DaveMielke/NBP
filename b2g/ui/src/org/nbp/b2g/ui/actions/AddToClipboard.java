package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.text.SpannableStringBuilder;

public class AddToClipboard extends CopyToClipboard {
  @Override
  protected CharSequence toActualText (CharSequence text) {
    CharSequence original = Clipboard.getText();
    if (original == null) return text;
    if (original.length() == 0) return text;

    SpannableStringBuilder sb = new SpannableStringBuilder();
    sb.append(original);
    sb.append(text);
    return sb.subSequence(0, sb.length());
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.AddToClipboard_action_confirmation;
  }

  public AddToClipboard (Endpoint endpoint) {
    super(endpoint);
  }
}
