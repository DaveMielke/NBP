package org.nbp.editor;

import android.content.Context;
import android.widget.EditText;
import android.util.AttributeSet;

import android.text.Spanned;

public class EditArea extends EditText {
  public EditArea (Context context, AttributeSet attributes) {
    super(context, attributes);
  }

  private ContentHandle contentHandle = null;
  private boolean hasChanged = false;
  private boolean enforceTextProtection = true;

  public final ContentHandle getContentHandle () {
    return contentHandle;
  }

  public final void setContentHandle (ContentHandle handle) {
    contentHandle = handle;
  }

  public final boolean getHasChanged () {
    return hasChanged;
  }

  public final void setHasChanged (boolean yes) {
    hasChanged = yes;
  }

  public final void setHasChanged () {
    setHasChanged(true);
  }

  public final boolean getEnforceTextProtection () {
    return enforceTextProtection;
  }

  public final void setEnforceTextProtection (boolean yes) {
    enforceTextProtection = yes;
  }

  public final boolean containsProtectedText (Spanned text, int start, int end) {
    if (!enforceTextProtection) return false;

    EditorSpan[] spans = text.getSpans(start, end, EditorSpan.class);
    if (spans == null) return false;

    for (EditorSpan span : spans) {
      if (!span.getContainsProtectedText()) continue;
      if (text.getSpanStart(span) >= end) continue;
      if (text.getSpanEnd(span) <= start) continue;
      return true;
    }

    return false;
  }

  public final boolean containsProtectedText (int start, int end) {
    return containsProtectedText(getText(), start, end);
  }

  public final boolean containsProtectedText () {
    return containsProtectedText(getSelectionStart(), getSelectionEnd());
  }

  public final void replaceSelection (CharSequence text) {
    getText().replace(getSelectionStart(), getSelectionEnd(), text);
  }

  @Override
  public void onSelectionChanged (int start, int end) {
    super.onSelectionChanged(start, end);
  }

  @Override
  public void onTextChanged (CharSequence text, int start, int before, int after) {
    super.onTextChanged(text, start, before, after);
  }

  public final boolean hasSelection () {
    return getSelectionStart() != getSelectionEnd();
  }

  public final void setSelection (EditorSpan span) {
    setSelection(span.getPosition(getText()));
  }
}
