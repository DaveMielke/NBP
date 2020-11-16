package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class XPSOperations extends WordsOperations {
  public XPSOperations () throws IOException {
    super(SaveFormat.XPS);
  }
}
