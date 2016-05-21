package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class SelectionIndicatorControl extends EnumerationControl<IndicatorOverlay> {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.SelectionIndicator_control_label);
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
    super(ControlGroup.RENDERING);
  }
}
