package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.EnumerationControl;

public class ComputerBrailleControl extends EnumerationControl<ComputerBraille> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ComputerBraille;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_advanced;
  }

  @Override
  protected String getPreferenceKey () {
    return "computer-braille";
  }

  @Override
  protected ComputerBraille getEnumerationDefault () {
    return ApplicationDefaults.COMPUTER_BRAILLE;
  }

  @Override
  public ComputerBraille getEnumerationValue () {
    return ApplicationSettings.COMPUTER_BRAILLE;
  }

  @Override
  protected boolean setEnumerationValue (ComputerBraille value) {
    ApplicationSettings.COMPUTER_BRAILLE = value;
    return true;
  }

  public ComputerBrailleControl () {
    super();
  }
}
