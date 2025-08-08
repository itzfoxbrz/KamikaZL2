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

import org.l2jmobius.Config;
import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class KeyPacket extends ServerPacket
{
	private final byte[] _key;
	private final int _result;
	
	public KeyPacket(byte[] key, int result)
	{
		_key = key;
		_result = result;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.VERSION_CHECK.writeId(this, buffer);
		buffer.writeByte(_result); // 0 - wrong protocol, 1 - protocol ok
		for (int i = 0; i < 8; i++)
		{
			buffer.writeByte(_key[i]); // key
		}
		buffer.writeInt(Config.PACKET_ENCRYPTION); // use blowfish encryption
		buffer.writeInt(Config.SERVER_ID); // server id
		buffer.writeByte(1);
		buffer.writeInt(0); // obfuscation key
		if ((Config.SERVER_LIST_TYPE & 0x1000) == 0x1000)
		{
			buffer.writeByte(4); // Aden
		}
		else if ((Config.SERVER_LIST_TYPE & 0x400) == 0x400)
		{
			buffer.writeByte(1); // Classic
		}
		else
		{
			buffer.writeByte(0); // Live
		}
		buffer.writeByte(0);
		buffer.writeByte(0);
	}
}
