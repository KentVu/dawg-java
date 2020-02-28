package kentvu.dawgjava

import kotlinx.coroutines.channels.Channel
import java.io.File
import java.net.URL
import java.nio.file.Files


private val String.size: Int
    get() = toByteArray().size

// TODO: Stop estimating and add a parameter (May Android won't change the line ending convention :) )
const val ESTIMATED_LINEBREAK_SIZE = 1 // assume linux/mac line endings. (\r, \n only)

//@file:JvmName("DawgTrie")
class DawgTrie: Trie {
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
                System.loadLibrary(libName)
            }
        }
    }

    val dawgSwig = dawgswig.DawgSwig("test.dawg")

    override suspend fun build(seed: Sequence<String>, progressListener: Channel<Int>?) {
        var count = 0
        seed.forEach {
            dawgSwig.Insert(it)
            count += it.size + ESTIMATED_LINEBREAK_SIZE /*the line ending*/
            progressListener?.send(count)
        }
        dawgSwig.Finish()
        progressListener?.close()
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
    fun newTrie(): Trie {
        return DawgTrie()
    }
}

