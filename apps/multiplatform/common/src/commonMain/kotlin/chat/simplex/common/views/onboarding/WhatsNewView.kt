package chat.simplex.common.views.onboarding

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalUriHandler
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chat.simplex.common.BuildConfigCommon
import chat.simplex.common.model.ChatController.appPrefs
import chat.simplex.common.model.ChatModel
import chat.simplex.common.model.*
import chat.simplex.common.model.ChatController.setConditionsNotified
import chat.simplex.common.model.ServerOperator.Companion.dummyOperatorInfo
import chat.simplex.common.platform.*
import chat.simplex.common.ui.theme.*
import chat.simplex.common.views.chat.item.CIFileViewScope
import chat.simplex.common.views.helpers.*
import chat.simplex.common.views.usersettings.UserAddressView
import chat.simplex.common.views.usersettings.networkAndServers.UsageConditionsView
import chat.simplex.common.views.usersettings.showAddShortLinkAlert
import chat.simplex.res.MR
import chat.simplex.res.qryptana_whats_new_absolute_privacy
import chat.simplex.res.qryptana_whats_new_absolute_privacy_desc
import chat.simplex.res.qryptana_whats_new_branding
import chat.simplex.res.qryptana_whats_new_branding_desc
import chat.simplex.res.qryptana_whats_new_independent_network
import chat.simplex.res.qryptana_whats_new_independent_network_desc
import chat.simplex.res.qryptana_whats_new_sovereign_infrastructure
import chat.simplex.res.qryptana_whats_new_sovereign_infrastructure_desc
import chat.simplex.res.crowdfunding_1
import chat.simplex.res.crowdfunding_2
import chat.simplex.res.crowdfunding_3
import chat.simplex.res.crowdfunding_4
import chat.simplex.res.ic_account_box
import chat.simplex.res.ic_add_link
import chat.simplex.res.ic_add_reaction
import chat.simplex.res.ic_arrow_back_ios_new
import chat.simplex.res.ic_arrow_forward_ios
import chat.simplex.res.ic_article
import chat.simplex.res.ic_at
import chat.simplex.res.ic_back_hand
import chat.simplex.res.ic_battery_2_bar
import chat.simplex.res.ic_battery_3_bar
import chat.simplex.res.ic_blur_on
import chat.simplex.res.ic_bolt
import chat.simplex.res.ic_calendar
import chat.simplex.res.ic_call
import chat.simplex.res.ic_chat
import chat.simplex.res.ic_chat_person
import chat.simplex.res.ic_check
import chat.simplex.res.ic_contact_support
import chat.simplex.res.ic_delete
import chat.simplex.res.ic_delete_forever
import chat.simplex.res.ic_desktop
import chat.simplex.res.ic_dns
import chat.simplex.res.ic_edit_note
import chat.simplex.res.ic_flag
import chat.simplex.res.ic_folder_pen
import chat.simplex.res.ic_forum
import chat.simplex.res.ic_forward
import chat.simplex.res.ic_group
import chat.simplex.res.ic_group_off
import chat.simplex.res.ic_inventory_2
import chat.simplex.res.ic_ios_share
import chat.simplex.res.ic_light_mode
import chat.simplex.res.ic_link
import chat.simplex.res.ic_lock
import chat.simplex.res.ic_manage_accounts
import chat.simplex.res.ic_maps_ugc
import chat.simplex.res.ic_match_case
import chat.simplex.res.ic_menu
import chat.simplex.res.ic_mic
import chat.simplex.res.ic_music_note
import chat.simplex.res.ic_open_in_new
import chat.simplex.res.ic_palette
import chat.simplex.res.ic_pending
import chat.simplex.res.ic_person
import chat.simplex.res.ic_person_add
import chat.simplex.res.ic_play_arrow_filled
import chat.simplex.res.ic_redeem
import chat.simplex.res.ic_safety_divider
import chat.simplex.res.ic_search
import chat.simplex.res.ic_security
import chat.simplex.res.ic_settings_ethernet
import chat.simplex.res.ic_shield
import chat.simplex.res.ic_star
import chat.simplex.res.ic_sync_problem
import chat.simplex.res.ic_task
import chat.simplex.res.ic_theater_comedy
import chat.simplex.res.ic_timer
import chat.simplex.res.ic_toast
import chat.simplex.res.ic_translate
import chat.simplex.res.ic_travel_explore
import chat.simplex.res.ic_upgrade
import chat.simplex.res.ic_upload_file
import chat.simplex.res.ic_verified_user
import chat.simplex.res.ic_videocam
import chat.simplex.res.ic_visibility_off
import chat.simplex.res.ic_vpn_key_filled
import chat.simplex.res.ic_waving_hand
import chat.simplex.res.ic_wifi_tethering
import chat.simplex.res.ic_work
import chat.simplex.res.learn_more
import chat.simplex.res.new_in_version
import chat.simplex.res.ok
import chat.simplex.res.v4_2_auto_accept_contact_requests
import chat.simplex.res.v4_2_auto_accept_contact_requests_desc
import chat.simplex.res.v4_2_group_links
import chat.simplex.res.v4_2_group_links_desc
import chat.simplex.res.v4_2_security_assessment
import chat.simplex.res.v4_2_security_assessment_desc
import chat.simplex.res.v4_3_improved_privacy_and_security
import chat.simplex.res.v4_3_improved_privacy_and_security_desc
import chat.simplex.res.v4_3_improved_server_configuration
import chat.simplex.res.v4_3_improved_server_configuration_desc
import chat.simplex.res.v4_3_irreversible_message_deletion
import chat.simplex.res.v4_3_irreversible_message_deletion_desc
import chat.simplex.res.v4_3_voice_messages
import chat.simplex.res.v4_3_voice_messages_desc
import chat.simplex.res.v4_4_disappearing_messages
import chat.simplex.res.v4_4_disappearing_messages_desc
import chat.simplex.res.v4_4_french_interface
import chat.simplex.res.v4_4_french_interface_descr
import chat.simplex.res.v4_4_live_messages
import chat.simplex.res.v4_4_live_messages_desc
import chat.simplex.res.v4_4_verify_connection_security
import chat.simplex.res.v4_4_verify_connection_security_desc
import chat.simplex.res.v4_5_italian_interface
import chat.simplex.res.v4_5_italian_interface_descr
import chat.simplex.res.v4_5_message_draft
import chat.simplex.res.v4_5_message_draft_descr
import chat.simplex.res.v4_5_multiple_chat_profiles
import chat.simplex.res.v4_5_multiple_chat_profiles_descr
import chat.simplex.res.v4_5_private_filenames
import chat.simplex.res.v4_5_private_filenames_descr
import chat.simplex.res.v4_5_reduced_battery_usage
import chat.simplex.res.v4_5_reduced_battery_usage_descr
import chat.simplex.res.v4_5_transport_isolation
import chat.simplex.res.v4_5_transport_isolation_descr
import chat.simplex.res.v4_6_audio_video_calls
import chat.simplex.res.v4_6_audio_video_calls_descr
import chat.simplex.res.v4_6_chinese_spanish_interface
import chat.simplex.res.v4_6_chinese_spanish_interface_descr
import chat.simplex.res.v4_6_group_moderation
import chat.simplex.res.v4_6_group_moderation_descr
import chat.simplex.res.v4_6_group_welcome_message
import chat.simplex.res.v4_6_group_welcome_message_descr
import chat.simplex.res.v4_6_hidden_chat_profiles
import chat.simplex.res.v4_6_hidden_chat_profiles_descr
import chat.simplex.res.v4_6_reduced_battery_usage
import chat.simplex.res.v4_6_reduced_battery_usage_descr
import chat.simplex.res.v5_0_app_passcode
import chat.simplex.res.v5_0_app_passcode_descr
import chat.simplex.res.v5_0_large_files_support
import chat.simplex.res.v5_0_large_files_support_descr
import chat.simplex.res.v5_0_polish_interface
import chat.simplex.res.v5_0_polish_interface_descr
import chat.simplex.res.v5_1_better_messages
import chat.simplex.res.v5_1_better_messages_descr
import chat.simplex.res.v5_1_custom_themes
import chat.simplex.res.v5_1_custom_themes_descr
import chat.simplex.res.v5_1_japanese_portuguese_interface
import chat.simplex.res.v5_1_message_reactions
import chat.simplex.res.v5_1_message_reactions_descr
import chat.simplex.res.v5_1_self_destruct_passcode
import chat.simplex.res.v5_1_self_destruct_passcode_descr
import chat.simplex.res.v5_2_disappear_one_message
import chat.simplex.res.v5_2_disappear_one_message_descr
import chat.simplex.res.v5_2_favourites_filter
import chat.simplex.res.v5_2_favourites_filter_descr
import chat.simplex.res.v5_2_fix_encryption
import chat.simplex.res.v5_2_fix_encryption_descr
import chat.simplex.res.v5_2_message_delivery_receipts
import chat.simplex.res.v5_2_message_delivery_receipts_descr
import chat.simplex.res.v5_2_more_things
import chat.simplex.res.v5_2_more_things_descr
import chat.simplex.res.v5_3_discover_join_groups
import chat.simplex.res.v5_3_discover_join_groups_descr
import chat.simplex.res.v5_3_encrypt_local_files
import chat.simplex.res.v5_3_encrypt_local_files_descr
import chat.simplex.res.v5_3_new_desktop_app
import chat.simplex.res.v5_3_new_desktop_app_descr
import chat.simplex.res.v5_3_new_interface_languages
import chat.simplex.res.v5_3_new_interface_languages_descr
import chat.simplex.res.v5_3_simpler_incognito_mode
import chat.simplex.res.v5_3_simpler_incognito_mode_descr
import chat.simplex.res.v5_4_better_groups
import chat.simplex.res.v5_4_better_groups_descr
import chat.simplex.res.v5_4_block_group_members
import chat.simplex.res.v5_4_block_group_members_descr
import chat.simplex.res.v5_4_incognito_groups
import chat.simplex.res.v5_4_incognito_groups_descr
import chat.simplex.res.v5_4_link_mobile_desktop
import chat.simplex.res.v5_4_link_mobile_desktop_descr
import chat.simplex.res.v5_4_more_things_descr
import chat.simplex.res.v5_5_join_group_conversation
import chat.simplex.res.v5_5_join_group_conversation_descr
import chat.simplex.res.v5_5_message_delivery
import chat.simplex.res.v5_5_message_delivery_descr
import chat.simplex.res.v5_5_new_interface_languages
import chat.simplex.res.v5_5_private_notes
import chat.simplex.res.v5_5_private_notes_descr
import chat.simplex.res.v5_5_simpler_connect_ui
import chat.simplex.res.v5_5_simpler_connect_ui_descr
import chat.simplex.res.v5_6_app_data_migration
import chat.simplex.res.v5_6_app_data_migration_descr
import chat.simplex.res.v5_6_picture_in_picture_calls
import chat.simplex.res.v5_6_picture_in_picture_calls_descr
import chat.simplex.res.v5_6_quantum_resistant_encryption
import chat.simplex.res.v5_6_quantum_resistant_encryption_descr
import chat.simplex.res.v5_6_safer_groups
import chat.simplex.res.v5_6_safer_groups_descr
import chat.simplex.res.v5_7_call_sounds
import chat.simplex.res.v5_7_call_sounds_descr
import chat.simplex.res.v5_7_forward
import chat.simplex.res.v5_7_forward_descr
import chat.simplex.res.v5_7_network
import chat.simplex.res.v5_7_network_descr
import chat.simplex.res.v5_7_new_interface_languages
import chat.simplex.res.v5_7_quantum_resistant_encryption_descr
import chat.simplex.res.v5_7_shape_profile_images
import chat.simplex.res.v5_7_shape_profile_images_descr
import chat.simplex.res.v5_8_chat_themes
import chat.simplex.res.v5_8_chat_themes_descr
import chat.simplex.res.v5_8_message_delivery
import chat.simplex.res.v5_8_message_delivery_descr
import chat.simplex.res.v5_8_persian_ui
import chat.simplex.res.v5_8_private_routing
import chat.simplex.res.v5_8_private_routing_descr
import chat.simplex.res.v5_8_safe_files
import chat.simplex.res.v5_8_safe_files_descr
import chat.simplex.res.v6_0_chat_list_media
import chat.simplex.res.v6_0_connect_faster_descr
import chat.simplex.res.v6_0_connection_servers_status
import chat.simplex.res.v6_0_connection_servers_status_descr
import chat.simplex.res.v6_0_delete_many_messages_descr
import chat.simplex.res.v6_0_increase_font_size
import chat.simplex.res.v6_0_new_chat_experience
import chat.simplex.res.v6_0_new_media_options
import chat.simplex.res.v6_0_privacy_blur
import chat.simplex.res.v6_0_private_routing_descr
import chat.simplex.res.v6_0_reachable_chat_toolbar
import chat.simplex.res.v6_0_reachable_chat_toolbar_descr
import chat.simplex.res.v6_0_upgrade_app
import chat.simplex.res.v6_0_upgrade_app_descr
import chat.simplex.res.v6_0_your_contacts_descr
import chat.simplex.res.v6_1_better_calls
import chat.simplex.res.v6_1_better_calls_descr
import chat.simplex.res.v6_1_better_security
import chat.simplex.res.v6_1_better_security_descr
import chat.simplex.res.v6_1_better_user_experience
import chat.simplex.res.v6_1_customizable_message_descr
import chat.simplex.res.v6_1_delete_many_messages_descr
import chat.simplex.res.v6_1_forward_many_messages_descr
import chat.simplex.res.v6_1_message_dates_descr
import chat.simplex.res.v6_1_switch_chat_profile_descr
import chat.simplex.res.v6_2_business_chats
import chat.simplex.res.v6_2_business_chats_descr
import chat.simplex.res.v6_2_improved_chat_navigation
import chat.simplex.res.v6_2_improved_chat_navigation_descr
import chat.simplex.res.v6_2_network_decentralization
import chat.simplex.res.v6_2_network_decentralization_descr
import chat.simplex.res.v6_2_network_decentralization_enable_flux
import chat.simplex.res.v6_3_better_groups_performance
import chat.simplex.res.v6_3_better_privacy_and_security
import chat.simplex.res.v6_3_faster_deletion_of_groups
import chat.simplex.res.v6_3_faster_sending_messages
import chat.simplex.res.v6_3_mentions
import chat.simplex.res.v6_3_mentions_descr
import chat.simplex.res.v6_3_organize_chat_lists
import chat.simplex.res.v6_3_organize_chat_lists_descr
import chat.simplex.res.v6_3_private_media_file_names
import chat.simplex.res.v6_3_reports
import chat.simplex.res.v6_3_reports_descr
import chat.simplex.res.v6_3_set_message_expiration_in_chats
import chat.simplex.res.v6_4_1_keep_chats_clean
import chat.simplex.res.v6_4_1_keep_chats_clean_descr
import chat.simplex.res.v6_4_1_new_interface_languages
import chat.simplex.res.v6_4_1_new_interface_languages_descr
import chat.simplex.res.v6_4_1_short_address
import chat.simplex.res.v6_4_1_short_address_create
import chat.simplex.res.v6_4_1_short_address_share
import chat.simplex.res.v6_4_1_short_address_update
import chat.simplex.res.v6_4_1_welcome_contacts
import chat.simplex.res.v6_4_1_welcome_contacts_descr
import chat.simplex.res.v6_4_connect_faster
import chat.simplex.res.v6_4_connect_faster_descr
import chat.simplex.res.v6_4_message_delivery_descr
import chat.simplex.res.v6_4_review_members
import chat.simplex.res.v6_4_review_members_descr
import chat.simplex.res.v6_4_role_moderator
import chat.simplex.res.v6_4_role_moderator_descr
import chat.simplex.res.v6_4_support_chat
import chat.simplex.res.v6_4_support_chat_descr
import chat.simplex.res.v6_5_invite_friends
import chat.simplex.res.v6_5_invite_friends_descr
import chat.simplex.res.v6_5_non_profit_governance
import chat.simplex.res.v6_5_non_profit_governance_descr
import chat.simplex.res.v6_5_ownership
import chat.simplex.res.v6_5_privacy
import chat.simplex.res.v6_5_public_channels
import chat.simplex.res.v6_5_reliability
import chat.simplex.res.v6_5_safe_web_links
import chat.simplex.res.v6_5_safe_web_links_descr
import chat.simplex.res.v6_5_security
import chat.simplex.res.v7_0_channels
import chat.simplex.res.v7_0_channels_contributors
import chat.simplex.res.v7_0_channels_previews
import chat.simplex.res.v7_0_channels_relays
import chat.simplex.res.v7_0_channels_wider_messages
import chat.simplex.res.v7_0_invest
import chat.simplex.res.v7_0_invest_descr
import chat.simplex.res.v7_0_invest_learn_more
import chat.simplex.res.view_updated_conditions
import chat.simplex.res.whats_new_read_more
import chat.simplex.res.whats_new_thanks_to_users_contribute_weblate
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import kotlin.math.absoluteValue

