package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;
import com.aspose.words.LoadFormat;

public class DocOperations extends WordsOperations {
  public DocOperations () throws IOException {
    super(SaveFormat.DOC, LoadFormat.DOC);
  }
}
