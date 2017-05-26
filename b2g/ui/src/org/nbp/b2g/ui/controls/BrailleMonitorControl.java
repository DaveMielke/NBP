package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class BrailleMonitorControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_BrailleMonitor;
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-monitor";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.BRAILLE_MONITOR;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.BRAILLE_MONITOR;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    BrailleMonitorWindow window = Devices.braille.get().getMonitorWindow();
    if (window != null) window.setVisibility(value);

    ApplicationSettings.BRAILLE_MONITOR = value;
    return true;
  }

  public BrailleMonitorControl () {
    super(ControlGroup.BRAILLE);
  }
}
