package ru.skillbranch.devintensive

import org.junit.Test

import org.junit.Assert.*
import ru.skillbranch.devintensive.extensions.*
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.data.User
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_user_factory() {
        val user = User.makeUser("John Cena")
        val user2 = User.makeUser("John")
        println("$user\n$user2")
    }

    @Test
    fun test_parse_name() {
        println(Utils.parseFullName(null))
        println(Utils.parseFullName(""))
        println(Utils.parseFullName(" "))
        println(Utils.parseFullName("John"))
    }

    @Test
    fun test_date_format() {
        println(Date().format())
        println(Date().format("HH:mm"))
    }

    @Test
    fun test_add_date() {
        println(Date().add(2, TimeUnits.SECOND))
        println(Date().add(-4, TimeUnits.DAY))
    }

    @Test
    fun test_initials() {
        println(Utils.toInitials("john", "doe"))
        println(Utils.toInitials("John", null))
        println(Utils.toInitials(null, null))
        println(Utils.toInitials(" ", ""))
    }

    @Test
    fun test_transliteration() {
        println(Utils.transliteration("Женя Стереотипов"))
        println(Utils.transliteration("Amazing Петр", "_"))
    }

    @Test
    fun test_date_humanize() {
        println(Date().add(-2, TimeUnits.HOUR).humanizeDiff())
        println(Date().add(-5, TimeUnits.DAY).humanizeDiff())
        println(Date().add(2, TimeUnits.MINUTE).humanizeDiff())
        println(Date().add(7, TimeUnits.DAY).humanizeDiff())
        println(Date().add(-400, TimeUnits.DAY).humanizeDiff())
        println(Date().add(400, TimeUnits.DAY).humanizeDiff())
    }

    @Test
    fun test_user_builder() {
        val user = User.Builder().id("0")
            .firstName("John")
            .lastName("Wick")
            .avatar("Cool John.img")
            .rating(100)
            .respect(100)
            .lastVisit(Date())
            .isOnline(true)
            .build()

        println(user)
    }

    @Test
    fun test_plural() {
        println("""
            ${TimeUnits.SECOND.plural(1)}
            ${TimeUnits.MINUTE.plural(4)}
            ${TimeUnits.HOUR.plural(19)}
            ${TimeUnits.DAY.plural(222)}
        """.trimIndent())
    }

    @Test
    fun test_truncation() {
        println("Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate())
        println("Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»"
            .truncate(15))
        println("A     ".truncate(3))
    }

    @Test
    fun test_split() {
        val string = "https://www.github.com/zloysergunya/tree"
        val pathCount = string.split(Regex("/[^/]"))
        print(pathCount.size)
    }
}