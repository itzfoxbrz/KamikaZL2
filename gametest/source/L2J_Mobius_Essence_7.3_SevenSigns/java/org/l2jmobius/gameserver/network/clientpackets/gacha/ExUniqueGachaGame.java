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
package org.l2jmobius.gameserver.network.clientpackets.gacha;

import java.util.List;
import java.util.Map.Entry;

import org.l2jmobius.gameserver.managers.events.UniqueGachaManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.holders.GachaItemHolder;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.gacha.UniqueGachaGame;
import org.l2jmobius.gameserver.network.serverpackets.gacha.UniqueGachaInvenAddItem;

public class ExUniqueGachaGame extends ClientPacket
{
	private int _gameCount;
	
	@Override
	protected void readImpl()
	{
		_gameCount = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Entry<List<GachaItemHolder>, Boolean> pair = UniqueGachaManager.getInstance().tryToRoll(player, _gameCount);
		final List<GachaItemHolder> rewards = pair.getKey();
		final boolean rare = pair.getValue().booleanValue();
		player.sendPacket(new UniqueGachaGame(rewards.isEmpty() ? UniqueGachaGame.FAILURE : UniqueGachaGame.SUCCESS, player, rewards, rare));
		player.sendPacket(new UniqueGachaInvenAddItem(rewards));
	}
}