@Composable
fun ModalData.WhatsNewView(updatedConditions: Boolean = false, viaSettings: Boolean = false, close: () -> Unit) {
  val currentVersion = remember { mutableStateOf(versionDescriptions.lastIndex) }
  val rhId = chatModel.remoteHostId()

  if (updatedConditions) {
    LaunchedEffect(Unit) {
      val conditionsId = chatModel.conditions.value.currentConditions.conditionsId
      try {
        setConditionsNotified(rh = rhId, conditionsId = conditionsId)
      } catch (e: Exception) {
        Log.d(TAG, "WhatsNewView setConditionsNotified error: ${e.message}")
      }
    }
  }

  @Composable
  fun featureDescription(icon: ImageResource?, titleId: StringResource, descrId: StringResource?, link: String?, subfeatures: List<Pair<ImageResource, StringResource>>) {
    @Composable
    fun linkButton(link: String) {
      val uriHandler = LocalUriHandler.current
      Icon(
        painterResource(MR.images.ic_open_in_new), stringResource(titleId), tint = MaterialTheme.colors.primary,
        modifier = Modifier
          .clickable { if (link.startsWith("simplex:")) uriHandler.openVerifiedSimplexUri(link) else uriHandler.openExternalLink(link) }
      )
    }

    Column(modifier = Modifier.padding(bottom = 12.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 4.dp)
      ) {
        if (icon != null)  Icon(painterResource(icon), stringResource(titleId), tint = MaterialTheme.colors.secondary)
        Text(
          generalGetString(titleId),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.h4,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.padding(bottom = 6.dp)
        )
        if (link != null) {
          linkButton(link)
        }
      }
      if (descrId != null) Text(generalGetString(descrId), fontSize = 15.sp)
      for ((si, sd) in subfeatures) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.padding(bottom = 6.dp)
        ) {
          Icon(painterResource(si), stringResource(sd), tint = MaterialTheme.colors.secondary)
          Text(generalGetString(sd), fontSize = 15.sp)
        }
      }
    }
  }

  @Composable
  fun pagination() {
    Row(
      Modifier
        .padding(bottom = DEFAULT_PADDING)
    ) {
      if (currentVersion.value > 0) {
        val prev = currentVersion.value - 1
        Box(Modifier.clip(RoundedCornerShape(20.dp))) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
              .clickable { currentVersion.value = prev }
              .padding(8.dp)
          ) {
            Icon(painterResource(MR.images.ic_arrow_back_ios_new), "previous", tint = MaterialTheme.colors.primary)
            Text(versionDescriptions[prev].version, color = MaterialTheme.colors.primary)
          }
        }
      }
      Spacer(Modifier.fillMaxWidth().weight(1f))
      if (currentVersion.value < versionDescriptions.lastIndex) {
        val next = currentVersion.value + 1
        Box(Modifier.clip(RoundedCornerShape(20.dp))) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
              .clickable { currentVersion.value = next }
              .padding(8.dp)
          ) {
            Text(versionDescriptions[next].version, color = MaterialTheme.colors.primary)
            Icon(painterResource(MR.images.ic_arrow_forward_ios), "next", tint = MaterialTheme.colors.primary)
          }
        }
      }
    }
  }

  val v = versionDescriptions[currentVersion.value]

  ModalView(close = close) {
    ColumnWithScrollBar(
      Modifier
        .padding(horizontal = DEFAULT_PADDING),
      verticalArrangement = Arrangement.spacedBy(DEFAULT_PADDING.times(0.75f))
    ) {
      AppBarTitle(String.format(generalGetString(MR.strings.new_in_version), v.version), withPadding = false, bottomPadding = DEFAULT_PADDING)

      val modalManager = if (viaSettings) ModalManager.start else ModalManager.center

      v.features.forEach { feature ->
        when (feature) {
          is VersionFeature.FeatureDescription -> {
            if (feature.show) {
              featureDescription(feature.icon, feature.titleId, feature.descrId, feature.link, feature.subfeatures)
            }
          }
          is VersionFeature.FeatureView -> {
            feature.view(modalManager)
          }
        }
      }

      if (v.post != null) {
        ReadMoreButton(v.post)
      }

      if (updatedConditions) {
        Text(
          stringResource(MR.strings.view_updated_conditions),
          color = MaterialTheme.colors.primary,
          modifier = Modifier
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null
            ) {
              modalManager.showModalCloseable { close ->
                UsageConditionsView(
                  userServers = mutableStateOf(emptyList()),
                  currUserServers = mutableStateOf(emptyList()),
                  close = close,
                  rhId = rhId
                )
              }
            }
        )
      }

      if (!viaSettings) {
        Spacer(Modifier.fillMaxHeight().weight(1f))
        Box(
          Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
          Box(Modifier.clip(RoundedCornerShape(20.dp))) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center,
              modifier = Modifier
                .clickable { close() }
                .padding(8.dp)
            ) {
              Text(
                generalGetString(MR.strings.ok),
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.primary
              )
            }
          }
        }
        Spacer(Modifier.fillMaxHeight().weight(1f))
      }

      Spacer(Modifier.fillMaxHeight().weight(1f))

      pagination()
    }
  }
}

