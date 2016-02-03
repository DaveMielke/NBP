package org.nbp.common;

import java.nio.charset.Charset;

public abstract class CommonParameters {
  public final static String INPUT_ENCODING_NAME = "UTF8";
  public final static Charset INPUT_ENCODING_CHARSET = Charset.forName(INPUT_ENCODING_NAME);

  public final static int SCREEN_LEFT_OFFSET = 60; // DIPs

  protected CommonParameters () {
  }
}
