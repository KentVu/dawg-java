/* File: dawg.i */
%module DawgdicSwig
%inline %{
#include "DawgJni.h"
%}
%immutable;
void buildDawg(char* filename);
