package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

public class FindEndpoint extends PromptEndpoint {
  @Override
  public boolean handleResponse (String response) {
    boolean found = false;

    if (response.length() > 0) {
      Endpoint endpoint = Endpoints.getHostEndpoint();

      synchronized (endpoint) {
        int start = endpoint.getBrailleStart();

        if (start < endpoint.getTextLength()) {
          String text = endpoint.getText();
          int offset = text.indexOf(response, start+1);

          if (offset >= 0) {
            endpoint.setLineIndent(endpoint.setLine(offset));
            found = true;
          }
        }
      }
    }

    return found;
  }

  public FindEndpoint () {
    super("find");
  }
}
