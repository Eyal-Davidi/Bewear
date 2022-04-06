package com.hva.hva_bewear.domain.text

import com.hva.hva_bewear.domain.text.data.TextRepository

class GetText(private val textRepository: TextRepository) {

    operator fun invoke(): String {
        return textRepository.getText()
    }
}
