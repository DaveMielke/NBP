package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SayPhonetic extends SayCharacter {
  private final static CharacterPhrase.Cache cache =
                   new CharacterPhrase.Cache();

  private final static CharacterPhrase.Dictionary dictionary =
                   new CharacterPhrase.Dictionary()
  {
    private void put (char letter, String word) {
      super.put(letter, word);
      super.put(Character.toUpperCase(letter), word);
    }

    {
      put('a', "Alpha");
      put('b', "Bravo");
      put('c', "Charlie");
      put('d', "Delta");
      put('e', "Echo");
      put('f', "Foxtrot");
      put('g', "Golf");
      put('h', "Hotel");
      put('i', "India");
      put('j', "Juliet");
      put('k', "Kilo");
      put('l', "Lima");
      put('m', "Mike");
      put('n', "November");
      put('o', "Oscar");
      put('p', "Papa");
      put('q', "Quebec");
      put('r', "Romeo");
      put('s', "Sierra");
      put('t', "Tango");
      put('u', "Uniform");
      put('v', "Victor");
      put('w', "Whiskey");
      put('x', "X-ray");
      put('y', "Yankee");
      put('z', "Zulu");
    }
  };

  @Override
  protected String toString (char character) {
    return CharacterPhrase.get(character, cache, dictionary);
  }

  public SayPhonetic (Endpoint endpoint) {
    super(endpoint);
  }
}
