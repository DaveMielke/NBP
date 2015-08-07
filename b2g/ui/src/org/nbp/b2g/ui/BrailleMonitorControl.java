package org.nbp.b2g.ui;

public class BrailleMonitorControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.BrailleMonitor_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-monitor";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_BRAILLE_MONITOR;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationSettings.BRAILLE_MONITOR;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.BRAILLE_MONITOR = value;
    return true;
  }

  public BrailleMonitorControl () {
    super(false);
  }
}
