package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class PDFOperations extends AsposeWordsOperations {
  public PDFOperations () throws IOException {
    super(SaveFormat.PDF);
  }
}
