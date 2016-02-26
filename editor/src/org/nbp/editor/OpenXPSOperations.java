package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class OpenXPSOperations extends AsposeWordsOperations {
  public OpenXPSOperations () throws IOException {
    super(SaveFormat.OPEN_XPS);
  }
}
