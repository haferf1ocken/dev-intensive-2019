package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel() {

    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = chatRepository.loadChats()
    private val chatItems = Transformations.map(chats){chats ->
        val result: MutableList<ChatItem> = mutableListOf()
        val sortedChats = chats.groupBy { it.isArchived }
        val archivedChats = sortedChats[true]
        if (!archivedChats.isNullOrEmpty()) result.add(Chat.archivedToChatItem(archivedChats))
        result.addAll((sortedChats[false] ?: error(""))
            .map { it.toChatItem() }
            .sortedBy { it.id.toInt() })
        return@map result.toList()
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chats = chatItems.value!!

            result.value = if (queryStr.isEmpty()) chats
            else chats.filter { it.title.contains(queryStr, ignoreCase = true) }
        }

        result.addSource(chatItems) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }
}