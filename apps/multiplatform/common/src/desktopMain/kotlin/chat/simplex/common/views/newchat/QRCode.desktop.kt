package chat.simplex.common.views.newchat

import androidx.compose.ui.graphics.*

actual fun ImageBitmap.replaceColor(from: Int, to: Int): ImageBitmap {
  val image = toAwtImage()
  val pixels = IntArray(width * height)
  image.getRGB(0, 0, width, height, pixels, 0, image.width)
  var i = 0
  while (i < pixels.size) {
    if (pixels[i] == from) {
      pixels[i] = to
    }
    i++
  }
  image.setRGB(0, 0, width, height, pixels, 0, image.width)
  return image.toComposeImageBitmap()
}

actual fun ImageBitmap.replaceColorWithVerticalGradient(from: Int, top: Int, bottom: Int): ImageBitmap {
  val image = toAwtImage()
  val pixels = IntArray(width * height)
  image.getRGB(0, 0, width, height, pixels, 0, image.width)
  for (y in 0 until height) {
    val to = lerpArgb(top, bottom, if (height <= 1) 0f else y.toFloat() / (height - 1))
    val row = y * width
    for (x in 0 until width) {
      val i = row + x
      if (pixels[i] == from) pixels[i] = to
    }
  }
  image.setRGB(0, 0, width, height, pixels, 0, image.width)
  return image.toComposeImageBitmap()
}
