package kentvu.dawgjava

interface WordSequence {
    operator fun iterator(): Iterator<String>

    private class DefaultWordSequence(private val s: String) : WordSequence {
        override fun iterator(): Iterator<String> {
            return s.lineSequence().iterator()
        }

    }

    companion object {
        fun new(s: String): WordSequence = DefaultWordSequence(s)
    }
}
