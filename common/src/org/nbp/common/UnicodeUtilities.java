package org.nbp.common;

import java.text.Normalizer;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class UnicodeUtilities {
  private final static String LOG_TAG = UnicodeUtilities.class.getName();

  private final static ByteFieldMap directionalityFields = new ByteFieldMap() {
    @Override
    protected final String getMapType () {
      return "Unicode directionality";
    }

    @Override
    protected final String getNamePrefix () {
      return "DIRECTIONALITY_";
    }
  };

  private final static ByteFieldMap categoryFields = new ByteFieldMap() {
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
    ByteFieldMap.makeMaps(
      Character.class,
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

  public static String normalize (String string, Normalizer.Form form) {
    return Normalizer.normalize(string, form);
  }

  public static String compose (String string) {
    return normalize(string, Normalizer.Form.NFC);
  }

  public static String decompose (String string) {
    return normalize(string, Normalizer.Form.NFD);
  }

  public static char getBaseCharacter (char character) {
    return decompose(Character.toString(character)).charAt(0);
  }

  private UnicodeUtilities () {
  }
}
