package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class XPSOperations extends AsposeWordsOperations {
  public XPSOperations () throws IOException {
    super(SaveFormat.XPS);
  }
}
