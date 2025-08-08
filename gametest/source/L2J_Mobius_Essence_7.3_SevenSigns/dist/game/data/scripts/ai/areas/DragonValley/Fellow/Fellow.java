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
package ai.areas.DragonValley.Fellow;

import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.enums.ChatType;
import org.l2jmobius.gameserver.network.serverpackets.NpcSay;

import ai.AbstractNpcAI;

/**
 * @author RobikBobik
 */
public class Fellow extends AbstractNpcAI
{
	// NPC
	private static final int FELLOW = 31713;
	// Location - Antharas Heart
	private static final Location TELEPORT_LOC = new Location(154376, 121290, -3807);
	// Misc
	private static final NpcStringId[] TEXT =
	{
		NpcStringId.LET_S_JOIN_OUR_FORCES_AND_FACE_THIS_TOGETHER,
		NpcStringId.BALTHUS_KNIGHTS_ARE_LOOKING_FOR_MERCENARIES
	};
	
	private Fellow()
	{
		addFirstTalkId(FELLOW);
		addTalkId(FELLOW);
		addSpawnId(FELLOW);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "teleToAntharas":
			{
				if ((player.getCommandChannel() == null) || (player.getCommandChannel().getLeader() != player) || (player.getCommandChannel().getMemberCount() < 27) || (player.getCommandChannel().getMemberCount() > 300))
				{
					return "31713-01.html";
				}
				for (Player member : player.getCommandChannel().getMembers())
				{
					if ((member != null) && (member.getLevel() > 70))
					{
						member.teleToLocation(TELEPORT_LOC);
					}
				}
				break;
			}
			case "CHAT_TIMER":
			{
				npc.broadcastPacket(new NpcSay(npc, ChatType.NPC_GENERAL, TEXT[getRandom(TEXT.length)]));
				startQuestTimer("CHAT_TIMER", 30000, npc, null);
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "31713.html";
	}
	
	@Override
	public void onSpawn(Npc npc)
	{
		startQuestTimer("CHAT_TIMER", 5000, npc, null);
	}
	
	public static void main(String[] args)
	{
		new Fellow();
	}
}
