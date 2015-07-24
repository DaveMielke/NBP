package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class UnicodeUtilities {
  private final static String LOG_TAG = UnicodeUtilities.class.getName();

  private final static NumericFieldMap directionalityFields = new NumericFieldMap() {
    @Override
    protected final String getMapType () {
      return "Unicode directionality";
    }

    @Override
    protected final String getNamePrefix () {
      return "DIRECTIONALITY_";
    }
  };

  private final static NumericFieldMap categoryFields = new NumericFieldMap() {
    @Override
    protected final String getMapType () {
      return "Unicode category";
    }

    @Override
    protected final String getNamePrefix () {
      return null;
    }
  };

  static {
    NumericFieldMap.makeMaps(
      Character.class, byte.class,
      directionalityFields,
      categoryFields // must be last (no name prefix)
    );
  }

  public static String getDirectionalityName (int value) {
    return directionalityFields.getName(value);
  }

  public static String getCategoryName (int value) {
    return categoryFields.getName(value);
  }

  private UnicodeUtilities () {
  }
}
