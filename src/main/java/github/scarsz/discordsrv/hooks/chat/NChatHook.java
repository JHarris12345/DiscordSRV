/*
 * DiscordSRV - https://github.com/DiscordSRV/DiscordSRV
 *
 * Copyright (C) 2016 - 2024 Austin "Scarsz" Shapiro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

/*
 * DiscordSRV - https://github.com/DiscordSRV/DiscordSRV
 *
 * Copyright (C) 2016 - 2024 Austin "Scarsz" Shapiro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

/*
 * DiscordSRV - https://github.com/DiscordSRV/DiscordSRV
 *
 * Copyright (C) 2016 - 2024 Austin "Scarsz" Shapiro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package github.scarsz.discordsrv.hooks.chat;

import com.nickuc.chat.api.events.PublicMessageEvent;
import com.nickuc.chat.api.nChatAPI;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.LangUtil;
import github.scarsz.discordsrv.util.MessageUtil;
import github.scarsz.discordsrv.util.PlayerUtil;
import github.scarsz.discordsrv.util.PluginUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

public class NChatHook implements ChatHook {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMessage(PublicMessageEvent event) {
        DiscordSRV.getPlugin().processChatMessage(event.getSender().getPlayer(), event.getMessage(), event.getChannel().getName(), event.isCancelled(), event);
    }

    @Override
    public void broadcastMessageToChannel(String channelName, Component message) {
        nChatAPI.getApi().getChannelByName(channelName).ifPresent(chatChannel -> {
            String legacy = MessageUtil.toLegacy(message);
            String chatChannelCommand = chatChannel.getCommand();
            String chatChannelNickname = chatChannelCommand != null ? chatChannelCommand : Character.toString(chatChannel.getName().charAt(0));
            String chatChannelColor = ChatColor.getLastColors(chatChannel.getFormat());

            String plainMessage = LangUtil.Message.CHAT_CHANNEL_MESSAGE.toString()
                    .replace("%channelname%", chatChannel.getName())
                    .replace("%channelnickname%", chatChannelNickname)
                    .replace("%message%", legacy)
                    .replace("%channelcolor%", MessageUtil.toLegacy(MessageUtil.toComponent(MessageUtil.translateLegacy(chatChannelColor))));

            String translatedMessage = MessageUtil.translateLegacy(plainMessage);
            nChatAPI.getApi().handleVirtualMessage(translatedMessage, chatChannel, virtualMessageEvent ->
                    PlayerUtil.notifyPlayersOfMentions(player -> virtualMessageEvent.getRecipients().contains(player), legacy));
        });
    }

    @Override
    public Plugin getPlugin() {
        return PluginUtil.getPlugin("nChat");
    }

}
