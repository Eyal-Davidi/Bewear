package com.hva.hva_bewear.data.text

import com.hva.hva_bewear.data.text.network.TextService
import com.hva.hva_bewear.domain.text.data.TextRepository

class RemoteTextRepository(private val textService: TextService) : TextRepository {

    override fun getText(): String {
        return textService.fetchText()
    }
}