@Composable
fun ReadMoreButton(url: String) {
  val uriHandler = LocalUriHandler.current
  Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = DEFAULT_PADDING.div(4))) {
    Text(
      stringResource(MR.strings.whats_new_read_more),
      color = MaterialTheme.colors.primary,
      modifier = Modifier
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null
        ) {
          uriHandler.openExternalLink(url)
        }
    )
    Icon(painterResource(MR.images.ic_open_in_new), stringResource(MR.strings.whats_new_read_more), tint = MaterialTheme.colors.primary)
  }
}

private sealed class VersionFeature {
  class FeatureDescription(
    val icon: ImageResource?,
    val titleId: StringResource,
    val descrId: StringResource?,
    var subfeatures: List<Pair<ImageResource, StringResource>> = listOf(),
    val link: String? = null,
    val show: Boolean = true
  ): VersionFeature()

  class FeatureView(
    val icon: ImageResource?,
    val titleId: StringResource,
    val view: @Composable (modalManager: ModalManager) -> Unit
  ): VersionFeature()
}

private data class VersionDescription(
  val version: String,
  val features: List<VersionFeature>,
  val post: String? = null,
)

private val versionDescriptions: List<VersionDescription> = listOf(
  VersionDescription(
    version = "Qryptana 0.1.0-alpha",
    post = null,
    features = listOf(
      VersionFeature.FeatureDescription(
        icon = MR.images.ic_dns,
        titleId = MR.strings.qryptana_whats_new_sovereign_infrastructure,
        descrId = MR.strings.qryptana_whats_new_sovereign_infrastructure_desc
      ),
      VersionFeature.FeatureDescription(
        icon = MR.images.ic_security,
        titleId = MR.strings.qryptana_whats_new_absolute_privacy,
        descrId = MR.strings.qryptana_whats_new_absolute_privacy_desc
      ),
      VersionFeature.FeatureDescription(
        icon = MR.images.ic_palette,
        titleId = MR.strings.qryptana_whats_new_branding,
        descrId = MR.strings.qryptana_whats_new_branding_desc
      ),
      VersionFeature.FeatureDescription(
        icon = MR.images.ic_travel_explore,
        titleId = MR.strings.qryptana_whats_new_independent_network,
        descrId = MR.strings.qryptana_whats_new_independent_network_desc
      )
    )
  )
)

