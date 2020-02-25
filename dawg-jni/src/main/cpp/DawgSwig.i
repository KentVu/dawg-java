/* File: dawg.i */
%module DawgSwigMdl
%inline %{
#include "DawgJni.h"
#include <fstream>

class SwigMap {
    char* []entries();
};

class DawgSwig
{
    std::string filename;
    dawgdic::DawgBuilder dawg_builder;
    dawgdic::Dictionary dic;

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
        dawgdic::DictionaryBuilder::Build(dawg, &dic);

        // Writes a dictionary into a file "test.dic".
        std::ofstream dic_file(filename, std::ios::binary);
        dic.Write(&dic_file);
    }
    bool Contains(char* key) {
        printf("Contains()\n");
        return dic.Contains(key);
    }
    SwigMap Search(char* prefix) {
        printf("Search() for %s\n", prefix);
    }
};
%}
%immutable;
//dawgdic::DawgBuilder newDawgBuilder();
