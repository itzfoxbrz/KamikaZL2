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
package ai.others.Subjugation.Yand;

import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * @author Serenitty
 */
public class Yand extends AbstractNpcAI
{
	// NPC
	private static final int YAND = 34327;
	// Item
	private static final int MORGOS_MILITARY_SCROLL_MS = 90318605;
	// Location
	private static final Location TELEPORT_LOC = new Location(146915, -82589, -5128);
	
	private Yand()
	{
		addFirstTalkId(YAND);
		addTalkId(YAND);
		addSpawnId(YAND);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "GoToInsideMorgos":
			{
				final int military = player.getVariables().getInt("MORGOS_MILITARY_FREE", 1);
				if (military == 0)
				{
					return "34327-01.html";
				}
				player.teleToLocation(TELEPORT_LOC);
				player.getVariables().set("MORGOS_MILITARY_FREE", 0);
				break;
			}
			case "BuyScrollMorgos":
			{
				MultisellData.getInstance().separateAndSend(MORGOS_MILITARY_SCROLL_MS, player, null, false);
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "34327.html";
	}
	
	public static void main(String[] args)
	{
		new Yand();
	}
}