private val lastVersion = versionDescriptions.last().version

fun setLastVersionDefault(m: ChatModel) {
  if (appPrefs.whatsNewVersion.get() != lastVersion) {
    appPrefs.whatsNewVersion.set(lastVersion)
  }
}

fun shouldShowWhatsNew(m: ChatModel): Boolean {
  val v = m.controller.appPrefs.whatsNewVersion.get()
  setLastVersionDefault(m)
  return v != lastVersion
}

private const val WEFUNDER_URL = "https://wefunder.com/simplex.chat?utm_source=app"

private const val CROWDFUNDING_CONTACT_URI = "simplex:/a#JxGcOA1_QhlmVFzYYabloMbvMZk5Y9d9iS3ITDnhzYo?h=smp11.simplex.im"

// the center modal takes the remaining width of the window, so the image is limited to its design width
private val MAX_CROWDFUNDING_IMAGE_WIDTH = DEFAULT_MIN_CENTER_MODAL_WIDTH

// the width of the page images shipped with the desktop app, so that they are never upscaled
private val CROWDFUNDING_PAGE_IMAGE_WIDTH = DEFAULT_MIN_CENTER_MODAL_WIDTH

// the corner radius the images are designed with, and the same radius as a share of their design width
private val CROWDFUNDING_IMAGE_CORNER_RADIUS = 12.dp
private const val CROWDFUNDING_IMAGE_CORNER_RADIUS_RATIO = 0.03f

