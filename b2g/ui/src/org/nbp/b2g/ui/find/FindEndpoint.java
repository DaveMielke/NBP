package org.nbp.b2g.ui.find;
import org.nbp.b2g.ui.*;

public class FindEndpoint extends PromptEndpoint {
  private final static String[] keysFileNames = new String[] {
    "nabcc", "all", "find"
  };

  @Override
  protected String[] getKeysFileNames () {
    return keysFileNames;
  }

  public FindEndpoint () {
    super("find");
  }
}
