package org.nbp.editor.operations;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;
import com.aspose.words.LoadFormat;

public class DocOperations extends AsposeWordsOperations {
  public DocOperations () throws IOException {
    super(SaveFormat.DOC, LoadFormat.DOC);
  }
}
