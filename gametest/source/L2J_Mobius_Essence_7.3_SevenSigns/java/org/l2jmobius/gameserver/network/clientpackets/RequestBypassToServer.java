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
package org.l2jmobius.gameserver.network.clientpackets;

import java.util.StringTokenizer;

import org.l2jmobius.Config;
import org.l2jmobius.commons.util.StringUtil;
import org.l2jmobius.commons.util.TraceUtil;
import org.l2jmobius.gameserver.ai.Intention;
import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.handler.AdminCommandHandler;
import org.l2jmobius.gameserver.handler.BypassHandler;
import org.l2jmobius.gameserver.handler.CommunityBoardHandler;
import org.l2jmobius.gameserver.handler.IBypassHandler;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.WorldObject;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.events.EventDispatcher;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcManorBypass;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcMenuSelect;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerBypass;
import org.l2jmobius.gameserver.model.events.returns.TerminateReturn;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.olympiad.Hero;
import org.l2jmobius.gameserver.network.Disconnection;
import org.l2jmobius.gameserver.network.PacketLogger;
import org.l2jmobius.gameserver.network.serverpackets.ActionFailed;
import org.l2jmobius.gameserver.network.serverpackets.LeaveWorld;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jmobius.gameserver.util.LocationUtil;

/**
 * @version $Revision: 1.12.4.5 $ $Date: 2005/04/11 10:06:11 $
 */
public class RequestBypassToServer extends ClientPacket
{
	private static final String[] _possibleNonHtmlCommands =
	{
		"_bbs",
		"bbs",
		"_mail",
		"_friend",
		"_match",
		"_diary",
		"_olympiad?command",
		"menu_select",
		"manor_menu_select",
		"pccafe"
	};
	
	// S
	private String _command;
	
