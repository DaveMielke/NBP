package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

public class FindEndpoint extends PromptEndpoint {
  @Override
  public boolean done (String response) {
    boolean found = false;
    Endpoint hostEndpoint = Endpoints.getHostEndpoint();

    synchronized (hostEndpoint) {
      int start = hostEndpoint.getBrailleStart();

      if (start < hostEndpoint.getTextLength()) {
        String text = hostEndpoint.getText();
        int offset = text.indexOf(response, start+1);

        if (offset >= 0) {
          hostEndpoint.setLineIndent(hostEndpoint.setLine(offset));
          found = true;
        }
      }
    }

    return found;
  }

  public FindEndpoint () {
    super("find");
  }
}
