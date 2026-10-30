# Future Privana sidebar placement

Status: documented only; entry hidden until implemented.

On desktop, the future Privana entry belongs **directly below the search bar in the left sidebar**, before the tags/connect-by-name content. The current anchor is the top-toolbar branch of `ChatList` in `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chatlist/ChatListView.kt`, immediately after `ChatListSearchBar`. Any future alternate toolbar layout must preserve that visual relationship to search.

This is not the old investment-banner slot: that banner was a separate scrolling list item after the one-hand-UI card and before conversations. Its removal does not reserve that location for Privana.

For now, render no Privana button, label, placeholder, spacer or blank reserved area. No VPN behavior, service URL, connection controls, status indicator or protection claim is implemented or implied. Implementation and its behavior need a separate task. Stock Android palettes and desktop copper/titanium styling remain unchanged.

The inherited investment promotion is removed from active chat-list/onboarding, Settings and What's New entry points on desktop/Android and iOS. The crowdfunding article link is removed from the v7 What's New entry. Unreachable upstream investment view definitions, resource strings/images, stored preference keys and shared banner chrome are retained to avoid destructive resource or data changes. Licenses, attribution, legitimate operator information and non-investment upstream release information remain intact.
