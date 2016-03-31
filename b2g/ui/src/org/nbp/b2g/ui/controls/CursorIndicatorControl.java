package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class CursorIndicatorControl extends EnumerationControl<IndicatorOverlay> {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.CursorIndicator_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "cursor-indicator";
  }

  @Override
  protected IndicatorOverlay getEnumerationDefault () {
    return ApplicationDefaults.CURSOR_INDICATOR;
  }

  @Override
  public IndicatorOverlay getEnumerationValue () {
    return ApplicationSettings.CURSOR_INDICATOR;
  }

  @Override
  protected boolean setEnumerationValue (IndicatorOverlay value) {
    ApplicationSettings.CURSOR_INDICATOR = value;
    return true;
  }

  public CursorIndicatorControl () {
    super(false);
  }
}
