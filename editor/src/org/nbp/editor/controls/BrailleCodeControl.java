package org.nbp.editor.controls;
import org.nbp.editor.*;

import org.nbp.common.controls.EnumerationControl;

import android.util.Log;

public class BrailleCodeControl extends EnumerationControl<BrailleCode> {
  private final static String LOG_TAG = BrailleCodeControl.class.getName();

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_BrailleCode;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_braille;
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-code";
  }

  @Override
  protected boolean testEnumerationValue (BrailleCode value) {
    String problem;

    try {
      if (value.getTranslator() != null) return true;
      problem = "translator not defined";
    } catch (Exception exception) {
      problem = exception.getMessage();
    }

    StringBuilder sb = new StringBuilder();
    sb.append("braille code not available: ");
    sb.append(getValueLabel(value));
    sb.append(": ");
    sb.append(problem);
    Log.w(LOG_TAG, sb.toString());

    return false;
  }

  @Override
  protected BrailleCode getEnumerationDefault () {
    return ApplicationDefaults.BRAILLE_CODE;
  }

  @Override
  public BrailleCode getEnumerationValue () {
    return ApplicationSettings.BRAILLE_CODE;
  }

  @Override
  protected boolean setEnumerationValue (BrailleCode value) {
    ApplicationSettings.BRAILLE_CODE = value;
    return true;
  }

  public BrailleCodeControl () {
    super();
  }
}
