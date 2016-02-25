package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class RTFOperations extends AsposeWordsOperations {
  public RTFOperations () throws IOException {
    super(SaveFormat.RTF);
  }
}
