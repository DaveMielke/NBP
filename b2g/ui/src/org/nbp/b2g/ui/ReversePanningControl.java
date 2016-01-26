package org.nbp.b2g.ui;

public class ReversePanningControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.ReversePanning_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "reverse-panning";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_REVERSE_PANNING;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.REVERSE_PANNING;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.REVERSE_PANNING = value;
    return true;
  }

  public ReversePanningControl () {
    super(false);
  }
}
