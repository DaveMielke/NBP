package org.nbp.b2g.ui;

public abstract class InputEndpoint extends Endpoint {
  public InputEndpoint () {
    super(true);
    addKeyBindings("input");
  }
}
