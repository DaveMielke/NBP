package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;
import com.aspose.words.LoadFormat;

public class RTFOperations extends AsposeWordsOperations {
  public RTFOperations () throws IOException {
    super(SaveFormat.RTF, LoadFormat.RTF);
  }
}
