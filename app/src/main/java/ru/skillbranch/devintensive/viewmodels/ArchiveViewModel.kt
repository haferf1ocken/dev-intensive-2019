package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class ArchiveViewModel : ViewModel() {
    private val chatRepository = ChatRepository
    private val chats = chatRepository.loadChats()
    private val archiveChats = Transformations.map(chats){ chats ->
        return@map chats.filter { it.isArchived }
            .map { it.toChatItem() }
            .sortedBy { it.id }
    }

    fun getChatData(): LiveData<List<ChatItem>> = archiveChats

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }
}