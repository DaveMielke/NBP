package org.nbp.editor.spans;
import org.nbp.editor.*;

import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

import android.text.style.CharacterStyle;

public abstract class RevisionSpan extends ReviewSpan {
  protected RevisionSpan (String prefix, String suffix, CharacterStyle style) {
    super(prefix, suffix, style);
  }

  protected RevisionSpan (String prefix, String suffix) {
    super(prefix, suffix);
  }

  protected RevisionSpan (CharacterStyle style) {
    super(style);
  }

  public abstract int getRevisionType ();
  public abstract CharSequence getAcceptText ();
  public abstract CharSequence getRejectText ();
  public abstract CharSequence getRevertText ();

  @Override
  public void finishDialog (DialogHelper helper) {
    super.finishDialog(helper);
    helper.setText(R.id.revision_text, getActualText());
    helper.setText(R.id.revision_type, getRevisionType());
  }
}
