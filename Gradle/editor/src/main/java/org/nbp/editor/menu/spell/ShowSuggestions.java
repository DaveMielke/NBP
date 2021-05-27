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
    final SuggestionSpan span = getSpan(SuggestionSpan.class);

    if (span != null) {
      final String[] suggestions = span.getSuggestions();
      final EditorActivity editor = getEditor();

      editor.showChooser(
        R.string.menu_spell_ShowSuggestions, suggestions,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int which) {
            String suggestion = suggestions[which];

            EditArea editArea = editor.getEditArea();
            Editable text = editArea.getText();

            int start = text.getSpanStart(span);
            int end = text.getSpanEnd(span);

            if (verifyWritableRegion(text, start, end)) {
              text.replace(start, end, suggestion);
              editArea.setSelection(start);
            }
          }
        }
      );
    } else {
      showMessage(R.string.message_no_suggestions);
    }
  }
}
