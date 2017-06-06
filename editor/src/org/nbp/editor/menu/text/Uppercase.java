package org.nbp.editor.menu.text;
import org.nbp.editor.*;

public class Uppercase extends CharacterAction {
  public Uppercase (EditorActivity editor) {
    super(editor);
  }

  @Override
  protected final boolean testCharacter (char character) {
    return Character.isLowerCase(character);
  }

  @Override
  protected final char translateCharacter (char character) {
    return Character.toUpperCase(character);
  }
}
