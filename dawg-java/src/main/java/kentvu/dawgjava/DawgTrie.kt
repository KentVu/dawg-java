package kentvu.dawgjava

import dawgswig.DawgSwig
import kotlinx.coroutines.channels.Channel
import java.io.File
import java.net.URL
import java.nio.file.Files


private val String.size: Int
    get() = toByteArray().size

// TODO: Stop estimating and add a parameter (May Android won't change the line ending convention :) )
const val ESTIMATED_LINEBREAK_SIZE = 1 // assume linux/mac line endings. (\r, \n only)

//@file:JvmName("DawgTrie")
class DawgTrie private constructor(private val dawgSwig: DawgSwig): Trie {
    companion object {
        init {
            val libName = "dawg-jni"
            try {
                // based on https://stackoverflow.com/a/49500154/1562087
                val mappedLibName = System.mapLibraryName(libName) // The name of the file in resources/ dir
                val url: URL = DawgTrie::class.java.getResource("/$mappedLibName")
                val tmpDir = Files.createTempDirectory("my-native-lib").toFile()
                tmpDir.deleteOnExit()
                val nativeLibTmpFile = File(tmpDir, mappedLibName)
                nativeLibTmpFile.deleteOnExit()
                url.openStream().use { `in` ->
                    Files.copy(`in`, nativeLibTmpFile.toPath())
                }
                System.load(nativeLibTmpFile.absolutePath)
            } catch (e: Exception) {
                println("Please ignore the following Load error!")
                e.printStackTrace()
                System.loadLibrary(libName)
            }
        }

        /**
         * @param progressListener Listen to progress by bytes read.
         */
        suspend fun build(
            persistFilePath: String = "test.dawg",
            seed: Sequence<String>,
            progressListener: Channel<Int>? = null
        ): DawgTrie {
            val dawgSwig = dawgswig.DawgSwig(persistFilePath)
            var count = 0
            seed.forEach {
                dawgSwig.Insert(it)
                count += it.size + ESTIMATED_LINEBREAK_SIZE /*the line ending*/
                progressListener?.send(count)
            }
            dawgSwig.Finish()
            progressListener?.close()
            return DawgTrie(dawgSwig)
        }

        fun load(
            persistFilePath: String
        ): DawgTrie {
            val dawgSwig = dawgswig.DawgSwig(persistFilePath)
            dawgSwig.Load()
            return DawgTrie(dawgSwig)
        }
    }

    override fun search(prefix: String): PrefixSearchResult {
        val result = mutableMapOf<String, Int>()
        val swigResult = dawgSwig.Search(prefix)
        for (swigEntry in swigResult) {
            result[swigEntry.key] = swigEntry.value
        }
        return result.toMap()
    }

    override fun contains(key: String): Boolean {
        return dawgSwig.Contains(key)
    }
}

object TrieFactory {
    suspend fun newTrie(
        seed: Sequence<String>,
        progressListener: Channel<Int>? = null,
        filePath: String? = null
    ): Trie {
        return if (filePath == null)
            DawgTrie.build(seed = seed, progressListener = progressListener)
        else DawgTrie.build(persistFilePath = filePath, seed = seed, progressListener = progressListener)
    }

    fun newTrieFromFile(filePath: String): Trie {
        return DawgTrie.load(filePath)
    }
}

