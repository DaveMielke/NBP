package org.nbp.b2g.input;

public class PowerOffAction extends ScanCodeAction {
  public PowerOffAction () {
    super("POWER", ApplicationUtilities.getGlobalActionTimeout());
  }
}
