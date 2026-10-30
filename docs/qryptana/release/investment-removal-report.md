# Investment promotion removal

Date: 2026-10-30. Implementation commit: `08eb12a16` on `qryptana-runtime-fixes`, starting from clean `6fc2b2c51`. The inherited investment promotion is disconnected from Qryptana's active desktop/Android and iOS UI. Privana remains documentation-only and hidden. Previous commits, Linux images/archives and Android APKs are preserved. No manual profile or its real contact/data was read, modified, automated or deleted.

## Focused trace and change

| Entry point | Previous path | Qryptana change |
|---|---|---|
| Desktop/Android chat list | ChatListView -> crowdfundingAvailable + dismissal preference -> GetStakeBanner -> openGetStake -> GetStakeView -> Wefunder / crowdfunding contact | Removed banner list item and its local state; no replacement item or spacer |
| Android onboarding cards | OnboardingCards retained unused GetStake/openGetStake/crowdfunding imports; actual onboarding branch already only rendered ConnectOnboardingView | Removed unused imports; no new route |
| Desktop/Android Settings | Investment section -> GetStakeView | Removed section including its divider and imports |
| Desktop/Android What's New | v7 feature -> InvestInSimpleXChatView -> GetStakeView; article link -> crowdfunding blog | Removed feature and campaign article link; removed its chat-list country prefetch |
| iOS onboarding/chat list | Both list branches -> GetStakeBanner -> openGetStake -> investment sheet -> GetStakeView | Removed both placements, sheet, opener and their view-local state |
| iOS Settings | Wefunder row -> GetStakeView | Removed investment section |
| iOS What's New | US investment feature -> InvestInSimpleXChat -> GetStakeView; crowdfunding article link | Removed feature and campaign article link |

The promotional destination was `https://wefunder.com/simplex.chat?utm_source=app`. Its view also offered a dedicated crowdfunding SimpleX contact link. No destination was renamed or redirected to Qryptana/Privana. What's New retains non-investment feature descriptions and legitimate upstream names/operator information.

Unreachable upstream investment view implementations, translations, images and stored preference definitions remain. In particular, bannerCard/BannerDismissButton and the corresponding Swift shared card chrome remain available to legitimate non-investment UI. No shared resource deletion, preference migration, blanket SimpleX rename, attribution/license edit, operator-info change or protocol/crypto change occurred. Themes, updater code/safeguards and package IDs are unchanged.

Future placement is recorded in [privana-sidebar.md](../decisions/privana-sidebar.md) and a source comment immediately after ChatListSearchBar in the desktop top-toolbar branch. It is **directly below the search bar in the left sidebar, before tags/connect-by-name**, not the former scrolling investment-banner slot after the one-hand-UI card. No button, label, blank reservation, VPN behavior, URL, connection controls or protection claim was implemented. Product/spec notes now distinguish retained upstream components from active Qryptana behavior.

## Verification

All Gradle checks and packages use the implementation commit above, the existing verified Temurin 21.0.13+9 installation at `/home/theanarchitect/.local/share/qryptana-toolchains/jdk-21.0.13+9`, installed Android SDK and offline dependencies. No new toolchain/dependency/system installation was needed. Automatic approval review authorized the local daemon/display operations. No approval rejection occurred.

From apps/multiplatform, used command-scoped JAVA_HOME above, ANDROID_HOME and ANDROID_SDK_ROOT=/home/theanarchitect/Android/Sdk, plus:

```sh
--offline --no-daemon --max-workers=1 --no-parallel \
  '-Dorg.gradle.jvmargs=-Xmx1536m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8' \
  -Pkotlin.compiler.execution.strategy=in-process
```

| Check | Result |
|---|---|
| :common:compileKotlinDesktop :common:desktopTest :android:compileFossDebugKotlin | Exit 0, 3m34s; **35 tests, zero failures/errors/skips**, XML retained |
| :desktop:createDistributable :android:assembleFossDebug | Exit 0, 54s; only affected local Linux/Android candidates rebuilt |
| Both APKs: build-tools/36.0.0/apksigner verify --verbose | Exit 0, APK v2 signatures verify; debug signing only |
| ARM64 APK: aapt dump badging | chat.simplex.app.debug, 7.0.3 / 381, min SDK 26, target/compile SDK 35; retained IDs |
| Focused source/call-site review | Active chat-list/onboarding/Settings routes and What's New feature lists no longer reference the campaign; dormant implementations are identified separately |
| iOS | Source/call-site and diff review only; no Swift or Xcode toolchain on this Linux host, so no iOS compilation/runtime claim |

Packaging uses the same JDK-release-metadata input safeguard as toolchain-acceptance-report.md. The retained investment-removal-evidence/packaging.gradle changes only the local output directory to the new directory below. Add `--init-script /home/theanarchitect/code/Qryptana-release-artifacts/investment-removal-20261030/packaging.gradle` to the packaging command. Runtime release reports 21.0.13. Source compilation produced existing deprecation/opt-in warnings; no test was suppressed. Raw logs remain in the artifact directory; committed copies normalize trailing whitespace.

