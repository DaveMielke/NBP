#include "lljni.h"

JAVA_METHOD(
  org_liblouis_Translation, translate, jboolean,
  jstring jTableName, jstring jInputBuffer, jcharArray jOutputBuffer,
  jintArray jOutputOffsets, jintArray jInputOffsets,
  jintArray jResultValues, jboolean backTranslate
) {
  const char *cTableName = (*env)->GetStringUTFChars(env, jTableName, NULL);
  const jchar *cInputBuffer = (*env)->GetStringChars(env, jInputBuffer, NULL);
  jchar *cOutputBuffer = (*env)->GetCharArrayElements(env, jOutputBuffer, NULL);
  jint *cOutputOffsets = (*env)->GetIntArrayElements(env, jOutputOffsets, NULL);
  jint *cInputOffsets = (*env)->GetIntArrayElements(env, jInputOffsets, NULL);
  jint *cResultValues = (*env)->GetIntArrayElements(env, jResultValues, NULL);

  jint *inputLength  = &cResultValues[0];
  jint *outputLength = &cResultValues[1];
  jint *cursorOffset = &cResultValues[2];

  if (*cursorOffset < 0) cursorOffset = NULL;
  int translationMode = backTranslate? (0): (dotsIO | ucBrl);

  unsigned char *typeForm = NULL;
  char *spacing = NULL;

  int successful =
    ((backTranslate != JNI_FALSE)? lou_backTranslate: lou_translate)(
      cTableName, cInputBuffer, inputLength, cOutputBuffer, outputLength,
      typeForm, spacing, cOutputOffsets, cInputOffsets, cursorOffset,
      translationMode
    );

  (*env)->ReleaseStringUTFChars(env, jTableName, cTableName);
  (*env)->ReleaseStringChars(env, jInputBuffer, cInputBuffer);
  (*env)->ReleaseCharArrayElements(env, jOutputBuffer, cOutputBuffer, 0);
  (*env)->ReleaseIntArrayElements(env, jOutputOffsets, cOutputOffsets, 0);
  (*env)->ReleaseIntArrayElements(env, jInputOffsets, cInputOffsets, 0);
  (*env)->ReleaseIntArrayElements(env, jResultValues, cResultValues, 0);

  return successful? JNI_TRUE: JNI_FALSE;
}
