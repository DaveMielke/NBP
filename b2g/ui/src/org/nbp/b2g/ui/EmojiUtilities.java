package org.nbp.b2g.ui;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public abstract class EmojiUtilities {
  private EmojiUtilities () {
  }

  private final static Map<String, String> emojiNames;
  private final static Set<String> supportedEmoji;

  private final static void defineEmoji (String emoji, int resource) {
    emojiNames.put(emoji, ApplicationContext.getString(resource));
  }

  static {
    emojiNames = new HashMap<String, String>();

    defineEmoji("=-O", R.string.emoji_face_shock);
    defineEmoji(":-P", R.string.emoji_face_smiling_cheeky);
    defineEmoji(";-)", R.string.emoji_face_winking);
    defineEmoji(":-(", R.string.emoji_face_sad);
    defineEmoji(":-)", R.string.emoji_face_smiling);
    defineEmoji(":-D", R.string.emoji_face_laughing);
    defineEmoji("O:-)", R.string.emoji_face_smiling_angelic);
    defineEmoji(":'(", R.string.emoji_face_crying);
    defineEmoji(":-\\", R.string.emoji_face_smiling_scheptical);
    defineEmoji(":-|", R.string.emoji_face_straight);

    supportedEmoji = emojiNames.keySet();
  }

  public final static Set<String> getSupportedEmoji () {
    return supportedEmoji;
  }

  public final static String getEmojiName (String emoji) {
    return emojiNames.get(emoji);
  }
}