	@Override
	protected void readImpl()
	{
		_command = readString();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (_command.isEmpty())
		{
			PacketLogger.warning(player + " sent empty bypass!");
			Disconnection.of(getClient(), player).defaultSequence(LeaveWorld.STATIC_PACKET);
			return;
		}
		
		boolean requiresBypassValidation = true;
		for (String possibleNonHtmlCommand : _possibleNonHtmlCommands)
		{
			if (_command.startsWith(possibleNonHtmlCommand))
			{
				requiresBypassValidation = false;
				break;
			}
		}
		
		int bypassOriginId = 0;
		if (requiresBypassValidation)
		{
			bypassOriginId = player.validateHtmlAction(_command);
			if (bypassOriginId == -1)
			{
				return;
			}
			
			if ((bypassOriginId > 0) && !LocationUtil.isInsideRangeOfObjectId(player, bypassOriginId, Npc.INTERACTION_DISTANCE))
			{
				// No logging here, this could be a common case where the player has the html still open and run too far away and then clicks a html action
				return;
			}
		}
		
		if (!getClient().getFloodProtectors().canUseServerBypass())
		{
			return;
		}
		
		if (EventDispatcher.getInstance().hasListener(EventType.ON_PLAYER_BYPASS, player))
		{
			final TerminateReturn terminateReturn = EventDispatcher.getInstance().notifyEvent(new OnPlayerBypass(player, _command), player, TerminateReturn.class);
			if ((terminateReturn != null) && terminateReturn.terminate())
			{
				return;
			}
		}
		
		try
		{
			if (_command.startsWith("admin_"))
			{
				AdminCommandHandler.getInstance().useAdminCommand(player, _command, true);
			}
			else if (CommunityBoardHandler.getInstance().isCommunityBoardCommand(_command))
			{
				CommunityBoardHandler.getInstance().handleParseCommand(_command, player);
			}
			else if (_command.equals("come_here") && player.isGM())
			{
				comeHere(player);
			}
			else if (_command.startsWith("npc_"))
			{
				final int endOfId = _command.indexOf('_', 5);
				String id;
				if (endOfId > 0)
				{
					id = _command.substring(4, endOfId);
				}
				else
				{
					id = _command.substring(4);
				}
				
				if (StringUtil.isNumeric(id))
				{
					final WorldObject object = World.getInstance().findObject(Integer.parseInt(id));
					if ((object != null) && object.isNpc() && (endOfId > 0) && player.isInsideRadius2D(object, Npc.INTERACTION_DISTANCE))
					{
						object.asNpc().onBypassFeedback(player, _command.substring(endOfId + 1));
					}
				}
				
				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
			else if (_command.startsWith("item_"))
			{
				final int endOfId = _command.indexOf('_', 5);
				String id;
				if (endOfId > 0)
				{
					id = _command.substring(5, endOfId);
				}
				else
				{
					id = _command.substring(5);
				}
				try
				{
					final Item item = player.getInventory().getItemByObjectId(Integer.parseInt(id));
					if ((item != null) && (endOfId > 0))
					{
						item.onBypassFeedback(player, _command.substring(endOfId + 1));
					}
					
					player.sendPacket(ActionFailed.STATIC_PACKET);
				}
				catch (NumberFormatException nfe)
				{
					PacketLogger.warning("NFE for command [" + _command + "] " + nfe.getMessage());
				}
			}
			else if (_command.startsWith("_match"))
			{
				final String params = _command.substring(_command.indexOf('?') + 1);
				final StringTokenizer st = new StringTokenizer(params, "&");
				final int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
				final int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
				final int heroid = Hero.getInstance().getHeroByClass(heroclass);
				if (heroid > 0)
				{
					Hero.getInstance().showHeroFights(player, heroclass, heroid, heropage);
				}
			}
			else if (_command.startsWith("_diary"))
			{
				final String params = _command.substring(_command.indexOf('?') + 1);
				final StringTokenizer st = new StringTokenizer(params, "&");
				final int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
				final int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
				final int heroid = Hero.getInstance().getHeroByClass(heroclass);
				if (heroid > 0)
				{
					Hero.getInstance().showHeroDiary(player, heroclass, heroid, heropage);
				}
			}
			else if (_command.startsWith("_olympiad?command"))
			{
				final int arenaId = Integer.parseInt(_command.split("=")[2]);
				final IBypassHandler handler = BypassHandler.getInstance().getHandler("arenachange");
				if (handler != null)
				{
					handler.useBypass("arenachange " + (arenaId - 1), player, null);
				}
			}
			else if (_command.startsWith("menu_select"))
			{
				final Npc lastNpc = player.getLastFolkNPC();
				if ((lastNpc != null) && lastNpc.canInteract(player) && EventDispatcher.getInstance().hasListener(EventType.ON_NPC_MENU_SELECT, lastNpc))
				{
					final String[] split = _command.substring(_command.indexOf('?') + 1).split("&");
					final int ask = Integer.parseInt(split[0].split("=")[1]);
					final int reply = Integer.parseInt(split[1].split("=")[1]);
					EventDispatcher.getInstance().notifyEventAsync(new OnNpcMenuSelect(player, lastNpc, ask, reply), lastNpc);
				}
			}
			else if (_command.startsWith("manor_menu_select"))
			{
				final Npc lastNpc = player.getLastFolkNPC();
				if (Config.ALLOW_MANOR && (lastNpc != null) && lastNpc.canInteract(player) && EventDispatcher.getInstance().hasListener(EventType.ON_NPC_MANOR_BYPASS, lastNpc))
				{
					final String[] split = _command.substring(_command.indexOf('?') + 1).split("&");
					final int ask = Integer.parseInt(split[0].split("=")[1]);
					final int state = Integer.parseInt(split[1].split("=")[1]);
					final boolean time = split[2].split("=")[1].equals("1");
					EventDispatcher.getInstance().notifyEventAsync(new OnNpcManorBypass(player, lastNpc, ask, state, time), lastNpc);
				}
			}
			else if (_command.startsWith("pccafe"))
			{
				if (!Config.PC_CAFE_ENABLED)
				{
					return;
				}
				final int multisellId = Integer.parseInt(_command.substring(10).trim());
				MultisellData.getInstance().separateAndSend(multisellId, player, null, false);
			}
			else
			{
				final IBypassHandler handler = BypassHandler.getInstance().getHandler(_command);
				if (handler != null)
				{
					if (bypassOriginId > 0)
					{
						final WorldObject bypassOrigin = World.getInstance().findObject(bypassOriginId);
						if ((bypassOrigin != null) && bypassOrigin.isCreature())
						{
							handler.useBypass(_command, player, bypassOrigin.asCreature());
						}
						else
						{
							handler.useBypass(_command, player, null);
						}
					}
					else
					{
						handler.useBypass(_command, player, null);
					}
				}
				else
				{
					PacketLogger.warning(getClient() + " sent not handled RequestBypassToServer: [" + _command + "]");
				}
			}
		}
		catch (Exception e)
		{
			PacketLogger.warning("Exception processing bypass from " + player + ": " + _command + " " + e.getMessage());
			PacketLogger.warning(TraceUtil.getStackTrace(e));
			if (player.isGM())
			{
				final StringBuilder sb = new StringBuilder(200);
				sb.append("<html><body>");
				sb.append("Bypass error: " + e + "<br1>");
				sb.append("Bypass command: " + _command + "<br1>");
				sb.append("StackTrace:<br1>");
				for (StackTraceElement ste : e.getStackTrace())
				{
					sb.append(ste + "<br1>");
				}
				sb.append("</body></html>");
				// item html
				final NpcHtmlMessage msg = new NpcHtmlMessage(0, 1, sb.toString());
				msg.disableValidation();
				player.sendPacket(msg);
			}
		}
	}
	
	/**
	 * @param player
	 */
	private void comeHere(Player player)
	{
		final WorldObject obj = player.getTarget();
		if (obj == null)
		{
			return;
		}
		if (obj.isNpc())
		{
			final Npc temp = obj.asNpc();
			temp.setTarget(player);
			temp.getAI().setIntention(Intention.MOVE_TO, player.getLocation());
		}
	}
}
