package com.hva.appname.data.text

import com.hva.appname.data.text.network.TextService
import com.hva.appname.domain.text.data.TextRepository

class RemoteTextRepository(private val textService: TextService) : TextRepository {

    override fun getText(): String {
        return textService.fetchText()
    }
}
