package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class DocXOperations extends AsposeWordsOperations {
  public DocXOperations () throws IOException {
    super(SaveFormat.DOCX);
  }
}
