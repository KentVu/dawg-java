package kentvu.dawgjava

import java.io.File

class JniDawg: Dawg {
    companion object {
        init {
            System.loadLibrary("dawg-jni")
        }
    }
    external fun saveDawg(inFile: String)

    override fun insert(s: String) {
        words.add(s)
    }

    override fun save() {
        System.out.println(words)
        val filename = "TestDawg.txt"
        File(filename).bufferedWriter().use { out ->
            words.forEach {
                out.write(it)
                out.newLine()
            }
        }
        saveDawg(filename)
    }

    private val words = sortedSetOf<String>()
}

interface Dawg {
    fun insert(s: String)
    fun save()
}
//public interface Dawg {
//    void insert(String s);
//    void save();
//}
