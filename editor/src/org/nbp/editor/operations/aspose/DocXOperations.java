package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;
import com.aspose.words.LoadFormat;

public class DocXOperations extends WordsOperations {
  public DocXOperations () throws IOException {
    super(SaveFormat.DOCX, LoadFormat.DOCX);
  }
}
