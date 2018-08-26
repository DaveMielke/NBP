package org.nbp.ipaws;

public class PongHandler extends ResponseHandler {
  public PongHandler () {
    super();
  }

  @Override
  public final boolean handleResponse (String response) {
    return true;
  }
}
