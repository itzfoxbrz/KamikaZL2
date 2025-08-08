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
package org.l2jmobius.gameserver.model.actor.holders.player;

import org.l2jmobius.gameserver.model.actor.enums.player.ShortcutType;

public class Shortcut
{
	/** Slot from 0 to 11. */
	private final int _slot;
	/** Page from 0 to 9. */
	private final int _page;
	/** Type: item, skill, action, macro, recipe, bookmark. */
	private final ShortcutType _type;
	/** Shortcut ID. */
	private final int _id;
	/** Shortcut level (skills). */
	private final int _level;
	/** Shortcut level (skills). */
	private final int _subLevel;
	/** Character type: 1 player, 2 summon. */
	private final int _characterType;
	/** Shared reuse group. */
	private int _sharedReuseGroup = -1;
	/** Auto use enabled. */
	private boolean _autoUse = false;
	
	public Shortcut(int slot, int page, ShortcutType type, int id, int level, int subLevel, int characterType)
	{
		_slot = slot;
		_page = page;
		_type = type;
		_id = id;
		_level = level;
		_subLevel = subLevel;
		_characterType = characterType;
	}
	
	/**
	 * Gets the shortcut ID.
	 * @return the ID
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Gets the shortcut level.
	 * @return the level
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Gets the shortcut level.
	 * @return the level
	 */
	public int getSubLevel()
	{
		return _subLevel;
	}
	
	/**
	 * Gets the shortcut page.
	 * @return the page
	 */
	public int getPage()
	{
		return _page;
	}
	
	/**
	 * Gets the shortcut slot.
	 * @return the slot
	 */
	public int getSlot()
	{
		return _slot;
	}
	
	/**
	 * Gets the shortcut type.
	 * @return the type
	 */
	public ShortcutType getType()
	{
		return _type;
	}
	
	/**
	 * Gets the shortcut character type.
	 * @return the character type
	 */
	public int getCharacterType()
	{
		return _characterType;
	}
	
	/**
	 * Gets the shared reuse group.
	 * @return the shared reuse group
	 */
	public int getSharedReuseGroup()
	{
		return _sharedReuseGroup;
	}
	
	/**
	 * Sets the shared reuse group.
	 * @param sharedReuseGroup the shared reuse group to set
	 */
	public void setSharedReuseGroup(int sharedReuseGroup)
	{
		_sharedReuseGroup = sharedReuseGroup;
	}
	
	/**
	 * Gets is shortcut is auto use enabled.
	 * @return if shortcut is auto use enabled.
	 */
	public boolean isAutoUse()
	{
		return _autoUse;
	}
	
	/**
	 * Sets the auto use status.
	 * @param value of shortcut auto use status.
	 */
	public void setAutoUse(boolean value)
	{
		_autoUse = value;
	}
}
