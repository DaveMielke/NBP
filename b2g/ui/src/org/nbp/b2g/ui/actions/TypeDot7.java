package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeDot7 extends TypeCharacter {
  @Override
  protected int getNavigationKeys () {
    return KeyMask.DOT_7;
  }

  public TypeDot7 (Endpoint endpoint) {
    super(endpoint);
  }
}
