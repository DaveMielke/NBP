package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class OXPSOperations extends WordsOperations {
  public OXPSOperations () throws IOException {
    super(SaveFormat.OPEN_XPS);
  }
}
