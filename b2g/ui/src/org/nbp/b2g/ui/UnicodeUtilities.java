package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class UnicodeUtilities {
  private final static String LOG_TAG = UnicodeUtilities.class.getName();

  private final static FieldNameMap directionalityNames = new FieldNameMap() {
    @Override
    protected final String getMapType () {
      return "Unicode directionality";
    }

    @Override
    protected final String getNamePrefix () {
      return "DIRECTIONALITY_";
    }
  };

  private final static FieldNameMap categoryNames = new FieldNameMap() {
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
    FieldNameMap.makeFieldNameMaps(
      Character.class, byte.class,
      directionalityNames,
      categoryNames // must be last (no name prefix)
    );
  }

  public static String getDirectionalityName (int value) {
    return directionalityNames.getName(value);
  }

  public static String getCategoryName (int value) {
    return categoryNames.getName(value);
  }

  private UnicodeUtilities () {
  }
}
