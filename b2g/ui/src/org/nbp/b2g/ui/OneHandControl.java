package org.nbp.b2g.ui;

public class OneHandControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.oneHand_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "one-hand";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_ONE_HAND;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationParameters.ONE_HAND_MODE;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    return ApplicationParameters.ONE_HAND_MODE = value;
  }

  public OneHandControl () {
    super();
  }
}
