package org.nbp.editor.menu.selection;
import org.nbp.editor.*;

public class Lowercase extends CharacterAction {
  public Lowercase () {
    super();
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
