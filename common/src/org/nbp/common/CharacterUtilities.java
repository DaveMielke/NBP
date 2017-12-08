package org.nbp.common;

public abstract class CharacterUtilities {
  public final static char CHAR_NUL   = 0X0000; // null
  public final static char CHAR_SOH   = 0X0001; // start of header
  public final static char CHAR_STX   = 0X0002; // start of text
  public final static char CHAR_ETX   = 0X0003; // end of text
  public final static char CHAR_EOT   = 0X0004; // end of transmission
  public final static char CHAR_ENQ   = 0X0005; // enquiry
  public final static char CHAR_ACK   = 0X0006; // acknowledgement (positive)
  public final static char CHAR_BEL   = 0X0007; // bell
  public final static char CHAR_BS    = 0X0008; // back space
  public final static char CHAR_HT    = 0X0009; // horizontal tab
  public final static char CHAR_LF    = 0X000A; // line feed (new line)
  public final static char CHAR_VT    = 0X000B; // vertical tab
  public final static char CHAR_FF    = 0X000C; // form feed
  public final static char CHAR_CR    = 0X000D; // carriage return
  public final static char CHAR_SO    = 0X000E; // shift out
  public final static char CHAR_SI    = 0X000F; // shift in
  public final static char CHAR_DLE   = 0X0010; // data link escape
  public final static char CHAR_DC1   = 0X0011; // direct control 1 (X-on)
  public final static char CHAR_DC2   = 0X0012; // direct control 2
  public final static char CHAR_DC3   = 0X0013; // direct control 3 (X-off)
  public final static char CHAR_DC4   = 0X0014; // direct control 4
  public final static char CHAR_NAK   = 0X0015; // negative acknowledgement
  public final static char CHAR_SYN   = 0X0016; // synchronous
  public final static char CHAR_ETB   = 0X0017; // end of text block
  public final static char CHAR_CAN   = 0X0018; // cancel
  public final static char CHAR_EM    = 0X0019; // end of medium
  public final static char CHAR_SUB   = 0X001A; // substitute
  public final static char CHAR_ESC   = 0X001B; // escape
  public final static char CHAR_FS    = 0X001C; // field separator
  public final static char CHAR_GS    = 0X001D; // group separator
  public final static char CHAR_RS    = 0X001E; // record separator
  public final static char CHAR_US    = 0X001F; // unit separator
  public final static char CHAR_SPACE = 0X0020; // space
  public final static char CHAR_DEL   = 0X007F; // delete
  public final static char CHAR_NBSP  = 0X00A0; // no-break space

  private final static CharacterFieldMap characterFields = new CharacterFieldMap() {
    @Override
    protected final String getMapType () {
      return "character";
    }

    @Override
    protected final String getNamePrefix () {
      return "CHAR_";
    }
  };

  static {
    CharacterFieldMap.makeMaps(
      CharacterUtilities.class,
      characterFields
    );
  }

  public static Character getCharacter (String name) {
    return characterFields.getValue(name.toUpperCase());
  }

  private CharacterUtilities () {
  }
}
