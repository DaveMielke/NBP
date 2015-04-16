package org.nbp.b2g.ui;

public abstract class Control {
  public abstract boolean next ();
  public abstract boolean previous ();
  protected abstract String getLabel ();

  protected void message (String label, int value) {
    ApplicationUtilities.message(label + " " + value);
  }

  public Control () {
  }
}
