package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;
import com.aspose.words.LoadFormat;

public class RTFOperations extends WordsOperations {
  public RTFOperations () throws IOException {
    super(SaveFormat.RTF, LoadFormat.RTF);
  }
}
