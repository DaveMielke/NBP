package org.nbp.editor;

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

  public CharSequence getAcceptText () {
    return getActualText();
  }

  public CharSequence getRejectText () {
    return getActualText();
  }

  @Override
  public void finishDialog (DialogHelper helper) {
    super.finishDialog(helper);
    helper.setText(R.id.revision_text, getActualText());
    helper.setText(R.id.revision_type, getRevisionType());
  }
}
