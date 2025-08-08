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
import org.l2jmobius.gameserver.model.actor.enums.player.MacroUpdateType;
import org.l2jmobius.gameserver.model.actor.holders.player.Macro;
import org.l2jmobius.gameserver.model.actor.holders.player.MacroCmd;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class SendMacroList extends ServerPacket
{
	private final int _count;
	private final Macro _macro;
	private final MacroUpdateType _updateType;
	
	public SendMacroList(int count, Macro macro, MacroUpdateType updateType)
	{
		_count = count;
		_macro = macro;
		_updateType = updateType;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.MACRO_LIST.writeId(this, buffer);
		buffer.writeByte(_updateType.getId());
		buffer.writeInt(_updateType != MacroUpdateType.LIST ? _macro.getId() : 0); // modified, created or deleted macro's id
		buffer.writeByte(_count); // count of Macros
		buffer.writeByte(_macro != null); // unknown
		if ((_macro != null) && (_updateType != MacroUpdateType.DELETE))
		{
			buffer.writeInt(_macro.getId()); // Macro ID
			buffer.writeString(_macro.getName()); // Macro Name
			buffer.writeString(_macro.getDescr()); // Desc
			buffer.writeString(_macro.getAcronym()); // acronym
			buffer.writeInt(_macro.getIcon()); // icon
			buffer.writeByte(_macro.getCommands().size()); // count
			int i = 1;
			for (MacroCmd cmd : _macro.getCommands())
			{
				buffer.writeByte(i++); // command count
				buffer.writeByte(cmd.getType().ordinal()); // type 1 = skill, 3 = action, 4 = shortcut
				buffer.writeInt(cmd.getD1()); // skill id
				buffer.writeByte(cmd.getD2()); // shortcut id
				buffer.writeString(cmd.getCmd()); // command name
			}
		}
	}
}
