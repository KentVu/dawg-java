/* File: dawg.i */
%module DawgSwigMdl
%inline %{
#include "DawgJni.h"
class DawgSwig
{

public:
    DawgSwig() {
        printf("new DawgSwig\n");
    }

};
//DawgSwig newDawgSwig();
%}
%immutable;
//dawgdic::DawgBuilder newDawgBuilder();
