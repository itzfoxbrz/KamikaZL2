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
package org.l2jmobius.gameserver.model.actor.holders.npc;

import org.l2jmobius.gameserver.model.actor.enums.npc.DropType;

/**
 * @author Mobius
 */
public class DropHolder
{
	private final DropType _dropType;
	private final int _itemId;
	private final long _min;
	private final long _max;
	private final double _chance;
	
	public DropHolder(DropType dropType, int itemId, long min, long max, double chance)
	{
		_dropType = dropType;
		_itemId = itemId;
		_min = min;
		_max = max;
		_chance = chance;
	}
	
	public DropType getDropType()
	{
		return _dropType;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public long getMin()
	{
		return _min;
	}
	
	public long getMax()
	{
		return _max;
	}
	
	public double getChance()
	{
		return _chance;
	}
}
