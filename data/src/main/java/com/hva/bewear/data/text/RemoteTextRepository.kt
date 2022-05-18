package com.hva.bewear.data.text

import com.hva.bewear.data.text.network.TextService
import com.hva.bewear.domain.text.data.TextRepository

class RemoteTextRepository(private val textService: TextService) : TextRepository {

    override fun getText(): String {
        return textService.fetchText()
    }
}
