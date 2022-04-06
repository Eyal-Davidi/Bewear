package com.hva.appname.domain.text

import com.hva.appname.domain.text.data.TextRepository

class GetText(private val textRepository: TextRepository) {

    operator fun invoke(): String {
        return textRepository.getText()
    }
}
