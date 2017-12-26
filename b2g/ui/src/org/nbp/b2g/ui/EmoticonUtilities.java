package org.nbp.b2g.ui;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public abstract class EmoticonUtilities {
  private EmoticonUtilities () {
  }

  private final static Map<String, String> emoticonNames;
  private final static Set<String> supportedEmoticons;

  private final static void defineEmoticon (String emoticon, int resource) {
    emoticonNames.put(emoticon, ApplicationContext.getString(resource));
  }

  static {
    emoticonNames = new HashMap<String, String>();

    defineEmoticon(":-)", R.string.emoticon_smile);
    defineEmoticon(":-(", R.string.emoticon_frown);
    defineEmoticon(";-)", R.string.emoticon_winking);
    defineEmoticon(":-P", R.string.emoticon_cheeky);
    defineEmoticon(":-O", R.string.emoticon_surprised);
    defineEmoticon(":-*", R.string.emoticon_kissing);
    defineEmoticon(">:O", R.string.emoticon_yelling);
    defineEmoticon("B-)", R.string.emoticon_cool);
    defineEmoticon(":-!", R.string.emoticon_foot_in_mouth);
    defineEmoticon(":-[", R.string.emoticon_embarrassed);
    defineEmoticon("O:-)", R.string.emoticon_angel);
    defineEmoticon(":-\\", R.string.emoticon_undecided);
    defineEmoticon(":'-(", R.string.emoticon_tears_sad);
    defineEmoticon(":-X", R.string.emoticon_lips_sealed);
    defineEmoticon(":-D", R.string.emoticon_laughing);
    defineEmoticon("o_O", R.string.emoticon_confused);
    defineEmoticon("<3", R.string.emoticon_heart);
    defineEmoticon("x-(", R.string.emoticon_mad);
    defineEmoticon(":-/", R.string.emoticon_skeptical);
    defineEmoticon(":-|", R.string.emoticon_indecision);
    defineEmoticon("#-)", R.string.emoticon_partied);
    defineEmoticon("%-)", R.string.emoticon_drunk);
    defineEmoticon(":-###", R.string.emoticon_sick);
    defineEmoticon("<:-|", R.string.emoticon_dunce);
    defineEmoticon(">:-)", R.string.emoticon_evil);
    defineEmoticon(":'-)", R.string.emoticon_tears_happy);
    defineEmoticon(":-))", R.string.emoticon_smile_big);
    defineEmoticon("|-O", R.string.emoticon_bored);

    supportedEmoticons = emoticonNames.keySet();
  }

  public final static Set<String> getSupportedEmoticons () {
    return supportedEmoticons;
  }

  public final static String getEmoticonName (String emoticon) {
    return emoticonNames.get(emoticon);
  }
}
