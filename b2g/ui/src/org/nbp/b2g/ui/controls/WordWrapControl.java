package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class WordWrapControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_WordWrap;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "word-wrap";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.WORD_WRAP;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.WORD_WRAP;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.WORD_WRAP = value;
    Endpoints.getCurrentEndpoint().refresh();
    return true;
  }

  public WordWrapControl () {
    super();
  }
}
