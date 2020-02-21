/* File: dawg.i */
%module DawgSwigMdl
%inline %{
#include "DawgJni.h"
#include <fstream>

class DawgSwig
{
std::string filename;
dawgdic::DawgBuilder dawg_builder;
public:
    DawgSwig(char* filename): filename(filename) {
        //string tmp
        //DawgSwig::filename(filename);
        printf("new DawgSwig for %s\n", filename);

    }
    void Insert(char* word) {
        printf("Insert %s\n", word);
        // Inserts keys into a simple dawg.
        dawg_builder.Insert(word);
    }
    void Finish() {
        printf("Finish()\n");
        // Finishes building a simple dawg.
        dawgdic::Dawg dawg;
        dawg_builder.Finish(&dawg);

        // Builds a dictionary from a simple dawg.
        dawgdic::Dictionary dic;
        dawgdic::DictionaryBuilder::Build(dawg, &dic);

        // Writes a dictionary into a file "test.dic".
        std::ofstream dic_file(/* new std::string */(filename), std::ios::binary);
        dic.Write(&dic_file);
    }
};
//DawgSwig newDawgSwig();
%}
%immutable;
//dawgdic::DawgBuilder newDawgBuilder();
