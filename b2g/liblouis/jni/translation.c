#include "lljni.h"

typedef enum {
  RVI_INPUT_LENGTH,
  RVI_OUTPUT_LENGTH,
  RVI_CURSOR_OFFSET,
} ResultValuesIndex;

JAVA_METHOD(
  org_liblouis_Translation, translate, jboolean,
  jstring jTableName,
  jstring jInputBuffer, jcharArray jOutputBuffer, jshortArray jTypeForm,
  jintArray jOutputOffsets, jintArray jInputOffsets,
  jintArray jResultValues, jboolean backTranslate
) {
  const char *cTableName = (*env)->GetStringUTFChars(env, jTableName, NULL);

  const jchar *cInputBuffer = (*env)->GetStringChars(env, jInputBuffer, NULL);
  jchar *cOutputBuffer = (*env)->GetCharArrayElements(env, jOutputBuffer, NULL);

  int haveTypeForm = jTypeForm != NULL;
  jshort *cTypeForm = haveTypeForm? (*env)->GetShortArrayElements(env, jTypeForm, NULL): NULL;

  jint *cOutputOffsets = (*env)->GetIntArrayElements(env, jOutputOffsets, NULL);
  jint *cInputOffsets = (*env)->GetIntArrayElements(env, jInputOffsets, NULL);

  jint *cResultValues = (*env)->GetIntArrayElements(env, jResultValues, NULL);
  jint *inputLength  = &cResultValues[RVI_INPUT_LENGTH];
  jint *outputLength = &cResultValues[RVI_OUTPUT_LENGTH];
  jint *cursorOffset = &cResultValues[RVI_CURSOR_OFFSET];

  if (*cursorOffset < 0) cursorOffset = NULL;
  int translationMode = backTranslate? (0): (dotsIO | ucBrl);

  char *spacing = NULL;

  memset(cOutputOffsets, (*inputLength * sizeof(*cOutputOffsets)), 0);
  memset(cInputOffsets, (*outputLength * sizeof(*cInputOffsets)), 0);

  int successful =
    ((backTranslate != JNI_FALSE)? lou_backTranslate: lou_translate)(
      cTableName,
      cInputBuffer, inputLength,
      cOutputBuffer, outputLength,
      (uint16_t *)cTypeForm, spacing,
      cOutputOffsets, cInputOffsets,
      cursorOffset, translationMode
    );

  (*env)->ReleaseStringUTFChars(env, jTableName, cTableName);
  (*env)->ReleaseStringChars(env, jInputBuffer, cInputBuffer);
  (*env)->ReleaseCharArrayElements(env, jOutputBuffer, cOutputBuffer, 0);
  if (haveTypeForm) (*env)->ReleaseShortArrayElements(env, jTypeForm, cTypeForm, 0);
  (*env)->ReleaseIntArrayElements(env, jOutputOffsets, cOutputOffsets, 0);
  (*env)->ReleaseIntArrayElements(env, jInputOffsets, cInputOffsets, 0);
  (*env)->ReleaseIntArrayElements(env, jResultValues, cResultValues, 0);

  return successful? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_liblouis_Translation, getBoldBit, jshort
) {
  return bold;
}

JAVA_METHOD(
  org_liblouis_Translation, getItalicBit, jshort
) {
  return italic;
}

JAVA_METHOD(
  org_liblouis_Translation, getUnderlineBit, jshort
) {
  return underline;
}

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
  org_liblouis_Translation, getEmphasisCount, jint
) {
  return emphasisCount;
}

JAVA_METHOD(
  org_liblouis_Translation, getEmphasisBit, jshort,
  jint number
) {
  if (number < 0) return NO_EMPHASIS;
  if (number >= emphasisCount) return NO_EMPHASIS;
  return emphasisTable[number];
}

JAVA_METHOD(
  org_liblouis_TableFile, getEmphasisBit, jshort,
  jstring jTable, jstring jName
) {
  const char *cTable = (*env)->GetStringUTFChars(env, jTable, NULL);
  const char *cName = (*env)->GetStringUTFChars(env, jName, NULL);
  formtype bit = lou_getTypeformForEmphClass(cTable, cName);

  (*env)->ReleaseStringUTFChars(env, jTable, cTable);
  (*env)->ReleaseStringUTFChars(env, jName, cName);
  return bit;
}
