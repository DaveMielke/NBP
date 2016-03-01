package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class OXPSOperations extends AsposeWordsOperations {
  public OXPSOperations () throws IOException {
    super(SaveFormat.OPEN_XPS);
  }
}
