package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.EnumerationControl;
import org.nbp.common.dictionary.DictionaryDatabase;

public class DictionaryDatabaseControl extends EnumerationControl<DictionaryDatabase> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_DictionaryDatabase;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_advanced;
  }

  @Override
  protected String getPreferenceKey () {
    return "dictionary-database";
  }

  @Override
  protected String getValueLabel (DictionaryDatabase database) {
    String label = database.getDescription();
    if (label == null) label = super.getValueLabel(database);
    return label;
  }

  @Override
  protected DictionaryDatabase getEnumerationDefault () {
    return ApplicationDefaults.DICTIONARY_DATABASE;
  }

  @Override
  public DictionaryDatabase getEnumerationValue () {
    return ApplicationSettings.DICTIONARY_DATABASE;
  }

  @Override
  protected boolean setEnumerationValue (DictionaryDatabase value) {
    ApplicationSettings.DICTIONARY_DATABASE = value;
    return true;
  }

  public DictionaryDatabaseControl () {
    super();
  }
}