private class CrowdfundingLayout(
  val maxImageWidth: Dp,
  val imageShape: Shape,
  // the modal manager that shows the page in the center of the window, or null when nothing does
  private val centerOfWindow: ModalManager?
) {
  fun inCenterOfWindow(modalManager: ModalManager) = modalManager === centerOfWindow
}

// the images are designed for the width of a phone screen, which Android always gives them. On desktop
// they are limited to their own width, and their radius is scaled with them, as they are still shown
// wider than designed: a fixed radius would not only look almost square, but would also leave the corners
// baked into the jpegs visible - they have black behind them, as jpegs have no transparency
private val crowdfundingLayout = if (appPlatform.isDesktop)
  CrowdfundingLayout(CROWDFUNDING_PAGE_IMAGE_WIDTH, object : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline =
      Outline.Rounded(RoundRect(size.toRect(), CornerRadius(size.width * CROWDFUNDING_IMAGE_CORNER_RADIUS_RATIO)))
  }, ModalManager.center)
else
  CrowdfundingLayout(Dp.Unspecified, RoundedCornerShape(CROWDFUNDING_IMAGE_CORNER_RADIUS), null)

// Google Play policy restricts promoting investments, so Play builds only show it in the US
@Composable
fun crowdfundingAvailable(): Boolean {
  if (!platform.androidIsPlayStoreBuild) return true
  if (androidPlayStoreCountry.value == null) {
    LaunchedEffect(Unit) {
      if (androidPlayStoreCountry.value == null) platform.androidLoadPlayStoreCountry()
    }
  }
  return isInUs()
}

