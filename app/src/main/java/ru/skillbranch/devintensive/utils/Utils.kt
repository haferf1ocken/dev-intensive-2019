package ru.skillbranch.devintensive.utils

import android.content.Context
import kotlin.math.roundToInt

object Utils {

    private val dictionary = mapOf(
        "а" to "a", "A" to "A",
        "б" to "b", "Б" to "B",
        "в" to "v", "В" to "V",
        "г" to "g", "Г" to "G",
        "д" to "d", "Д" to "D",
        "е" to "e", "Е" to "E",
        "ё" to "e", "Ё" to "E",
        "ж" to "zh", "Ж" to "Zh",
        "з" to "z", "З" to "Z",
        "и" to "i", "И" to "I",
        "й" to "i", "Й" to "I",
        "к" to "k", "К" to "K",
        "л" to "l", "Л" to "L",
        "м" to "m", "М" to "M",
        "н" to "n", "Н" to "N",
        "о" to "o", "О" to "O",
        "п" to "p", "П" to "P",
        "р" to "r", "Р" to "R",
        "с" to "s", "С" to "S",
        "т" to "t", "Т" to "T",
        "у" to "u", "У" to "U",
        "ф" to "f", "Ф" to "F",
        "х" to "h", "Х" to "H",
        "ц" to "c", "Ц" to "C",
        "ч" to "ch", "Ч" to "Ch",
        "ш" to "sh", "Ш" to "Sh",
        "щ" to "sh'", "Щ" to "Sh'",
        "ъ" to "",  "Ъ" to "",
        "ы" to "i", "Ы" to "I",
        "ь" to "", "Ь" to "",
        "э" to "e", "Э" to "E",
        "ю" to "yu", "Ю" to "Yu",
        "я" to "ya", "Я" to "Ya"
    )

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")

        val firstName = if (parts?.getOrNull(0).isNullOrBlank()) null else parts?.get(0)
        val lastName = if (parts?.getOrNull(1).isNullOrBlank()) null else parts?.get(1)

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        var nickname = ""

        payload.forEach {
            nickname += when {
                it == ' ' -> divider
                dictionary.containsKey(it.toString()) -> dictionary[it.toString()]
                else -> it
            }
        }
        return nickname
    }

    fun toInitials(firstName: String?, lastName: String?): String? {

        val firstLetter = firstName?.trim()?.firstOrNull()?.toUpperCase()
        val secondLetter = lastName?.trim()?.firstOrNull()?.toUpperCase()

        return when {
            firstLetter == null && secondLetter == null -> null
            firstLetter == null -> "$secondLetter"
            secondLetter == null -> "$firstLetter"
            else -> "$firstLetter$secondLetter"
        }
    }

    fun convertPxToDp(context: Context, px: Int): Int {
        return (px / context.resources.displayMetrics.density).roundToInt()
    }

    fun convertDpToPx(context: Context, dp: Float): Int {
        return (dp * context.resources.displayMetrics.density).roundToInt()
    }

    fun convertSpToPx(context: Context, sp: Int): Int {
        return sp * context.resources.displayMetrics.scaledDensity.roundToInt()
    }
}