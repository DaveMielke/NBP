package org.nbp.b2g.input;

import java.util.Arrays;

public class CharacterMap {
  private final char[] characterMap = new char[0X100];

  public char getCharacter (int keyMask) {
    return characterMap[keyMask];
  }

  private void fillCharacterMap () {
    Arrays.fill(characterMap, '?');
    characterMap[KeyMask.DOTS_NONE] = ' ';

    characterMap[KeyMask.DOTS_1] = 'a';
    characterMap[KeyMask.DOTS_12] = 'b';
    characterMap[KeyMask.DOTS_14] = 'c';
    characterMap[KeyMask.DOTS_145] = 'd';
    characterMap[KeyMask.DOTS_15] = 'e';
    characterMap[KeyMask.DOTS_124] = 'f';
    characterMap[KeyMask.DOTS_1245] = 'g';
    characterMap[KeyMask.DOTS_125] = 'h';
    characterMap[KeyMask.DOTS_24] = 'i';
    characterMap[KeyMask.DOTS_245] = 'j';

    characterMap[KeyMask.DOTS_13] = 'k';
    characterMap[KeyMask.DOTS_123] = 'l';
    characterMap[KeyMask.DOTS_134] = 'm';
    characterMap[KeyMask.DOTS_1345] = 'n';
    characterMap[KeyMask.DOTS_135] = 'o';
    characterMap[KeyMask.DOTS_1234] = 'p';
    characterMap[KeyMask.DOTS_12345] = 'q';
    characterMap[KeyMask.DOTS_1235] = 'r';
    characterMap[KeyMask.DOTS_234] = 's';
    characterMap[KeyMask.DOTS_2345] = 't';

    characterMap[KeyMask.DOTS_136] = 'u';
    characterMap[KeyMask.DOTS_1236] = 'v';
    characterMap[KeyMask.DOTS_1346] = 'x';
    characterMap[KeyMask.DOTS_13456] = 'y';
    characterMap[KeyMask.DOTS_1356] = 'z';
    characterMap[KeyMask.DOTS_12346] = '&';
    characterMap[KeyMask.DOTS_123456] = '=';
    characterMap[KeyMask.DOTS_12356] = '(';
    characterMap[KeyMask.DOTS_2346] = '!';
    characterMap[KeyMask.DOTS_23456] = ')';

    characterMap[KeyMask.DOTS_16] = '*';
    characterMap[KeyMask.DOTS_126] = '<';
    characterMap[KeyMask.DOTS_146] = '%';
    characterMap[KeyMask.DOTS_1456] = '?';
    characterMap[KeyMask.DOTS_156] = ':';
    characterMap[KeyMask.DOTS_1246] = '$';
    characterMap[KeyMask.DOTS_12456] = '}';
    characterMap[KeyMask.DOTS_1256] = '|';
    characterMap[KeyMask.DOTS_246] = '{';
    characterMap[KeyMask.DOTS_2456] = 'w';

    characterMap[KeyMask.DOTS_2] = '1';
    characterMap[KeyMask.DOTS_23] = '2';
    characterMap[KeyMask.DOTS_25] = '3';
    characterMap[KeyMask.DOTS_256] = '4';
    characterMap[KeyMask.DOTS_26] = '5';
    characterMap[KeyMask.DOTS_235] = '6';
    characterMap[KeyMask.DOTS_2356] = '7';
    characterMap[KeyMask.DOTS_236] = '8';
    characterMap[KeyMask.DOTS_35] = '9';
    characterMap[KeyMask.DOTS_356] = '0';

    characterMap[KeyMask.DOTS_34] = '/';
    characterMap[KeyMask.DOTS_345] = '>';
    characterMap[KeyMask.DOTS_3456] = '#';
    characterMap[KeyMask.DOTS_346] = '+';
    characterMap[KeyMask.DOTS_3] = '\'';
    characterMap[KeyMask.DOTS_36] = '-';

    characterMap[KeyMask.DOTS_4] = '`';
    characterMap[KeyMask.DOTS_45] = '~';
    characterMap[KeyMask.DOTS_456] = '_';
    characterMap[KeyMask.DOTS_5] = '"';
    characterMap[KeyMask.DOTS_46] = '.';
    characterMap[KeyMask.DOTS_56] = ';';
    characterMap[KeyMask.DOTS_6] = ',';

    characterMap[KeyMask.DOTS_17] = 'A';
    characterMap[KeyMask.DOTS_127] = 'B';
    characterMap[KeyMask.DOTS_147] = 'C';
    characterMap[KeyMask.DOTS_1457] = 'D';
    characterMap[KeyMask.DOTS_157] = 'E';
    characterMap[KeyMask.DOTS_1247] = 'F';
    characterMap[KeyMask.DOTS_12457] = 'G';
    characterMap[KeyMask.DOTS_1257] = 'H';
    characterMap[KeyMask.DOTS_247] = 'I';
    characterMap[KeyMask.DOTS_2457] = 'J';

    characterMap[KeyMask.DOTS_137] = 'K';
    characterMap[KeyMask.DOTS_1237] = 'L';
    characterMap[KeyMask.DOTS_1347] = 'M';
    characterMap[KeyMask.DOTS_13457] = 'N';
    characterMap[KeyMask.DOTS_1357] = 'O';
    characterMap[KeyMask.DOTS_12347] = 'P';
    characterMap[KeyMask.DOTS_123457] = 'Q';
    characterMap[KeyMask.DOTS_12357] = 'R';
    characterMap[KeyMask.DOTS_2347] = 'S';
    characterMap[KeyMask.DOTS_23457] = 'T';

    characterMap[KeyMask.DOTS_1367] = 'U';
    characterMap[KeyMask.DOTS_12367] = 'V';
    characterMap[KeyMask.DOTS_13467] = 'X';
    characterMap[KeyMask.DOTS_134567] = 'Y';
    characterMap[KeyMask.DOTS_13567] = 'Z';

    characterMap[KeyMask.DOTS_124567] = ']';
    characterMap[KeyMask.DOTS_12567] = '\\';
    characterMap[KeyMask.DOTS_2467] = '[';
    characterMap[KeyMask.DOTS_24567] = 'W';
    characterMap[KeyMask.DOTS_47] = '@';
    characterMap[KeyMask.DOTS_457] = '^';
  }

  public CharacterMap () {
    fillCharacterMap();
  }
}
