#include "lljni.h"

static const formtype emphasisTable[] = {
  emph_4,
  emph_5,
  emph_6,
  emph_7,
  emph_8,
  emph_9,
  emph_10,

#ifdef comp_emph_1
comp_emph_1,
#endif /* comp_emph_1 */

#ifdef comp_emph_2
comp_emph_2,
#endif /* comp_emph_2 */

#ifdef comp_emph_3
comp_emph_3,
#endif /* comp_emph_3 */
};

static const uint8_t emphasisCount = sizeof(emphasisTable) / sizeof(emphasisTable[0]);
static const formtype NO_EMPHASIS = 0;

JAVA_METHOD(
  org_liblouis_Emphasis, getCount, jint
) {
  return emphasisCount;
}

JAVA_METHOD(
  org_liblouis_Emphasis, getBit, jshort,
  jint number
) {
  if (number < 0) return NO_EMPHASIS;
  if (number >= emphasisCount) return NO_EMPHASIS;
  return emphasisTable[number];
}

JAVA_METHOD(
  org_liblouis_Emphasis, getBoldBit, jshort
) {
  return bold;
}

JAVA_METHOD(
  org_liblouis_Emphasis, getItalicBit, jshort
) {
  return italic;
}

JAVA_METHOD(
  org_liblouis_Emphasis, getUnderlineBit, jshort
) {
  return underline;
}
