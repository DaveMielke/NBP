package org.nbp.editor.menu.text;
import org.nbp.editor.*;

public class Lowercase extends CharacterAction {
  public Lowercase (EditorActivity editor) {
    super(editor);
  }

  @Override
  protected final boolean testCharacter (char character) {
    return Character.isUpperCase(character);
  }

  @Override
  protected final char translateCharacter (char character) {
    return Character.toLowerCase(character);
  }
}
