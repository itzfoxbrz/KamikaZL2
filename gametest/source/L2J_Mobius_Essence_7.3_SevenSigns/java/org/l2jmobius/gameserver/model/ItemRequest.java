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
package org.l2jmobius.gameserver.model;

public class ItemRequest
{
	int _objectId;
	int _itemId;
	long _count;
	long _price;
	
	public ItemRequest(int objectId, long count, long price)
	{
		_objectId = objectId;
		_count = count;
		_price = price;
	}
	
	public ItemRequest(int objectId, int itemId, long count, long price)
	{
		_objectId = objectId;
		_itemId = itemId;
		_count = count;
		_price = price;
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public void setCount(long count)
	{
		_count = count;
	}
	
	public long getCount()
	{
		return _count;
	}
	
	public long getPrice()
	{
		return _price;
	}
	
	@Override
	public int hashCode()
	{
		return _objectId;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!(obj instanceof ItemRequest))
		{
			return false;
		}
		return (_objectId != ((ItemRequest) obj)._objectId);
	}
}