fun isInUs(): Boolean =
  androidPlayStoreCountry.value == "US"
      || androidPlayStoreCountry.value == ""
      || androidPlayStoreCountry.value == null

@Composable
private fun InvestInSimpleXChatView(modalManager: ModalManager) {
  if (!crowdfundingAvailable()) return
  val showGetStake = { modalManager.showModalCloseable(cardScreen = true) { close -> GetStakeView(showFirstImage = false, inCenterOfWindow = crowdfundingLayout.inCenterOfWindow(modalManager), close = close) } }
  Column(modifier = Modifier.padding(bottom = 12.dp)) {
    Text(
      generalGetString(MR.strings.v7_0_invest),
      style = MaterialTheme.typography.h4,
      fontWeight = FontWeight.Medium,
      modifier = Modifier.padding(bottom = 6.dp)
    )
    Text(
      buildAnnotatedString {
        append(generalGetString(MR.strings.v7_0_invest_descr))
        append(" ")
        withStyle(SpanStyle(color = MaterialTheme.colors.primary)) {
          append(generalGetString(MR.strings.learn_more))
        }
      },
      fontSize = 15.sp,
      modifier = Modifier
        .pointerHoverIcon(PointerIcon.Hand)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = showGetStake
        )
    )
    if (BuildConfigCommon.SIMPLEX_ASSETS) {
      Image(
        painterResource(MR.images.crowdfunding_1),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
          .padding(top = 8.dp)
          .widthIn(max = MAX_CROWDFUNDING_IMAGE_WIDTH)
          .fillMaxWidth()
          .clip(crowdfundingLayout.imageShape)
          .pointerHoverIcon(PointerIcon.Hand)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = showGetStake
          )
      )
    }
  }
}

