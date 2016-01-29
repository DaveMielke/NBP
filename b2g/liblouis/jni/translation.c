#include "lljni.h"

typedef enum {
  RVI_INPUT_LENGTH,
  RVI_OUTPUT_LENGTH,
  RVI_CURSOR_OFFSET,
} ResultValuesIndex;

JAVA_METHOD(
  org_liblouis_Translation, translate, jboolean,
  jstring jTableName,
  jstring jInputBuffer, jcharArray jOutputBuffer, jbyteArray jTypeForm,
  jintArray jOutputOffsets, jintArray jInputOffsets,
  jintArray jResultValues, jboolean backTranslate
) {
  const char *cTableName = (*env)->GetStringUTFChars(env, jTableName, NULL);

  const jchar *cInputBuffer = (*env)->GetStringChars(env, jInputBuffer, NULL);
  jchar *cOutputBuffer = (*env)->GetCharArrayElements(env, jOutputBuffer, NULL);

  int haveTypeForm = jTypeForm != NULL;
  jbyte *cTypeForm = haveTypeForm? (*env)->GetByteArrayElements(env, jTypeForm, NULL): NULL;

  jint *cOutputOffsets = (*env)->GetIntArrayElements(env, jOutputOffsets, NULL);
  jint *cInputOffsets = (*env)->GetIntArrayElements(env, jInputOffsets, NULL);

  jint *cResultValues = (*env)->GetIntArrayElements(env, jResultValues, NULL);
  jint *inputLength  = &cResultValues[RVI_INPUT_LENGTH];
  jint *outputLength = &cResultValues[RVI_OUTPUT_LENGTH];
  jint *cursorOffset = &cResultValues[RVI_CURSOR_OFFSET];

  if (*cursorOffset < 0) cursorOffset = NULL;
  int translationMode = backTranslate? (0): (dotsIO | ucBrl);

  char *spacing = NULL;

  int successful =
    ((backTranslate != JNI_FALSE)? lou_backTranslate: lou_translate)(
      cTableName,
      cInputBuffer, inputLength,
      cOutputBuffer, outputLength,
      (unsigned char *)cTypeForm, spacing,
      cOutputOffsets, cInputOffsets,
      cursorOffset, translationMode
    );

  (*env)->ReleaseStringUTFChars(env, jTableName, cTableName);
  (*env)->ReleaseStringChars(env, jInputBuffer, cInputBuffer);
  (*env)->ReleaseCharArrayElements(env, jOutputBuffer, cOutputBuffer, 0);
  if (haveTypeForm) (*env)->ReleaseByteArrayElements(env, jTypeForm, cTypeForm, JNI_ABORT);
  (*env)->ReleaseIntArrayElements(env, jOutputOffsets, cOutputOffsets, 0);
  (*env)->ReleaseIntArrayElements(env, jInputOffsets, cInputOffsets, 0);
  (*env)->ReleaseIntArrayElements(env, jResultValues, cResultValues, 0);

  return successful? JNI_TRUE: JNI_FALSE;
}