### Isolated screen verification

A new authenticated Xwayland :97 server, with TCP disabled, hosted all task input/screenshots. Every run allocated a new `mkdtemp` directory under /tmp, isolating XDG config/data/cache, user.home, JVM temp and Java preferences. No pre-existing profile was reused or inspected, including profiles from previous acceptance runs. No contacts, messages, calls, device operations, external terms acceptance or update checks were performed.

For the changed screens, a diagnostic-only Java host renders the actual compiled composables from the old/new app jars. It uses upstream sample user data in memory, leaves investment dismissal **false**, and does not start the chat engine or open a chat database. Initializing the existing native runtime supports the search field's markdown parser only. The host supplies the required app-bar context and a themed surface for the Settings preview; it is not compiled into the candidate. This avoids altering onboarding preferences or accepting operator terms merely to reach the screens.

| Observed screen | Evidence |
|---|---|
| Preserved baseline chat list | before-chatlist.png visibly shows Invest in SimpleX Chat! / Crowdfunding on Wefunder |
| Rebuilt chat list, same synthetic setup | after-chatlist.png: banner absent; search/tags remain contiguous; no Privana entry or reserved widget |
| Rebuilt Settings through end of content | after-settings.png and after-settings-bottom.png: Appearance/privacy/help/data/network/call/version content remains, investment section absent |
| Rebuilt What's New | after-whatsnew.png: non-investment features retained, campaign feature/article action absent |
| Actual rebuilt application, fresh disposable startup | candidate-onboarding.png: branded copper/titanium onboarding; native initialization succeeds |
| Actual rebuilt application normal close | WM_DELETE_WINDOW invokes application close handler; candidate-exit.json records **0**; stderr contains only expected JAVA_TOOL_OPTIONS notice, no launcher-abort diagnostic |

These are component renders plus a packaged-app startup/normal-close smoke, not completion of the previous full Settings/Appearance/onboarding/lifecycle checklist in a real running chat session. No full Android-device, native iOS, theme-mode/font-scale or contact-compatibility test is claimed. Existing passing theme/updater tests remain supplemental safeguards; stock Android palettes and desktop styling source were not changed.

Early diagnostic host attempts lacked composition context/native-parser initialization and were not accepted as successful renders; raw logs remain locally. A transparent preview host also required the normal themed surface for legible Settings evidence. Corrected selected render logs have no exceptions. Some diagnostic JVMs lingered after their frames closed; the four recorded task-owned preview PIDs were terminated during cleanup (SIGTERM followed by SIGKILL where necessary). Their exit statuses are not called normal shutdown passes. The actual packaged application's exit 0 is recorded separately. All task-owned applications and the temporary display are stopped; disposable data remains under newly allocated /tmp paths, outside Git and archives.

## New local artifacts

Directory: `/home/theanarchitect/code/Qryptana-release-artifacts/investment-removal-20261030/`.

| File | Bytes | SHA-256 |
|---|---|---|
| qryptana-7.0.3-linux-x86_64-no-investment-local.tar.gz | 388968138 | `d1703e0a813508fe6831cc677ad737264d7e6a5cd3751d5038f49f34aef9ea5d` |
| qryptana-7.0.3-android-foss-arm64-v8a-no-investment-debug.apk | 276231728 | `476f659457172c99098e9dd1371cc0e2eb0d157ed2b9e36ea976eae977d35e7f` |
| qryptana-7.0.3-android-foss-armeabi-v7a-no-investment-debug.apk | 261927072 | `3a6c01a0bb8b3c1130e35980481ed724c90d9008cf56d984e87342abfc071cc3` |

Unpacked image: `linux-temurin/main/app/qryptana` within that directory. Archive verification checks **1017 regular files** against linux-manifest.json. All **806 native/launcher files** match the previous verified Temurin candidate, including libapplauncher.so (`336fe5e8ce1e8b36e0f8287f265f9456f52e776d48041f427e275c45c350e494`); no Haskell/native rebuild or binary substitution occurred. Existing v7.0.3 upstream native provenance applies. Both previous Linux archives and both previous APKs were rehashed unchanged; receipts are in preserved-artifacts.json.

Selected evidence under investment-removal-evidence/ contains no databases, profile settings, cookie contents, contacts, private keys or identities. Test profiles, failed diagnostic experiments, raw logs, rebuilt outputs and exact reproduction helpers remain in the local artifact directory. Public release remains HOLD for previously documented device, full runtime, installer, ownership, translation and legal gates. Removal of the investment promotion closes that UI issue; it does not supply other release approvals. No push, publication, upload, deployment, production signing or physical-device installation occurred.
