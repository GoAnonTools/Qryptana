import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.CompositionLocalKt;
import chat.simplex.common.views.helpers.CollapsingAppBarKt;
import androidx.compose.ui.awt.ComposePanel;
import chat.simplex.common.model.*;
import chat.simplex.common.ui.theme.ThemeKt;
import chat.simplex.common.views.helpers.AnimatedViewState;
import chat.simplex.common.views.usersettings.SettingsViewKt;
import chat.simplex.common.views.onboarding.WhatsNewViewKt;
import chat.simplex.common.views.chatlist.ChatListViewKt;
import kotlinx.coroutines.flow.StateFlowKt;

// Diagnostic-only rendering of production composables, never part of the distribution.
// Uses upstream sample data in memory. Does not start the chat engine or open a database.
public class PromotionPreview {
  public static void main(String[] args) {
    String screen = args[0];
    System.load(System.getProperty("compose.application.resources.dir") + "/libapp-lib.so");
    chat.simplex.common.platform.CoreKt.initHS();
    var prefs = ChatController.INSTANCE.getAppPrefs();
    prefs.getOneHandUI().getSet().invoke(false);
    prefs.getOneHandUICardShown().getSet().invoke(true);
    prefs.getAddressCreationCardShown().getSet().invoke(true);
    // Explicitly leave investment dismissal false: absence must not depend on a dismissed banner.
    prefs.getGetStakeBannerDismissed().getSet().invoke(false);
    ChatModel.INSTANCE.getCurrentUser().setValue(User.Companion.getSampleData());
    ChatModel.INSTANCE.getChatRunning().setValue(false);
    WhatsNewViewKt.setLastVersionDefault(ChatModel.INSTANCE);
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Qryptana");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.addWindowListener(new WindowAdapter() {
        public void windowClosed(WindowEvent e) { System.exit(0); }
      });
      ComposePanel panel = new ComposePanel();
      panel.setBackground(new Color(0x181715));
      panel.setContent((outer, outerChanged) -> {
        var bar = CollapsingAppBarKt.rememberAppBarHandler(null, null, true, outer, 0, 7);
        CompositionLocalKt.CompositionLocalProvider(CollapsingAppBarKt.getLocalAppBarHandler().provides(bar), (composer, changed) -> {
        if (screen.equals("settings")) ThemeKt.SimpleXTheme(null, (c, flags) -> {
          Function2<Composer, Integer, Unit> content = (cc, ff) -> { SettingsViewKt.PreviewSettingsLayout(cc, 0); return Unit.INSTANCE; };
          try {
            var surface = java.util.Arrays.stream(androidx.compose.material.SurfaceKt.class.getMethods())
              .filter(m -> m.getName().startsWith("Surface-") && m.getParameterCount() == 10).findFirst().orElseThrow();
            surface.invoke(null, null, null, 0L, 0L, null, 0f, content, c, 0, 63);
          } catch (ReflectiveOperationException e) { throw new RuntimeException(e); }
          return Unit.INSTANCE;
        }, composer, 0, 1);
        else if (screen.equals("whatsnew")) WhatsNewViewKt.PreviewWhatsNewView(composer, 0);
        else ThemeKt.SimpleXTheme(null, (c, flags) -> {
          ChatListViewKt.ChatListView(ChatModel.INSTANCE,
            StateFlowKt.MutableStateFlow(AnimatedViewState.GONE), ignored -> Unit.INSTANCE, true, c, 0);
          return Unit.INSTANCE;
        }, composer, 0, 1);
        return Unit.INSTANCE;
      }, outer, 0);
        return Unit.INSTANCE;
      });
      frame.setContentPane(panel);
      frame.setSize(screen.equals("chatlist") ? 440 : 760, 850);
      frame.setLocation(0, 0);
      frame.setVisible(true);
    });
  }
}
