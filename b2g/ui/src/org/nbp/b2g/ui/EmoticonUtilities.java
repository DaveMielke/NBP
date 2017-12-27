package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.LinkedHashSet;

public abstract class EmoticonUtilities {
  private EmoticonUtilities () {
  }

  private final static Set<String> supportedEmoticons;
  private final static Map<String, String> emoticonNames;

  private final static void defineEmoticon (String emoticon, int resource) {
    supportedEmoticons.add(emoticon);
    emoticonNames.put(emoticon, ApplicationContext.getString(resource));
  }

  static {
    supportedEmoticons = new LinkedHashSet<String>();
    emoticonNames = new HashMap<String, String>();

    defineEmoticon(":-)", R.string.emoticon_smile);
    defineEmoticon(":-))", R.string.emoticon_smile_big);
    defineEmoticon(";-)", R.string.emoticon_winking);
    defineEmoticon(":-(", R.string.emoticon_frown);
    defineEmoticon(":'-)", R.string.emoticon_tears_happy);
    defineEmoticon(":'-(", R.string.emoticon_tears_sad);
    defineEmoticon("o_O", R.string.emoticon_confused);
    defineEmoticon("|-O", R.string.emoticon_bored);
    defineEmoticon(":-D", R.string.emoticon_laughing);
    defineEmoticon(":-O", R.string.emoticon_surprised);
    defineEmoticon(":-P", R.string.emoticon_cheeky);
    defineEmoticon(":-*", R.string.emoticon_kissing);
    defineEmoticon("<3", R.string.emoticon_heart);
    defineEmoticon(":-X", R.string.emoticon_lips_sealed);
    defineEmoticon(":-[", R.string.emoticon_embarrassed);
    defineEmoticon(":-!", R.string.emoticon_foot_in_mouth);
    defineEmoticon(":-/", R.string.emoticon_skeptical);
    defineEmoticon(":-\\", R.string.emoticon_undecided);
    defineEmoticon(">:O", R.string.emoticon_yelling);
    defineEmoticon("x-(", R.string.emoticon_mad);
    defineEmoticon(":-|", R.string.emoticon_indecision);
    defineEmoticon("B-)", R.string.emoticon_cool);
    defineEmoticon("O:-)", R.string.emoticon_angel);
    defineEmoticon(">:-)", R.string.emoticon_evil);
    defineEmoticon("<:-|", R.string.emoticon_dunce);
    defineEmoticon(":-###", R.string.emoticon_sick);
    defineEmoticon("%-)", R.string.emoticon_drunk);
    defineEmoticon("#-)", R.string.emoticon_partied);
  }

  public final static String[] getEmoticons () {
    Set<String> emoticons = supportedEmoticons;
    return emoticons.toArray(new String[emoticons.size()]);
  }

  public final static String getEmoticonName (String emoticon) {
    return emoticonNames.get(emoticon);
  }
}
