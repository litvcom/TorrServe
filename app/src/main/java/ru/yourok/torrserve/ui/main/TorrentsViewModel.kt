package ru.yourok.torrserve.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.yourok.torrserve.server.api.Api
import ru.yourok.torrserve.server.models.torrent.Torrent


class TorrentsViewModel : ViewModel() {
    private var isWork = false
    var data: MutableLiveData<List<Torrent>>? = null

    fun getData(): LiveData<List<Torrent>> {
        if (data == null) {
            data = MutableLiveData()
            update()
        }
        return data!!
    }

    override fun onCleared() {
        isWork = false
        super.onCleared()
    }

    private fun update() {
        viewModelScope.launch(Dispatchers.IO) {
            synchronized(isWork) {
                if (isWork)
                    return@launch
            }
            isWork = true
            while (isWork) {
                try {
                    val list = Api.listTorrent()
                    var diff = false
                    val oldList = data?.value ?: emptyList()
                    if (list.size != oldList.size)
                        diff = true
                    else
                        for (i in list.indices)
                            if (list[i] != oldList[i]) {
                                diff = true
                                break
                            }

                    if (diff)
                        withContext(Dispatchers.Main) { data?.value = list }
                    delay(1000)
                } catch (e: Exception) {
                    delay(2000)
                }
            }
        }
    }
}