package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeDot8 extends TypeCharacter {
  private final static KeySet dot8 = new KeySet() {
    {
      set(KeySet.DOT_8);
    }
  };

  @Override
  protected final KeySet getNavigationKeys () {
    return dot8;
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public TypeDot8 (Endpoint endpoint) {
    super(endpoint);
  }
}
