package org.nbp.editor.menu.spell;
import org.nbp.editor.*;

import android.text.style.SuggestionSpan;
import android.text.Editable;
import android.content.DialogInterface;

public class ShowSuggestions extends SpanAction {
  public ShowSuggestions (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction () {
    final EditArea editArea = getEditArea();
    final Editable text = editArea.getText();
    final SuggestionSpan span = getSpan(SuggestionSpan.class);

    if (span != null) {
      final String[] suggestions = span.getSuggestions();

      getEditor().showChooser(
        R.string.menu_spell_ShowSuggestions, suggestions,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int which) {
            String suggestion = suggestions[which];
            int start = text.getSpanStart(span);
            int end = text.getSpanEnd(span);
            text.replace(start, end, suggestion);
            editArea.setSelection(start);
          }
        }
      );
    } else {
      showMessage(R.string.message_no_suggestions);
    }
  }
}
