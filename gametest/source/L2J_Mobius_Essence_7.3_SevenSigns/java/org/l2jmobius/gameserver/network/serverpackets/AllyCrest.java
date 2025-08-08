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
import org.l2jmobius.gameserver.data.sql.CrestTable;
import org.l2jmobius.gameserver.model.Crest;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class AllyCrest extends ServerPacket
{
	private final int _clanId;
	private final int _crestId;
	private final byte[] _data;
	
	public AllyCrest(int crestId, int clanId)
	{
		_crestId = crestId;
		_clanId = clanId;
		final Crest crest = CrestTable.getInstance().getCrest(crestId);
		_data = crest != null ? crest.getData() : null;
	}
	
	public AllyCrest(int crestId, int clanId, byte[] data)
	{
		_crestId = crestId;
		_clanId = clanId;
		_data = data;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.ALLIANCE_CREST.writeId(this, buffer);
		buffer.writeInt(_crestId);
		buffer.writeInt(_clanId);
		if (_data != null)
		{
			buffer.writeInt(_data.length);
			buffer.writeInt(_data.length);
			buffer.writeBytes(_data);
		}
		else
		{
			buffer.writeInt(0);
		}
	}
}
