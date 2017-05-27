package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class ReversePanningControl extends BooleanControl {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_keyboard;
  }

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ReversePanning;
  }

  @Override
  protected String getPreferenceKey () {
    return "reverse-panning";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.REVERSE_PANNING;
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
    super();
  }
}