private class CrowdfundingSlide(
  val image: ImageResource,
  val heading: String,
  val info: String?,
  val text: String,
)

// not localized: the page is only shown to US investors, and the text duplicates the images
private val getStakeSlides: List<CrowdfundingSlide> = listOf(
  CrowdfundingSlide(
    MR.images.crowdfunding_1,
    "The first and the only messaging network without any user IDs",
    null,
    "By investing, you can benefit from the company growth, and help us build the future of private and secure communications."
  ),
  CrowdfundingSlide(
    MR.images.crowdfunding_2,
    "480,000+ users joined on their own",
    null,
    "SimpleX users have been more than doubling every year without any paid marketing, and donated over \$650,000."
  ),
  CrowdfundingSlide(
    MR.images.crowdfunding_3,
    "Developers already bet on SimpleX success",
    "Independent developers created moderation and AI bots, Telegram bridges, and a public server registry.",
    "Every service developers build on SimpleX Network may increase its value, and bring new users to SimpleX Chat."
  ),
  CrowdfundingSlide(
    MR.images.crowdfunding_4,
    "Revenue plan: free for users, channels & businesses pay",
    "SimpleX Chat plans to earn from the infrastructure and services that creators, businesses and large communities need as they grow.",
    "Read about how we plan to make SimpleX Chat and network profitable, and about all the investment terms on Wefunder."
  ),
)

@Composable
fun GetStakeView(showFirstImage: Boolean, inCenterOfWindow: Boolean = false, close: () -> Unit) {
  val uriHandler = LocalUriHandler.current
  val stopped = chatModel.chatRunning.value == false

  @Composable
  fun slideImage(slide: CrowdfundingSlide) {
    if (BuildConfigCommon.SIMPLEX_ASSETS) {
      Image(
        painterResource(slide.image),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
          .widthIn(max = crowdfundingLayout.maxImageWidth)
          .fillMaxWidth()
          .clip(crowdfundingLayout.imageShape)
          .fullScreenOnClick(slide.image)
      )
    } else {
      Text(slide.heading, style = MaterialTheme.typography.h4, fontWeight = FontWeight.Medium)
      if (slide.info != null) {
        Text(slide.info, Modifier.padding(top = 4.dp), lineHeight = 24.sp)
      }
    }
  }

  ColumnWithScrollBar(Modifier.pinchZoom().padding(horizontal = DEFAULT_PADDING)) {
    // in the center of the window the page is wide enough for the title to fit on one line
    val title = "Invest in\nSimpleX Chat"
    AppBarTitle(if (inCenterOfWindow) title.replace("\n", " ") else title, withPadding = false)
    // What's new already shows the image of the first slide, above the link that opens this page
    if (showFirstImage) {
      slideImage(getStakeSlides[0])
    }
    Text(
      buildAnnotatedString {
        append(getStakeSlides[0].text)
        // only the link is clickable, the rest of the paragraph is not
        withLink(LinkAnnotation.Url(WEFUNDER_URL) { uriHandler.openUriCatching(WEFUNDER_URL) }) {
          withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)) {
            append(" Learn more and invest on Wefunder.")
          }
        }
      },
      Modifier.padding(top = if (showFirstImage) 8.dp else 0.dp),
      lineHeight = 24.sp
    )

    getStakeSlides.drop(1).forEach { slide ->
      Column(Modifier.padding(top = DEFAULT_PADDING * 1.5f)) {
        slideImage(slide)
        Text(slide.text, Modifier.padding(top = 8.dp), lineHeight = 24.sp)
      }
    }

    Column(
      Modifier.fillMaxWidth().padding(top = DEFAULT_PADDING * 2),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      OnboardingActionButton(
        if (appPlatform.isAndroid) Modifier.fillMaxWidth() else Modifier.widthIn(min = 300.dp),
        labelId = MR.strings.v7_0_invest_learn_more,
        onboarding = null,
        onclick = { uriHandler.openUriCatching(WEFUNDER_URL) }
      )
      if (!chatModel.desktopNoUserNoRemote) {
        TextButtonBelowOnboardingButton(
          "or ask SimpleX team",
          onClick = if (stopped) null else ({
            close()
            uriHandler.openVerifiedSimplexUri(CROWDFUNDING_CONTACT_URI)
          })
        )
      }
    }
  }
}

