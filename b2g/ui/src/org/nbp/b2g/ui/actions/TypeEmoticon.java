package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeEmoticon extends InputAction {
  @Override
  protected final boolean performInputAction (final Endpoint endpoint) {
    StringBuilder message = new StringBuilder();
    message.append(getString(R.string.emoticon_message_select));
    final String[] emoticons = EmoticonUtilities.getEmoticons();

    for (String emoticon : emoticons) {
      message.append('\n');
      message.append(EmoticonUtilities.getEmoticonName(emoticon));
    }

    Endpoints.setPopupEndpoint(
      message.toString(),
      new PopupClickHandler() {
        @Override
        public boolean handleClick (int index) {
          if (index == 0) return false;
          StringBuilder emoticon = new StringBuilder(emoticons[--index]);

          synchronized (endpoint) {
            CharSequence text = endpoint.getText();
            int start = endpoint.getSelectionStart();
            int end = endpoint.getSelectionEnd();

            if (start > 0) {
              if (!Character.isWhitespace(text.charAt(start-1))) {
                emoticon.insert(0, ' ');
              }
            }

            if (end < text.length()) {
              if (!Character.isWhitespace(text.charAt(end))) {
                emoticon.append(' ');
              }
            }

            try {
              return endpoint.insertText(emoticon.toString());
            } finally {
              Endpoints.setHostEndpoint();
            }
          }
        }
      }
    );

    return true;
  }

  public TypeEmoticon (Endpoint endpoint) {
    super(endpoint);
  }
}
