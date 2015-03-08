#define PACKAGE_PATH "org.nbp.b2g.input"
#define MAKE_LOG_TAG(component) static const char LOG_TAG[] = PACKAGE_PATH ".JNI." component
#define MAKE_FILE_LOG_TAG MAKE_LOG_TAG(__FILE__)

extern void logSystemError (const char *tag, const char *action);

extern int executeHostCommand (const char *command);

extern int isWritable (const char *path);
extern int makeWritable (const char *path);
