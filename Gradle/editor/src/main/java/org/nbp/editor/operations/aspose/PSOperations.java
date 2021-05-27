package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class PSOperations extends WordsOperations {
  public PSOperations () throws IOException {
    super(SaveFormat.PS);
  }
}
