package com.duxburysystems;

import java.util.Map;
import java.util.HashMap;

public abstract class BrailleTranslationErrors {
  private BrailleTranslationErrors () {
  }

  public final static int SUCCESS = 0;
  public final static int CANNOT_OPEN_BTB = 1;
  public final static int CANNOT_INITIALIZE_BTE = 2;
  public final static int CANNOT_OPEN_SCT = 3;
  public final static int CANNOT_INITIALIZE_NIN = 4;
  public final static int CANNOT_READ_INPUT = 5;
  public final static int CANNOT_RESET_NIN = 6;
  public final static int CANNOT_WRITE_OUTPUT = 7;
  public final static int TRANSLATION_FAILED = 8;
  public final static int LICENSE_NOT_FOUND = 9;
  public final static int INVALID_LICENSE = 10;
  public final static int CANNOT_OPEN_CHITAB = 11;
  public final static int CANNOT_ALLOCATE_MAP = 12;
  public final static int CANNOT_RESET_BTE = 13;
  public final static int INVALID_PARAMETER = 14;
  public final static int CANNOT_OPEN_SBT = 15;
  public final static int INVALID_VARIANT = 22;
  public final static int NO_MEMORY = 23;

  private final static Map<Integer, String> errorMessages =
               new HashMap<Integer, String>() {
    {
      put(SUCCESS, "success");
      put(CANNOT_OPEN_BTB, "cannot open braille table");
      put(CANNOT_INITIALIZE_BTE, "cannot initialize BTE submodule");
      put(CANNOT_OPEN_SCT, "cannot open scanning control table");
      put(CANNOT_INITIALIZE_NIN, "cannot initialize NIN submodule");
      put(CANNOT_READ_INPUT, "cannot read input stream");
      put(CANNOT_RESET_NIN, "cannot reset NIN submodule");
      put(CANNOT_WRITE_OUTPUT, "cannot write output stream");
      put(TRANSLATION_FAILED, "translation failed");
      put(LICENSE_NOT_FOUND, "license not found");
      put(INVALID_LICENSE, "invalid license");
      put(CANNOT_OPEN_CHITAB, "cannot open chitab.txt");
      put(CANNOT_ALLOCATE_MAP, "cannot allocate map");
      put(CANNOT_RESET_BTE, "cannot reset BTE submodule");
      put(INVALID_PARAMETER, "invalid parameter");
      put(CANNOT_OPEN_SBT, "cannot open scrub table");
      put(INVALID_VARIANT, "invalid variant");
      put(NO_MEMORY, "out of memory");
    }
  };

  public final static String getMessage (int error) {
    if (error < 0) error = -error;
    String message = errorMessages.get(error);

    StringBuilder sb = new StringBuilder();
    sb.append("braille translation error ");
    sb.append(error);

    if (message != null) {
      sb.append(": ");
      sb.append(message);
    }

    return sb.toString();
  }
}
