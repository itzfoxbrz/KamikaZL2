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
package ai.areas.Hellbound.HellboundMessenger;

import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * @author Mobius
 */
public class HellboundMessenger extends AbstractNpcAI
{
	// NPC
	private static final int MESSENGER = 34196;
	// Location
	private static final Location IVORY_TOWER = new Location(86722, 15389, -3515);
	// Misc
	private static final int MINIMUM_LEVEL = 85;
	
	private HellboundMessenger()
	{
		addStartNpc(MESSENGER);
		addTalkId(MESSENGER);
		addFirstTalkId(MESSENGER);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if ((npc.getId() == MESSENGER) && event.equals("teleport"))
		{
			if (player.getLevel() < MINIMUM_LEVEL)
			{
				return "34196-02.htm";
			}
			
			player.teleToLocation(IVORY_TOWER);
			return null;
		}
		
		return super.onEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "34196-01.htm";
	}
	
	public static void main(String[] args)
	{
		new HellboundMessenger();
	}
}
