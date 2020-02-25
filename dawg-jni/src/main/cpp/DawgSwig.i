/* File: dawg.i */
%module DawgSwigMdl
%{
//#include "dawgdic/dawg-builder.h"
%}
//%typemap(directorin, descriptor="Ljava/lang/String;") CharType "$input = (jint) $1;"
//%typemap(jni) size_t "jint"
//%typedef size_t jint;
%typemap(in) BaseType "jint"
%include "dawgdic/base-types.h"
%include "dawgdic/dawg.h"
%include "dawgdic/dawg-builder.h"
%include "dawgdic/dictionary.h"
%include "dawgdic/dictionary-builder.h"
%immutable;
//dawgdic::DawgBuilder newDawgBuilder();
