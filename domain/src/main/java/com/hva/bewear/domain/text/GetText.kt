package com.hva.bewear.domain.text

import com.hva.bewear.domain.text.data.TextRepository

class GetText(private val textRepository: TextRepository) {

    operator fun invoke(): String {
        return textRepository.getText()
    }
}
