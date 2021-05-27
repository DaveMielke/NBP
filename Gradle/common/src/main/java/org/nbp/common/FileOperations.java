package org.nbp.common;

public abstract class FileOperations {
  private FileOperations () {
  }

  static {
    System.loadLibrary("nbp_common");
  }

  public native static String[] getExtendedAttributeNames (
    String path, boolean follow
  );

  public native static String getExtendedAttributeValue (
    String path, String name, boolean follow
  );

  public native static void setExtendedAttribute (
    String path, String name, String value, boolean follow
  );

  public native static void removeExtendedAttribute (
    String path, String name, boolean follow
  );
}
