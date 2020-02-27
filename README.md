![Android CI](https://github.com/KentVu/dawg-java/workflows/Android%20CI/badge.svg?branch=master)

dawg-java
=========

Port of [dawgdic](https://code.google.com/p/dawgdic/) C++ library to Java.

Build
=====

Run: `./gradlew build`.

If you encounter `UnsatisfiedLinkError` when run test from Android Studio:

Open `Edit Configuration` -> `Templates` -> `Android JUnit`, under `VM Options` change to:
```
-ea -Djava.library.path=../dawg-jni/build/lib/main/debug
```

Make sure `Working directory` is still `$MODULE_DIR$`.

References
==========

- https://docs.gradle.org/current/userguide/building_cpp_projects.html
- https://github.com/vladsoroka/GradleJniSample
- https://www.jetbrains.com/help/idea/setting-up-jni-development-in-gradle-project.html
- https://gist.github.com/santa4nt/4a8fd626335e36c94356 Sample JNI/C++ HelloWorld
- https://developer.android.com/training/articles/perf-jni RegisterNatives

License
=======

Wrapper code is licensed under MIT License.
Bundled [dawgdic](https://code.google.com/p/dawgdic/) C++ library is licensed under BSD license.
Bundled [libb64](http://libb64.sourceforge.net/) is Public Domain.
