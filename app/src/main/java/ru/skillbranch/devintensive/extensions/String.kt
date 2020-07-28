package ru.skillbranch.devintensive.extensions

fun String.truncate(truncationSize: Int = 16): String {
    return if (this.length > truncationSize) {
        val subString = this.substring(0, truncationSize)
        if (subString.last().isWhitespace()) {
            val newString = subString.dropLastWhile { it.isWhitespace() }
            val whiteSpaces = subString.takeLastWhile { it.isWhitespace() }

            if (whiteSpaces.length < 2) {
                newString.plus("...")
            } else {
                newString
            }
        } else {
            subString.plus("...")
        }
    } else {
        this
    }
}