package org.nbp.editor.operations;
import org.nbp.editor.*;

import java.io.IOException;

import com.aspose.words.SaveFormat;

public class EPubOperations extends AsposeWordsOperations {
  public EPubOperations () throws IOException {
    super(SaveFormat.EPUB);
  }
}
