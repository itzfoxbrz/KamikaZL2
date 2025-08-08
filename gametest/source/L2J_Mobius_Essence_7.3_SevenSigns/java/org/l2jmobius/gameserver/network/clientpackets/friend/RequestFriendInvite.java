/*
 * Copyright (c) 2013 L2jMobius
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.l2jmobius.gameserver.network.clientpackets.friend;

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.gameserver.data.xml.FakePlayerData;
import org.l2jmobius.gameserver.model.BlockList;
import org.l2jmobius.gameserver.model.ClientSettings;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import org.l2jmobius.gameserver.network.serverpackets.friend.FriendAddRequest;

public class RequestFriendInvite extends ClientPacket
{
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readString();
	}
	
	private void scheduleDeny(Player player)
	{
		if (player != null)
		{
			player.sendPacket(SystemMessageId.YOU_HAVE_FAILED_TO_ADD_A_FRIEND_TO_YOUR_FRIENDS_LIST);
			player.onTransactionResponse();
		}
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (FakePlayerData.getInstance().isTalkable(_name))
		{
			if (!player.isProcessingRequest())
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_VE_REQUESTED_C1_TO_BE_ON_YOUR_FRIENDS_LIST);
				sm.addString(_name);
				player.sendPacket(sm);
				ThreadPool.schedule(() -> scheduleDeny(player), 10000);
				player.blockRequest();
			}
			else
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_ON_ANOTHER_TASK_PLEASE_TRY_AGAIN_LATER);
				sm.addString(_name);
				player.sendPacket(sm);
			}
			return;
		}
		
		final Player friend = World.getInstance().getPlayer(_name);
		
		// Target is not found in the game.
		if ((friend == null) || !friend.isOnline() || friend.isInvisible())
		{
			player.sendPacket(SystemMessageId.THE_USER_WHO_REQUESTED_TO_BECOME_FRIENDS_IS_NOT_FOUND_IN_THE_GAME);
			return;
		}
		// You cannot add yourself to your own friend list.
		if (friend == player)
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_ADD_YOURSELF_TO_YOUR_OWN_FRIEND_LIST);
			return;
		}
		// Target is in olympiad.
		if (player.isInOlympiadMode() || friend.isInOlympiadMode())
		{
			player.sendPacket(SystemMessageId.A_USER_CURRENTLY_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_SEND_PARTY_AND_FRIEND_INVITATIONS);
			return;
		}
		
		// Cannot request friendship in an event.
		if (player.isOnEvent())
		{
			player.sendMessage("You cannot request friendship while participating in an event.");
			return;
		}
		
		// Target blocked active player.
		if (BlockList.isBlocked(friend, player))
		{
			player.sendMessage("You are in target's block list.");
			return;
		}
		SystemMessage sm;
		// Target is blocked.
		if (BlockList.isBlocked(player, friend))
		{
			sm = new SystemMessage(SystemMessageId.YOU_HAVE_BLOCKED_C1);
			sm.addString(friend.getName());
			player.sendPacket(sm);
			return;
		}
		
		// Target already in friend list.
		if (player.getFriendList().contains(friend.getObjectId()))
		{
			player.sendPacket(SystemMessageId.THIS_PLAYER_IS_ALREADY_REGISTERED_ON_YOUR_FRIENDS_LIST);
			return;
		}
		// Target is busy.
		if (friend.isProcessingRequest())
		{
			sm = new SystemMessage(SystemMessageId.C1_IS_ON_ANOTHER_TASK_PLEASE_TRY_AGAIN_LATER);
			sm.addString(_name);
			player.sendPacket(sm);
			return;
		}
		// Check, if tatget blocked sends requests in game.
		if (checkInviteByIgnoredSettings(friend, player))
		{
			player.sendPacket(new SystemMessage(SystemMessageId.PREFERENCES_IS_CONFIGURED_TO_REFUSE_FRIEND_REQUESTS_AND_THE_FRIEND_INVITATION_OF_C1_IS_AUTOMATICALLY_REJECTED).addPcName(friend));
			return;
		}
		// Friend request sent.
		player.onTransactionRequest(friend);
		friend.sendPacket(new FriendAddRequest(player.getName()));
		sm = new SystemMessage(SystemMessageId.YOU_VE_REQUESTED_C1_TO_BE_ON_YOUR_FRIENDS_LIST);
		sm.addString(_name);
		player.sendPacket(sm);
	}
	
	private boolean checkInviteByIgnoredSettings(Player target, Player requestor)
	{
		final ClientSettings targetClientSettings = target.getClientSettings();
		final boolean condition = targetClientSettings.isFriendRequestRestrictedFromOthers();
		if (condition && !targetClientSettings.isFriendRequestRestrictedFromClan() && (target.getClan() != null) && (requestor.getClan() != null) && (target.getClan() == requestor.getClan()))
		{
			return false;
		}
		return condition;
	}
}
