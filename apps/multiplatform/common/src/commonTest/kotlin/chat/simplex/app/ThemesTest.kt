package chat.simplex.app

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import chat.simplex.common.ui.theme.*
import kotlin.test.Test
import kotlin.test.assertEquals

// use this command for testing:
// ./gradlew desktopTest
class ThemesTest {
  @Test
  fun desktopAlwaysSelectsBlackButMobileHonorsPreferences() {
    for (saved in listOf("LIGHT", "DARK", "SIMPLEX", "BLACK", "SYSTEM")) {
      for (systemDark in listOf(false, true)) {
        for (savedDark in listOf("DARK", "SIMPLEX", "BLACK")) {
          assertEquals("BLACK", ThemeDefaults.themeName(true, saved, systemDark, savedDark))
          val mobile = if (saved != "SYSTEM") saved else if (systemDark) savedDark else "LIGHT"
          assertEquals(mobile, ThemeDefaults.themeName(false, saved, systemDark, savedDark))
        }
      }
    }
    val desktop = ThemeDefaults.blackPalette(true)
    assertEquals(Color(0xFFD9A878), desktop.primary)
    assertEquals(Color(0xFFC4844A), desktop.primaryVariant)
    assertEquals(Color(0xFF0B0B0A), desktop.background)
    assertEquals(Color(0xFF131211), desktop.surface)
    assertEquals(Color(0xFFEDE8E2), desktop.onSurface)
    assertEquals(false, desktop.isLight)
  }

  @Test
  fun mobilePalettesMatchUpstreamBeforeRebrand() {
    // Independent expected values from 423c8c0's Color.kt / Theme.kt, including
    // Material lightColors/darkColors defaults, not the current shared aliases.
    val palettes = listOf(
      ThemeDefaults.lightPalette(false) to ThemeDefaults.lightAppPalette(false),
      ThemeDefaults.darkPalette(false) to ThemeDefaults.darkAppPalette(false),
      ThemeDefaults.simplexPalette(false) to ThemeDefaults.simplexAppPalette(false),
      ThemeDefaults.blackPalette(false) to ThemeDefaults.blackAppPalette(false),
    )
    val expected = listOf(
      listOf(0xFF0088FF, 0xFF0088FF, 0xFF8B8786, 0xFFF1F2F6, 0xFFFFFFFF, 0xFFFFFFFF, 0xFF000000, 0xFF000000,
        0xFF0088FF, 0xFFE9F7FF, 0xFFE9F7FF, 0xFFD6F0FF, 0xFFF5F5F6, 0xFFECECEE),
      listOf(0xFF0088FF, 0xFF0088FF, 0xFF8B8786, 0xFF2B2C2E, 0xFF121212, 0xFF222222, 0xFFFFFBFA, 0xFFFFFBFA,
        0xFF0088FF, 0xFF18262E, 0xFF18262E, 0xFF1D3847, 0xFF262627, 0xFF373739),
      listOf(0xFF70F0F9, 0xFF1298A5, 0xFF8B8786, 0xFF2C464D, 0xFF111528, 0xFF121C37, 0xFFFFFFFF, 0xFFFFFFFF,
        0xFF267BE5, 0xFF172941, 0xFF172941, 0xFF1C3A57, 0xFF25283A, 0xFF36394A),
      listOf(0xFF0077E0, 0xFF0077E0, 0xFF8B8786, 0xFF2B2C2E, 0xFF070707, 0xFF161617, 0xFFFFFBFA, 0xFFFFFBFA,
        0xFF0077E0, 0xFF243747, 0xFF18262E, 0xFF1D3847, 0xFF1B1B1B, 0xFF29292B),
    )
    palettes.forEachIndexed { index, (colors, app) ->
      assertEquals(expected[index].map { Color(it) }, paletteValues(colors, app), "Mobile palette $index")
      assertEquals(index == 0, colors.isLight)
      assertEquals(Color.Red, colors.error)
    }
  }

  private fun paletteValues(c: Colors, a: AppColors) = listOf(
    c.primary, c.primaryVariant, c.secondary, c.secondaryVariant, c.background, c.surface, c.onBackground, c.onSurface,
    a.title, a.primaryVariant2, a.sentMessage, a.sentQuote, a.receivedMessage, a.receivedQuote,
  )

  @Test
  fun testSkipDuplicates() {
    val r = ArrayList<ThemeOverrides>()
    r.add(ThemeOverrides("UUID", DefaultTheme.DARK))
    r.add(ThemeOverrides("UUID", DefaultTheme.DARK))
    r.add(ThemeOverrides("UUID", DefaultTheme.LIGHT))
    r.add(ThemeOverrides("UUID2", DefaultTheme.DARK))
    r.add(ThemeOverrides("UUID3", DefaultTheme.LIGHT, wallpaper = ThemeWallpaper()))
    r.add(ThemeOverrides("UUID4", DefaultTheme.LIGHT, wallpaper = null))
    r.add(ThemeOverrides("UUID5", DefaultTheme.LIGHT, wallpaper = ThemeWallpaper(preset = "something")))
    r.add(ThemeOverrides("UUID5", DefaultTheme.LIGHT, wallpaper = ThemeWallpaper(preset = "something2")))
    r.add(ThemeOverrides("UUID6", DefaultTheme.LIGHT, wallpaper = ThemeWallpaper(preset = "something2")))
    r.add(ThemeOverrides("UUID7", DefaultTheme.DARK, wallpaper = ThemeWallpaper(preset = "something2")))
    r.add(ThemeOverrides("UUID8", DefaultTheme.DARK, wallpaper = ThemeWallpaper(imageFile = "image")))
    r.add(ThemeOverrides("UUID9", DefaultTheme.DARK, wallpaper = ThemeWallpaper(imageFile = "image2")))
    r.add(ThemeOverrides("UUID10", DefaultTheme.LIGHT, wallpaper = ThemeWallpaper(imageFile = "image")))
    assertEquals(
      r.skipDuplicates(), listOf(
        ThemeOverrides("UUID", DefaultTheme.DARK),
        ThemeOverrides("UUID3", DefaultTheme.LIGHT, wallpaper = ThemeWallpaper()),
        ThemeOverrides("UUID5", DefaultTheme.LIGHT, wallpaper = ThemeWallpaper(preset = "something")),
        ThemeOverrides("UUID6", DefaultTheme.LIGHT, wallpaper = ThemeWallpaper(preset = "something2")),
        ThemeOverrides("UUID7", DefaultTheme.DARK, wallpaper = ThemeWallpaper(preset = "something2")),
        ThemeOverrides("UUID8", DefaultTheme.DARK, wallpaper = ThemeWallpaper(imageFile = "image")),
        ThemeOverrides("UUID10", DefaultTheme.LIGHT, wallpaper = ThemeWallpaper(imageFile = "image"))
      )
    )
  }
}
