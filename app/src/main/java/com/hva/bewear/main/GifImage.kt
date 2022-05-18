package com.hva.bewear.main

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ComponentRegistry
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size

object GifImage {
    @Composable
    operator fun invoke(
        modifier: Modifier = Modifier,
        imageID: Int
    ) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components(fun ComponentRegistry.Builder.() {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            })
            .build()
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = imageID)
                    .apply(block = fun ImageRequest.Builder.() {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = modifier
        )
    }
}