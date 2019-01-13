package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeDot7 extends TypeCharacter {
  private final static KeySet dot7 = new KeySet(KeySet.DOT_7);

  @Override
  protected final KeySet getNavigationKeys () {
    return dot7;
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public TypeDot7 (Endpoint endpoint) {
    super(endpoint);
  }
}
