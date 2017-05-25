package org.nbp.editor.menu.edit;
import org.nbp.editor.*;

import android.view.MenuItem;

import org.nbp.common.CommonUtilities;
import org.nbp.common.SpeechToText;

import android.content.DialogInterface;
import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

import android.widget.EditText;

public class Record extends EditorAction {
  public Record () {
    super();
  }

  private final void verifyRecording (
    final EditorActivity editor, final String text
  ) {
    if (!text.isEmpty()) {
      DialogFinisher finisher = new DialogFinisher() {
        @Override
        public void finishDialog (DialogHelper helper) {
          helper.setText(R.id.record_text, text);
        }
      };

      DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          EditText view = (EditText)CommonUtilities.findView(dialog, R.id.record_text);
          editor.getEditArea().replaceSelection(view.getText());
        }
      };

      editor.showDialog(
        R.string.menu_edit_Record, R.layout.record_verify,
        finisher, R.string.action_accept, listener
      );
    }
  }

  private final void verifyRecording (
    final EditorActivity editor, final String[] choices
  ) {
    int count = choices.length;

    if (count != 0) {
      if (count == 1) {
        verifyRecording(editor, choices[0]);
      } else {
        editor.showChooser(
          R.string.menu_edit_Record, choices,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int item) {
              verifyRecording(editor, choices[item]);
            }
          }
        );
      }
    }
  }

  @Override
  public void performAction (final EditorActivity editor, MenuItem item) {
    if (editor.getEditArea().containsProtectedText()) {
      editor.showMessage(R.string.message_protected_text);
    } else {
      SpeechToText.TextHandler handler = new SpeechToText.TextHandler() {
        @Override
        public void handleText (String[] choices) {
          verifyRecording(editor, choices);
        }
      };

      new SpeechToText.Builder(editor)
                      .start(handler);
    }
  }
}
