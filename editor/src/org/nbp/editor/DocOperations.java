package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class DocOperations extends AsposeWordsOperations {
  public DocOperations () throws IOException {
    super(SaveFormat.DOC);
  }
}
