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
  fun androidPalettesMapSemanticRolesAndRemainDistinct() {
    val roles = listOf(
      QryptanaAndroidThemeRoles.light,
      QryptanaAndroidThemeRoles.dark,
      QryptanaAndroidThemeRoles.simplex,
      QryptanaAndroidThemeRoles.black,
    )
    val palettes = listOf(
      ThemeDefaults.lightPalette(false) to ThemeDefaults.lightAppPalette(false),
      ThemeDefaults.darkPalette(false) to ThemeDefaults.darkAppPalette(false),
      ThemeDefaults.simplexPalette(false) to ThemeDefaults.simplexAppPalette(false),
      ThemeDefaults.blackPalette(false) to ThemeDefaults.blackAppPalette(false),
    )
    palettes.zip(roles).forEachIndexed { index, (pair, role) ->
      val (colors, app) = pair
      assertEquals(role.actionText, colors.primary)
      assertEquals(role.filledControl, colors.primaryVariant)
      assertEquals(role.secondaryText, colors.secondary)
      assertEquals(role.structuralBorder, colors.secondaryVariant)
      assertEquals(role.background, colors.background)
      assertEquals(role.surface, colors.surface)
      assertEquals(role.body, colors.onBackground)
      assertEquals(role.body, colors.onSurface)
      assertEquals(role.error, colors.error)
      assertEquals(role.heading, app.title)
      assertEquals(role.sentMessage, app.sentMessage)
      assertEquals(role.receivedMessage, app.receivedMessage)
      assertEquals(index == 0, colors.isLight)
    }
    assertEquals(4, palettes.map { paletteValues(it.first, it.second) }.distinct().size)
  }

  @Test
  fun androidSemanticRolesMeetEnabledContrastTargets() {
    listOf(
      QryptanaAndroidThemeRoles.light,
      QryptanaAndroidThemeRoles.dark,
      QryptanaAndroidThemeRoles.simplex,
      QryptanaAndroidThemeRoles.black,
    ).forEach { role ->
      assertContrastAtLeast(role.actionText, role.background, 4.5, "action/background")
      assertContrastAtLeast(role.heading, role.background, 4.5, "heading/background")
      assertContrastAtLeast(role.body, role.background, 4.5, "body/background")
      assertContrastAtLeast(role.secondaryText, role.background, 4.5, "secondary/background")
      assertContrastAtLeast(role.actionText, role.surface, 4.5, "action/surface")
      assertContrastAtLeast(role.onFilledControl, role.filledControl, 4.5, "filled-control foreground")
      assertContrastAtLeast(role.neutralIcon, role.surface, 3.0, "neutral icon/surface")
      assertContrastAtLeast(role.structuralBorder, role.surface, 3.0, "border/surface")
      assertContrastAtLeast(role.error, role.background, 4.5, "error/background")
      kotlin.test.assertNotEquals(role.error, role.actionText)
      kotlin.test.assertNotEquals(SimplexGreen, role.actionText)
      kotlin.test.assertNotEquals(WarningOrange, role.actionText)
    }
  }

  @Test
  fun androidApprovedLightDarkTokensAreStable() {
    val light = QryptanaAndroidThemeRoles.light
    assertEquals(Color(0xFFF1F3F5), light.background)
    assertEquals(Color(0xFFFCFDFE), light.surface)
    assertEquals(Color(0xFF20262B), light.heading)
    assertEquals(Color(0xFF30383F), light.body)
    assertEquals(Color(0xFF59636C), light.secondaryText)
    val dark = QryptanaAndroidThemeRoles.dark
    assertEquals(Color(0xFF0E1114), dark.background)
    assertEquals(Color(0xFF191E23), dark.surface)
    assertEquals(Color(0xFF20272D), dark.raisedSurface)
    assertEquals(Color(0xFFEEF1F3), dark.heading)
    assertEquals(Color(0xFF9AA5AE), dark.secondaryText)
    assertEquals(Color(0xFFAEB8C0), dark.neutralIcon)
    assertEquals(Color(0xFF6A3E32), dark.copperShadow)
    assertEquals(Color(0xFFAD7257), dark.copperBody)
    assertEquals(Color(0xFFF0D4C4), dark.copperHighlight)
  }

  private fun assertContrastAtLeast(foreground: Color, background: Color, minimum: Double, role: String) {
    val ratio = contrastRatio(foreground, background)
    kotlin.test.assertTrue(ratio >= minimum, "$role contrast $ratio is below $minimum")
  }

  private fun contrastRatio(a: Color, b: Color): Double {
    val l1 = relativeLuminance(a)
    val l2 = relativeLuminance(b)
    return (maxOf(l1, l2) + 0.05) / (minOf(l1, l2) + 0.05)
  }

  private fun relativeLuminance(color: Color): Double {
    fun linear(channel: Float): Double {
      val c = channel.toDouble()
      return if (c <= 0.04045) c / 12.92 else Math.pow((c + 0.055) / 1.055, 2.4)
    }
    return 0.2126 * linear(color.red) + 0.7152 * linear(color.green) + 0.0722 * linear(color.blue)
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
