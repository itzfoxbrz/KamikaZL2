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

public class CharCreateFail extends ServerPacket
{
	// TODO: Enum
	public static final int REASON_CREATION_FAILED = 0x00; // "Your character creation has failed."
	public static final int REASON_TOO_MANY_CHARACTERS = 0x01; // "You cannot create another character. Please delete the existing character and try again." Removes all settings that were selected (race, class, etc).
	public static final int REASON_NAME_ALREADY_EXISTS = 0x02; // "This name already exists."
	public static final int REASON_16_ENG_CHARS = 0x03; // "Your title cannot exceed 16 characters in length. Please try again."
	public static final int REASON_INCORRECT_NAME = 0x04; // "Incorrect name. Please try again."
	public static final int REASON_CREATE_NOT_ALLOWED = 0x05; // "Characters cannot be created from this server."
	public static final int REASON_CHOOSE_ANOTHER_SVR = 0x06; // "Unable to create character. You are unable to create a new character on the selected server. A restriction is in place which restricts users from creating characters on different servers where no previous character exists. Please
																// choose another server."
	private final int _error;
	
	public CharCreateFail(int errorCode)
	{
		_error = errorCode;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.CHARACTER_CREATE_FAIL.writeId(this, buffer);
		buffer.writeInt(_error);
	}
}
