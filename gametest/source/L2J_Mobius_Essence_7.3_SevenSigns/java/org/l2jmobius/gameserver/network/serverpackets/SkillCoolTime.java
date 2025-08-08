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

import java.util.LinkedList;
import java.util.List;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.TimeStamp;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * Skill Cool Time server packet implementation.
 * @author Mobius
 */
public class SkillCoolTime extends ServerPacket
{
	private final List<TimeStamp> _reuseTimestamps = new LinkedList<>();
	
	public SkillCoolTime(Player player)
	{
		for (TimeStamp ts : player.getSkillReuseTimeStamps().values())
		{
			if (ts.hasNotPassed() && !SkillData.getInstance().getSkill(ts.getSkillId(), ts.getSkillLevel(), ts.getSkillSubLevel()).isNotBroadcastable())
			{
				_reuseTimestamps.add(ts);
			}
		}
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.SKILL_COOL_TIME.writeId(this, buffer);
		buffer.writeInt(_reuseTimestamps.size());
		for (TimeStamp ts : _reuseTimestamps)
		{
			final long reuse = ts.getReuse();
			final long remaining = ts.getRemaining();
			final int sharedReuseGroup = ts.getSharedReuseGroup();
			buffer.writeInt(sharedReuseGroup > 0 ? sharedReuseGroup : ts.getSkillId());
			buffer.writeInt(ts.getSkillLevel());
			buffer.writeInt((int) (reuse > 0 ? reuse : remaining) / 1000);
			buffer.writeInt(Math.max(1, (int) remaining / 1000));
		}
	}
}
