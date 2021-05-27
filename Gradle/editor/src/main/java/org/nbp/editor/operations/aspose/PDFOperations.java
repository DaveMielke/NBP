package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class PDFOperations extends WordsOperations {
  public PDFOperations () throws IOException {
    super(SaveFormat.PDF);
  }
}
