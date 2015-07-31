package org.nbp.b2g.ui;

public class SelectionIndicatorControl extends EnumerationControl<IndicatorOverlay> {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.SelectionIndicator_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "selection-indicator";
  }

  @Override
  protected IndicatorOverlay getEnumerationDefault () {
    return ApplicationParameters.DEFAULT_SELECTION_INDICATOR;
  }

  @Override
  protected IndicatorOverlay getEnumerationValue () {
    return ApplicationSettings.SELECTION_INDICATOR;
  }

  @Override
  protected boolean setEnumerationValue (IndicatorOverlay value) {
    ApplicationSettings.SELECTION_INDICATOR = value;
    return true;
  }

  public SelectionIndicatorControl () {
    super(false);
  }
}
