package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int {
        return messages.count { !it.isReaded }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? {
        return  messages.lastOrNull()?.date
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String, String?> =
        when (val lastMessage = messages.lastOrNull()) {
            is TextMessage -> lastMessage.text.orEmpty() to lastMessage.from.firstName
            is ImageMessage -> "${lastMessage.from.firstName} - отправил фото" to null
            null -> "" to null
            else -> error("not expected message type")
        }

    private fun isSingle(): Boolean = members.size == 1

    companion object {
        fun archivedToChatItem(chats: List<Chat>): ChatItem {
            val lastChat =
                if (chats.none { it.unreadableMessageCount() > 0 }) chats.last()
                else chats.filter { it.unreadableMessageCount() > 0 }
                    .maxBy { it.lastMessageDate()!! }!!

            return ChatItem(
                id = "-1",
                initials = "",
                title = "",
                avatar = null,
                shortDescription = lastChat.lastMessageShort().first,
                lastMessageDate = lastChat.lastMessageDate()?.shortFormat(),
                messageCount = chats.sumBy { it.unreadableMessageCount() },
                chatType = ChatType.ARCHIVE,
                author = lastChat.lastMessageShort().second
            )
        }
    }

    fun toChatItem(): ChatItem {
        return if (isSingle()) {
            val user = members.first()
            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                "${user.firstName ?: ""} ${user.lastName ?: ""}",
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                user.isOnline
            )
        } else {
            ChatItem(
                id,
                null,
                "",
                title,
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                false,
                ChatType.GROUP,
                lastMessageShort().second
            )
        }
    }
}

enum class ChatType{
    SINGLE,
    GROUP,
    ARCHIVE
}



