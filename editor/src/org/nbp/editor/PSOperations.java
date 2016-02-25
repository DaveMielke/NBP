package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class PSOperations extends AsposeWordsOperations {
  public PSOperations () throws IOException {
    super(SaveFormat.PS);
  }
}
