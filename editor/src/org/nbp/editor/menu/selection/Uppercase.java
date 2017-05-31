package org.nbp.editor.menu.selection;
import org.nbp.editor.*;

public class Uppercase extends CharacterAction {
  public Uppercase () {
    super();
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
