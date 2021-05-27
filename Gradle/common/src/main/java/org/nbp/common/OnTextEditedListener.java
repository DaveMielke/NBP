package org.nbp.common;

import android.text.TextWatcher;
import android.text.Editable;
import android.widget.EditText;

public abstract class OnTextEditedListener {
  protected abstract void onTextEdited (boolean isDifferent);

  private final String originalText;

  private final TextWatcher textWatcher = new TextWatcher() {
    @Override
    public final void beforeTextChanged (CharSequence oldText, int start, int before, int after) {
    }

    @Override
    public final void onTextChanged (CharSequence newText, int start, int before, int after) {
      onTextEdited(!originalText.equals(newText.toString()));
    }

    @Override
    public final void afterTextChanged (Editable e) {
    }
  };

  public OnTextEditedListener (EditText editText) {
    originalText = editText.getText().toString();

    editText.addTextChangedListener(textWatcher);
  }
}
