#ifndef _Included_DawgJni
#define _Included_DawgJni

//#include "dawgdic/base-types.h"
#include "dawgdic/dawg-builder.h"
#include "dawgdic/dictionary-builder.h"

void JniMapPut(JNIEnv *jenv, jobject, char *, dawgdic::ValueType);

#endif
