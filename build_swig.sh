#!/bin/bash
set -v

#if [ "$1" == "-c" ]; then
#    echo Clone swig
#    git clone --depth=1 https://github.com/swig/swig.git
#    shift
#fi
if [ "$1" == "-d" ]; then
    echo install dependencies
    sudo apt-get -yqq install automake bison
    shift
fi
if swig -version |grep 'Version 4'; then
    echo Swig version 4 installed
    exit
fi
echo Installing SWIG
cd swig
git log -1
./autogen.sh
./configure --prefix=$PWD/dist
make
make install
