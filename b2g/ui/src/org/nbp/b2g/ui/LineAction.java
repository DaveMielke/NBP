package org.nbp.b2g.ui;

public abstract class LineAction extends DirectionalAction {
  protected final ActionResult setLine (Endpoint endpoint, int offset) {
    endpoint.setLine(offset);
    endpoint.setLineIndent(0);
    return ActionResult.WRITE;
  }

  protected LineAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}
