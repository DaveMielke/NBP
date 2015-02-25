package org.nbp.b2g.input;

import java.util.Arrays;

public class CharacterMap {
  private final char[] characterMap = new char[0X100];

  public char getCharacter (int keyMask) {
    return characterMap[keyMask];
  }

  private void fillCharacterMap () {
    Arrays.fill(characterMap, '?');
    characterMap[KeyMask.DotsNone] = ' ';

    characterMap[KeyMask.Dots1] = 'a';
    characterMap[KeyMask.Dots12] = 'b';
    characterMap[KeyMask.Dots14] = 'c';
    characterMap[KeyMask.Dots145] = 'd';
    characterMap[KeyMask.Dots15] = 'e';
    characterMap[KeyMask.Dots124] = 'f';
    characterMap[KeyMask.Dots1245] = 'g';
    characterMap[KeyMask.Dots125] = 'h';
    characterMap[KeyMask.Dots24] = 'i';
    characterMap[KeyMask.Dots245] = 'j';

    characterMap[KeyMask.Dots13] = 'k';
    characterMap[KeyMask.Dots123] = 'l';
    characterMap[KeyMask.Dots134] = 'm';
    characterMap[KeyMask.Dots1345] = 'n';
    characterMap[KeyMask.Dots135] = 'o';
    characterMap[KeyMask.Dots1234] = 'p';
    characterMap[KeyMask.Dots12345] = 'q';
    characterMap[KeyMask.Dots1235] = 'r';
    characterMap[KeyMask.Dots234] = 's';
    characterMap[KeyMask.Dots2345] = 't';

    characterMap[KeyMask.Dots136] = 'u';
    characterMap[KeyMask.Dots1236] = 'v';
    characterMap[KeyMask.Dots1346] = 'x';
    characterMap[KeyMask.Dots13456] = 'y';
    characterMap[KeyMask.Dots1356] = 'z';
    characterMap[KeyMask.Dots12346] = '&';
    characterMap[KeyMask.Dots123456] = '=';
    characterMap[KeyMask.Dots12356] = '(';
    characterMap[KeyMask.Dots2346] = '!';
    characterMap[KeyMask.Dots23456] = ')';

    characterMap[KeyMask.Dots16] = '*';
    characterMap[KeyMask.Dots126] = '<';
    characterMap[KeyMask.Dots146] = '%';
    characterMap[KeyMask.Dots1456] = '?';
    characterMap[KeyMask.Dots156] = ':';
    characterMap[KeyMask.Dots1246] = '$';
    characterMap[KeyMask.Dots12456] = '}';
    characterMap[KeyMask.Dots1256] = '|';
    characterMap[KeyMask.Dots246] = '{';
    characterMap[KeyMask.Dots2456] = 'w';

    characterMap[KeyMask.Dots2] = '1';
    characterMap[KeyMask.Dots23] = '2';
    characterMap[KeyMask.Dots25] = '3';
    characterMap[KeyMask.Dots256] = '4';
    characterMap[KeyMask.Dots26] = '5';
    characterMap[KeyMask.Dots235] = '6';
    characterMap[KeyMask.Dots2356] = '7';
    characterMap[KeyMask.Dots236] = '8';
    characterMap[KeyMask.Dots35] = '9';
    characterMap[KeyMask.Dots356] = '0';

    characterMap[KeyMask.Dots34] = '/';
    characterMap[KeyMask.Dots345] = '>';
    characterMap[KeyMask.Dots3456] = '#';
    characterMap[KeyMask.Dots346] = '+';
    characterMap[KeyMask.Dots3] = '\'';
    characterMap[KeyMask.Dots36] = '-';

    characterMap[KeyMask.Dots4] = '`';
    characterMap[KeyMask.Dots45] = '~';
    characterMap[KeyMask.Dots456] = '_';
    characterMap[KeyMask.Dots5] = '"';
    characterMap[KeyMask.Dots46] = '.';
    characterMap[KeyMask.Dots56] = ';';
    characterMap[KeyMask.Dots6] = ',';

    characterMap[KeyMask.Dots17] = 'A';
    characterMap[KeyMask.Dots127] = 'B';
    characterMap[KeyMask.Dots147] = 'C';
    characterMap[KeyMask.Dots1457] = 'D';
    characterMap[KeyMask.Dots157] = 'E';
    characterMap[KeyMask.Dots1247] = 'F';
    characterMap[KeyMask.Dots12457] = 'G';
    characterMap[KeyMask.Dots1257] = 'H';
    characterMap[KeyMask.Dots247] = 'I';
    characterMap[KeyMask.Dots2457] = 'J';

    characterMap[KeyMask.Dots137] = 'K';
    characterMap[KeyMask.Dots1237] = 'L';
    characterMap[KeyMask.Dots1347] = 'M';
    characterMap[KeyMask.Dots13457] = 'N';
    characterMap[KeyMask.Dots1357] = 'O';
    characterMap[KeyMask.Dots12347] = 'P';
    characterMap[KeyMask.Dots123457] = 'Q';
    characterMap[KeyMask.Dots12357] = 'R';
    characterMap[KeyMask.Dots2347] = 'S';
    characterMap[KeyMask.Dots23457] = 'T';

    characterMap[KeyMask.Dots1367] = 'U';
    characterMap[KeyMask.Dots12367] = 'V';
    characterMap[KeyMask.Dots13467] = 'X';
    characterMap[KeyMask.Dots134567] = 'Y';
    characterMap[KeyMask.Dots13567] = 'Z';

    characterMap[KeyMask.Dots124567] = ']';
    characterMap[KeyMask.Dots12567] = '\\';
    characterMap[KeyMask.Dots2467] = '[';
    characterMap[KeyMask.Dots24567] = 'W';
    characterMap[KeyMask.Dots47] = '@';
    characterMap[KeyMask.Dots457] = '^';
  }

  public CharacterMap () {
    fillCharacterMap();
  }
}
