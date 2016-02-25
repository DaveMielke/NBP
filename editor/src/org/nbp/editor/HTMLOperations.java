package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class HTMLOperations extends AsposeWordsOperations {
  public HTMLOperations () throws IOException {
    super(SaveFormat.HTML);
  }
}
