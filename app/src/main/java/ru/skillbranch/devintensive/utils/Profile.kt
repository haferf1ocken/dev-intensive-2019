package ru.skillbranch.devintensive.utils

data class Profile(
    val rating: Int = 0,
    val respect: Int = 0,
    val firstName: String,
    val lastName: String,
    val about: String,
    val repository: String) {

    private val fullName = if (firstName.isEmpty() or lastName.isEmpty())
        firstName.plus(lastName) else firstName.plus(" ").plus(lastName)

    private val nickname = Utils.transliteration(fullName, "_")
    private val rank: String = "Junior Android Developer"

    fun toMap(): Map<String, Any> = mapOf(
        "nickname" to nickname,
        "rank" to rank,
        "rating" to rating,
        "respect" to respect,
        "firstName" to firstName,
        "lastName" to lastName,
        "about" to about,
        "repository" to repository
    )
}