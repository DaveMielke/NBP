package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeDot8 extends TypeCharacter {
  @Override
  protected int getNavigationKeys () {
    return KeyMask.DOT_8;
  }

  public TypeDot8 (Endpoint endpoint) {
    super(endpoint);
  }
}
