package org.nbp.editor;

import org.nbp.common.CommonSpan;

public abstract class EditorSpan extends CommonSpan {
  public abstract String getSpanIdentifier ();

  protected EditorSpan () {
    super();
  }
}
