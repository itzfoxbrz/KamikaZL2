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
package org.l2jmobius.gameserver.model.item.holders;

/**
 * @author Index, Mobius
 */
public class UniqueItemEnchantHolder extends ItemEnchantHolder
{
	private final int _objectId;
	
	public UniqueItemEnchantHolder(int id, int objectId)
	{
		this(id, objectId, 1);
	}
	
	public UniqueItemEnchantHolder(int id, int objectId, long count)
	{
		super(id, count);
		_objectId = objectId;
	}
	
	public UniqueItemEnchantHolder(ItemEnchantHolder itemHolder, int objectId)
	{
		super(itemHolder.getId(), itemHolder.getCount(), itemHolder.getEnchantLevel());
		_objectId = objectId;
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	@Override
	public String toString()
	{
		return "[" + getClass().getSimpleName() + "] ID: " + getId() + ", object ID: " + _objectId + ", count: " + getCount() + ", enchant level: " + getEnchantLevel();
	}
}
