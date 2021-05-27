package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;
import com.aspose.words.LoadFormat;

public class ODTOperations extends WordsOperations {
  public ODTOperations () throws IOException {
    super(SaveFormat.ODT, LoadFormat.ODT);
  }
}
