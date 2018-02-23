package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeDot7 extends TypeCharacter {
  @Override
  protected final int getNavigationKeys () {
    return KeyMask.DOT_7;
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public TypeDot7 (Endpoint endpoint) {
    super(endpoint);
  }
}
