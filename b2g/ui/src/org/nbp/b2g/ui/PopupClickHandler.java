package org.nbp.b2g.ui;

public abstract class PopupClickHandler implements ValueHandler<Integer> {
  protected abstract boolean handleClick (int index);

  @Override
  public final boolean handleValue (Integer index) {
    return handleClick(index);
  }

  protected PopupClickHandler () {
  }
}
