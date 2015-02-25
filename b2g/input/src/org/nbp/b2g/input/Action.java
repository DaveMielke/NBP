package org.nbp.b2g.input;

import android.inputmethodservice.InputMethodService;

public abstract class Action {
  public abstract boolean performAction ();

  protected final InputService inputService;

  public Action (InputService inputService) {
    this.inputService = inputService;
  }
}
