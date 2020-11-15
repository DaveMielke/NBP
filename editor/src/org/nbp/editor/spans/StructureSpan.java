package org.nbp.editor.spans;
import org.nbp.editor.*;

public abstract class StructureSpan extends EditorSpan {
  protected StructureSpan () {
    super();
    setContainsProtectedText(false);
  }
}
