package org.nbp.b2g.ui;

public abstract class Control {
  public abstract boolean next ();
  public abstract boolean previous ();
  public abstract String getValue ();
  protected abstract String getLabel ();
  protected abstract String getPreferenceKey ();

  protected void reportValue () {
    ApplicationUtilities.message(getLabel() + " " + getValue());
  }

  public Control () {
  }
}
