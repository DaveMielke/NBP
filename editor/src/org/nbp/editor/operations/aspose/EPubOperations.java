package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class EPubOperations extends WordsOperations {
  public EPubOperations () throws IOException {
    super(SaveFormat.EPUB);
  }
}
