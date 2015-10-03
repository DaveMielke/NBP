package org.nbp.b2g.ui;

public class OneHandControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return ApplicationContext.getString(R.string.OneHand_control_label);
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
  public boolean getBooleanValue () {
    return ApplicationSettings.ONE_HAND;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.ONE_HAND = value;
    return true;
  }

  public OneHandControl () {
    super(false);
  }
}
