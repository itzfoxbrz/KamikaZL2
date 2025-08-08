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
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class ShortBuffStatusUpdate extends ServerPacket
{
	public static final ShortBuffStatusUpdate RESET_SHORT_BUFF = new ShortBuffStatusUpdate(0, 0, 0, 0);
	
	private final int _skillId;
	private final int _skillLevel;
	private final int _skillSubLevel;
	private final int _duration;
	
	public ShortBuffStatusUpdate(int skillId, int skillLevel, int skillSubLevel, int duration)
	{
		_skillId = skillId;
		_skillLevel = skillLevel;
		_skillSubLevel = skillSubLevel;
		_duration = duration;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.SHORT_BUFF_STATUS_UPDATE.writeId(this, buffer);
		buffer.writeInt(_skillId);
		buffer.writeShort(_skillLevel);
		buffer.writeShort(_skillSubLevel);
		buffer.writeInt(_duration);
	}
}
