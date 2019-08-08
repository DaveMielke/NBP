#include "lljni.h"

typedef enum {
  RVI_INPUT_LENGTH,
  RVI_OUTPUT_LENGTH,
  RVI_CURSOR_OFFSET,
} ResultValuesIndex;

JAVA_METHOD(
  org_liblouis_InternalTranslator, translate, jboolean,
  jstring jTableList, jstring jInputBuffer, jcharArray jOutputBuffer,
  jshortArray jTypeForm, jintArray jOutputOffsets, jintArray jInputOffsets,
  jintArray jResultValues, jboolean jBackTranslate, jboolean jNoContractions
) {
  const char *cTableList = (*env)->GetStringUTFChars(env, jTableList, NULL);

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
  int translationMode = 0;
  char *spacing = NULL;

  int cBackTranslate = jBackTranslate != JNI_FALSE;
  int cNoContractions = jNoContractions != JNI_FALSE;

  if (cBackTranslate) {
    translationMode |= noUndefinedDots;
  } else {
    translationMode |= dotsIO | ucBrl;
    if (cNoContractions) translationMode |= noContractions;
  }

  memset(cOutputOffsets, (*inputLength * sizeof(*cOutputOffsets)), 0);
  memset(cInputOffsets, (*outputLength * sizeof(*cInputOffsets)), 0);

  int successful =
    (cBackTranslate? lou_backTranslate: lou_translate)(
      cTableList, cInputBuffer, inputLength, cOutputBuffer, outputLength,
      (uint16_t *)cTypeForm, spacing, cOutputOffsets, cInputOffsets,
      cursorOffset, translationMode
    );

  (*env)->ReleaseStringUTFChars(env, jTableList, cTableList);
  (*env)->ReleaseStringChars(env, jInputBuffer, cInputBuffer);
  (*env)->ReleaseCharArrayElements(env, jOutputBuffer, cOutputBuffer, 0);
  if (haveTypeForm) (*env)->ReleaseShortArrayElements(env, jTypeForm, cTypeForm, 0);
  (*env)->ReleaseIntArrayElements(env, jOutputOffsets, cOutputOffsets, 0);
  (*env)->ReleaseIntArrayElements(env, jInputOffsets, cInputOffsets, 0);
  (*env)->ReleaseIntArrayElements(env, jResultValues, cResultValues, 0);

  return successful? JNI_TRUE: JNI_FALSE;
}
