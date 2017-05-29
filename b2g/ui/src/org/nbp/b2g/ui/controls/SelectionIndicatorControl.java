package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.EnumerationControl;

public class SelectionIndicatorControl extends EnumerationControl<IndicatorOverlay> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SelectionIndicator;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_braille;
  }

  @Override
  protected String getPreferenceKey () {
    return "selection-indicator";
  }

  @Override
  protected IndicatorOverlay getEnumerationDefault () {
    return ApplicationDefaults.SELECTION_INDICATOR;
  }

  @Override
  public IndicatorOverlay getEnumerationValue () {
    return ApplicationSettings.SELECTION_INDICATOR;
  }

  @Override
  protected boolean setEnumerationValue (IndicatorOverlay value) {
    ApplicationSettings.SELECTION_INDICATOR = value;
    return true;
  }

  public SelectionIndicatorControl () {
    super();
  }
}
