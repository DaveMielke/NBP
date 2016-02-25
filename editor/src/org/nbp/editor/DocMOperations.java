package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;
import com.aspose.words.LoadFormat;

public class DocMOperations extends AsposeWordsOperations {
  public DocMOperations () throws IOException {
    super(SaveFormat.DOCM, LoadFormat.DOCM);
  }
}
