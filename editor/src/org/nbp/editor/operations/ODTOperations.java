package org.nbp.editor.operations;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;
import com.aspose.words.LoadFormat;

public class ODTOperations extends AsposeWordsOperations {
  public ODTOperations () throws IOException {
    super(SaveFormat.ODT, LoadFormat.ODT);
  }
}
