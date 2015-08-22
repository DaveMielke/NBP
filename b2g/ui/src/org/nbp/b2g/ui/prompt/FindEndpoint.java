package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FindEndpoint extends PromptEndpoint {
  @Override
  protected final boolean handleResponse (String response) {
    boolean found = false;

    if (response.length() > 0) {
      Endpoint endpoint = Endpoints.host.get();

      synchronized (endpoint) {
        int start = endpoint.getBrailleStart();

        if (start < endpoint.getTextLength()) {
          Matcher matcher = Pattern.compile(
            response,
            Pattern.LITERAL | Pattern.CASE_INSENSITIVE
          ).matcher(endpoint.getText().toString());

          if (matcher.find(start+1)) {
            endpoint.setLineIndent(endpoint.setLine(matcher.start()));
            found = true;
          }
        }
      }
    }

    return found;
  }

  public FindEndpoint () {
    super(R.string.prompt_find);
  }
}
