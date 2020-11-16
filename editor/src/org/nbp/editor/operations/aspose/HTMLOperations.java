package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;
import com.aspose.words.LoadFormat;

public class HTMLOperations extends WordsOperations {
  public HTMLOperations () throws IOException {
    super(SaveFormat.HTML, LoadFormat.HTML);
  }
}
