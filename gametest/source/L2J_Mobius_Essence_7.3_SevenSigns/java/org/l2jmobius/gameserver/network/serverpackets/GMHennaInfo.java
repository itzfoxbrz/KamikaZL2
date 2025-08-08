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
package org.l2jmobius.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.henna.HennaPoten;
import org.l2jmobius.gameserver.model.stats.BaseStat;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * This server packet sends the player's henna information using the Game Master's UI.
 * @author Mobius
 */
public class GMHennaInfo extends ServerPacket
{
	private final Player _player;
	private final List<HennaPoten> _hennas = new ArrayList<>();
	
	public GMHennaInfo(Player player)
	{
		_player = player;
		for (HennaPoten henna : _player.getHennaPotenList())
		{
			if (henna != null)
			{
				_hennas.add(henna);
			}
		}
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.GMHENNA_INFO.writeId(this, buffer);
		buffer.writeShort(_player.getHennaValue(BaseStat.INT)); // equip INT
		buffer.writeShort(_player.getHennaValue(BaseStat.STR)); // equip STR
		buffer.writeShort(_player.getHennaValue(BaseStat.CON)); // equip CON
		buffer.writeShort(_player.getHennaValue(BaseStat.MEN)); // equip MEN
		buffer.writeShort(_player.getHennaValue(BaseStat.DEX)); // equip DEX
		buffer.writeShort(_player.getHennaValue(BaseStat.WIT)); // equip WIT
		buffer.writeShort(0); // equip LUC
		buffer.writeShort(0); // equip CHA
		buffer.writeInt(3); // Slots
		buffer.writeInt(_hennas.size()); // Size
		for (HennaPoten henna : _hennas)
		{
			buffer.writeInt(henna.getPotenId());
			buffer.writeInt(1);
		}
		buffer.writeInt(0);
		buffer.writeInt(0);
		buffer.writeInt(0);
	}
}
