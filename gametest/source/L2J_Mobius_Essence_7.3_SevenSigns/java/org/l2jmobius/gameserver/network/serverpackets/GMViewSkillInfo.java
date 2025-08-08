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

import java.util.Collection;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class GMViewSkillInfo extends ServerPacket
{
	private final Player _player;
	private final Collection<Skill> _skills;
	
	public GMViewSkillInfo(Player player)
	{
		_player = player;
		_skills = _player.getSkillList();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.GM_VIEW_SKILL_INFO.writeId(this, buffer);
		buffer.writeString(_player.getName());
		buffer.writeInt(_skills.size());
		final boolean isDisabled = (_player.getClan() != null) && (_player.getClan().getReputationScore() < 0);
		for (Skill skill : _skills)
		{
			buffer.writeInt(skill.isPassive());
			buffer.writeShort(skill.getDisplayLevel());
			buffer.writeShort(skill.getSubLevel());
			buffer.writeInt(skill.getDisplayId());
			buffer.writeInt(0);
			buffer.writeByte(isDisabled && skill.isClanSkill());
			buffer.writeByte(skill.isEnchantable());
		}
	}
}