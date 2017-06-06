package org.nbp.editor;

import android.text.InputFilter;
import android.text.Spanned;

import android.view.Menu;
import android.view.MenuItem;

import org.nbp.common.Tones;

public class EditAreaFilter extends EditorComponent implements InputFilter {
  public EditAreaFilter (EditorActivity editor) {
    super(editor);
  }

  private final Runnable getMenuAction (char character) {
    if (character < 0X20) {
      final Menu menu = MenuAction.getCurrentMenu();

      if (menu != null) {
        int count = menu.size();

        char letter = character;
        letter |= 0X60;

        for (int index=0; index<count; index+=1) {
          final MenuItem item = menu.getItem(index);

          if (letter == item.getAlphabeticShortcut()) {
            return new Runnable() {
              @Override
              public void run () {
                EditArea editArea = getEditArea();
                editArea.beginBatchEdit();
                boolean performed = menu.performIdentifierAction(item.getItemId(), 0);
                editArea.endBatchEdit();
                if (!performed)  Tones.beep();
              }
            };
          }
        }
      }
    }

    return null;
  }

  private final boolean handleCharacter (char character) {
    if (!Character.isISOControl(character)) return false;
    if (character == '\n') return false;
    if (character == '\t') return false;

    Tones.beep();
    return true;
  }

  @Override
  public CharSequence filter (
    CharSequence src, int srcStart, int srcEnd,
    Spanned dst, int dstStart, int dstEnd
  ) {
    boolean handled = false;
    Runnable menuAction = null;

    if ((srcStart + 1) == srcEnd) {
      char character = src.charAt(srcStart);
      menuAction = getMenuAction(character);

      if (menuAction != null) {
        handled = true;
      } else if (handleCharacter(character)) {
        handled = true;
      }
    }

    if (!handled) {
      if (!getEditor().verifyWritableRegion(dst, dstStart, dstEnd)) {
        handled = true;
      }
    }

    if (!handled) {
      getEditArea().setHasChanged();
      return null;
    }

    {
      final EditArea editArea = getEditArea();
      final int start = editArea.getSelectionStart();
      final int end = editArea.getSelectionEnd();

      postAction(
        new Runnable() {
          @Override
          public void run () {
            editArea.setSelection(start, end);
          }
        }
      );
    }

    if (menuAction != null) postAction(menuAction);
    return dst.subSequence(dstStart, dstEnd);
  }
}