// there is no pinch gesture with a mouse, so on desktop a slide is opened full screen instead
@Composable
private fun Modifier.fullScreenOnClick(image: ImageResource): Modifier {
  if (!appPlatform.isDesktop) return this
  return pointerHoverIcon(PointerIcon.Hand).clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null
  ) {
    ModalManager.fullscreen.showCustomModal { close ->
      BackHandler(onBack = close)
      Box(
        Modifier
          .fillMaxSize()
          .background(Color.Black)
          .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = close),
        contentAlignment = Alignment.Center
      ) {
        Image(painterResource(image), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize())
      }
    }
  }
}

private const val MAX_PAGE_ZOOM = 5f

/**
 * The slide images contain small text that is unreadable at screen width, so the page can be pinch-zoomed.
 * Android only: pinch is unavailable with a mouse.
 */
@Composable
private fun Modifier.pinchZoom(): Modifier {
  if (!appPlatform.isAndroid) return this
  var scale by remember { mutableStateOf(1f) }
  var offsetX by remember { mutableStateOf(0f) }
  var offsetY by remember { mutableStateOf(0f) }
  var size by remember { mutableStateOf(IntSize.Zero) }
  return this
    .onGloballyPositioned { size = it.size }
    .graphicsLayer {
      scaleX = scale
      scaleY = scale
      translationX = offsetX
      translationY = offsetY
    }
    .pointerInput(Unit) {
      awaitEachGesture {
        // the initial pass, as the scroll of the same column is applied after this modifier and would take the gesture first
        awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
        var taken: Boolean? = null
        do {
          val event = awaitPointerEvent(PointerEventPass.Initial)
          val multiTouch = event.changes.count { it.pressed } > 1
          if (multiTouch || scale > 1f) {
            scale = (scale * event.calculateZoom()).coerceIn(1f, MAX_PAGE_ZOOM)
            val pan = event.calculatePan()
            // the page is scaled around its center, so it can be panned by half of the overflow in each direction
            val maxX = size.width * (scale - 1f) / 2
            val maxY = size.height * (scale - 1f) / 2
            val pannedY = offsetY + pan.y * scale
            // the clamp is applied even when the gesture is not taken: at scale 1 both bounds
            // are 0, which resets the offsets after zooming back out
            offsetX = (offsetX + pan.x * scale).coerceIn(-maxX, maxX)
            offsetY = pannedY.coerceIn(-maxY, maxY)
            // two fingers always mean zoom, taken without a touch slop: waiting for one would let
            // the scroll reach its own slop first and scroll the page. A one finger drag is left
            // to the scroll at the edges, decided once so it cannot alternate mid drag
            if (multiTouch) taken = true
            else if (taken == null && pan.y != 0f) taken = pannedY.absoluteValue < maxY
            if (taken == true) event.changes.forEach { if (it.pressed) it.consume() }
          }
        } while (event.changes.any { it.pressed })
      }
    }
}

@Composable
fun CreateUpdateAddressShortLinkView(modalManager: ModalManager) {
  val clipboard = LocalClipboardManager.current
  val progressIndicator = remember { mutableStateOf(false) }

  fun share(userAddress: String) { clipboard.shareText(userAddress) }

  Column(modifier = Modifier.padding(bottom = 12.dp)) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(bottom = 4.dp)
    ) {
      Icon(painterResource(MR.images.ic_link), stringResource(MR.strings.v6_4_1_short_address), tint = MaterialTheme.colors.secondary)
      Text(
        generalGetString(MR.strings.v6_4_1_short_address),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.h4,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 6.dp)
      )
    }
    val addr = chatModel.userAddress.value
    if (addr != null) {
      if (addr.shouldBeUpgraded) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            stringResource(MR.strings.v6_4_1_short_address_update),
            color = MaterialTheme.colors.primary,
            fontSize = 15.sp,
            modifier = Modifier
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
              ) {
                showAddShortLinkAlert(progressIndicator = progressIndicator, share = ::share)
              }
          )
          if (progressIndicator.value) {
            CIFileViewScope.progressIndicator(sizeMultiplier = 0.5f)
          }
        }
      } else {
        Text(
          stringResource(MR.strings.v6_4_1_short_address_share),
          color = MaterialTheme.colors.primary,
          fontSize = 15.sp,
          modifier = Modifier
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null
            ) {
              share(addr.connLinkContact.simplexChatUri(short = true))
            }
        )
      }
    } else {
      Text(
        stringResource(MR.strings.v6_4_1_short_address_create),
        color = MaterialTheme.colors.primary,
        fontSize = 15.sp,
        modifier = Modifier
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
          ) {
            modalManager.showModalCloseable { close ->
              UserAddressView(chatModel = chatModel, shareViaProfile = false, autoCreateAddress = true, close = close)
            }
          }
      )
    }
  }
}

@Preview/*(
  uiMode = Configuration.UI_MODE_NIGHT_YES,
  showBackground = true,
  name = "Dark Mode"
)*/
@Composable
fun PreviewWhatsNewView() {
  SimpleXTheme {
    val data = remember { ModalData() }
    data.WhatsNewView(
      viaSettings = true,
      close = {}
    )
  }
}
