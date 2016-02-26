package org.nbp.editor;

import java.io.IOException;

import com.aspose.words.SaveFormat;
import com.aspose.words.LoadFormat;

public class MHTMLOperations extends AsposeWordsOperations {
  public MHTMLOperations () throws IOException {
    super(SaveFormat.MHTML, LoadFormat.MHTML);
  }
}
