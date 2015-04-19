package org.nbp.b2g.ui;

public class LongPressControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.longPress_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "long-press";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_LONG_PRESS;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationParameters.LONG_PRESS_ACTIONS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationParameters.LONG_PRESS_ACTIONS = value;
    return true;
  }

  public LongPressControl () {
    super();
  }
}
