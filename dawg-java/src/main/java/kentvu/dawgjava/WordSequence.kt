package kentvu.dawgjava

interface WordSequence {

    private class DefaultWordSequence(s: String) : WordSequence {

    }

    companion object {
        fun new(s: String): WordSequence = DefaultWordSequence(s)
    }
}
