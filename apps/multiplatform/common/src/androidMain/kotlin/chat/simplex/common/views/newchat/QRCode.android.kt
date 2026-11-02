package chat.simplex.common.views.newchat

import androidx.compose.ui.graphics.*

actual fun ImageBitmap.replaceColor(from: Int, to: Int): ImageBitmap {
  val pixels = IntArray(width * height)
  val bitmap = this.asAndroidBitmap()
  bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
  var i = 0
  while (i < pixels.size) {
    if (pixels[i] == from) {
      pixels[i] = to
    }
    i++
  }
  bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
  return bitmap.asImageBitmap()
}

actual fun ImageBitmap.replaceColorWithVerticalGradient(from: Int, top: Int, bottom: Int): ImageBitmap {
  val pixels = IntArray(width * height)
  val bitmap = this.asAndroidBitmap()
  bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
  for (y in 0 until height) {
    val to = lerpArgb(top, bottom, if (height <= 1) 0f else y.toFloat() / (height - 1))
    val row = y * width
    for (x in 0 until width) {
      val i = row + x
      if (pixels[i] == from) pixels[i] = to
    }
  }
  bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
  return bitmap.asImageBitmap()
}
