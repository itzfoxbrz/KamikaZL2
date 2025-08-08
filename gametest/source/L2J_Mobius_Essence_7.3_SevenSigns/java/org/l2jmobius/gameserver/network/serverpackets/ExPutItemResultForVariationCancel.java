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

public class ExPutItemResultForVariationCancel extends ServerPacket
{
	private final int _itemObjId;
	private final int _itemId;
	private final int _itemAug1;
	private final int _itemAug2;
	private final long _price;
	
	public ExPutItemResultForVariationCancel(Item item, long price)
	{
		_itemObjId = item.getObjectId();
		_itemId = item.getDisplayId();
		_price = price;
		_itemAug1 = item.getAugmentation().getOption1Id();
		_itemAug2 = item.getAugmentation().getOption2Id();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PUT_ITEM_RESULT_FOR_VARIATION_CANCEL.writeId(this, buffer);
		buffer.writeInt(_itemObjId);
		buffer.writeInt(_itemId);
		buffer.writeInt(_itemAug1);
		buffer.writeInt(_itemAug2);
		buffer.writeLong(_price);
		buffer.writeInt(1);
	}
}
