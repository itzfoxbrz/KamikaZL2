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

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class DropItem extends ServerPacket
{
	private final Item _item;
	private final int _objectId;
	
	/**
	 * Constructor of the DropItem server packet
	 * @param item : Item designating the item
	 * @param playerObjId : int designating the player ID who dropped the item
	 */
	public DropItem(Item item, int playerObjId)
	{
		_item = item;
		_objectId = playerObjId;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.DROP_ITEM.writeId(this, buffer);
		buffer.writeInt(_objectId);
		buffer.writeInt(_item.getObjectId());
		buffer.writeInt(_item.getDisplayId());
		buffer.writeInt(_item.getX());
		buffer.writeInt(_item.getY());
		buffer.writeInt(_item.getZ());
		// only show item count if it is a stackable item
		buffer.writeByte(_item.isStackable());
		buffer.writeLong(_item.getCount());
		buffer.writeInt(0);
		buffer.writeByte(_item.getEnchantLevel() > 0);
		buffer.writeInt(0);
		buffer.writeByte(_item.getEnchantLevel()); // Grand Crusade
		buffer.writeByte(_item.getAugmentation() != null); // Grand Crusade
		buffer.writeByte(_item.getSpecialAbilities().size()); // Grand Crusade
		buffer.writeByte(0);
	}
}
