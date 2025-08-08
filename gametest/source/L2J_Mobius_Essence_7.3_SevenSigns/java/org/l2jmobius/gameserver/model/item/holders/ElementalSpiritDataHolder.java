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
 * @author JoeAlisson
 */
public class ElementalSpiritDataHolder
{
	private int _charId;
	private int _level = 1;
	private byte _type;
	private byte _stage = 1;
	private long _experience;
	private byte _attackPoints;
	private byte _defensePoints;
	private byte _critRatePoints;
	private byte _critDamagePoints;
	private boolean _inUse;
	
	public ElementalSpiritDataHolder()
	{
	}
	
	public ElementalSpiritDataHolder(byte type, int objectId)
	{
		_charId = objectId;
		_type = type;
	}
	
	public int getCharId()
	{
		return _charId;
	}
	
	public void setCharId(int charId)
	{
		_charId = charId;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public void setLevel(int level)
	{
		_level = level;
	}
	
	public byte getType()
	{
		return _type;
	}
	
	public void setType(byte type)
	{
		_type = type;
	}
	
	public byte getStage()
	{
		return _stage;
	}
	
	public void setStage(byte stage)
	{
		_stage = stage;
	}
	
	public long getExperience()
	{
		return _experience;
	}
	
	public void setExperience(long experience)
	{
		_experience = experience;
	}
	
	public byte getAttackPoints()
	{
		return _attackPoints;
	}
	
	public void setAttackPoints(byte attackPoints)
	{
		_attackPoints = attackPoints;
	}
	
	public byte getDefensePoints()
	{
		return _defensePoints;
	}
	
	public void setDefensePoints(byte defensePoints)
	{
		_defensePoints = defensePoints;
	}
	
	public byte getCritRatePoints()
	{
		return _critRatePoints;
	}
	
	public void setCritRatePoints(byte critRatePoints)
	{
		_critRatePoints = critRatePoints;
	}
	
	public byte getCritDamagePoints()
	{
		return _critDamagePoints;
	}
	
	public void setCritDamagePoints(byte critDamagePoints)
	{
		_critDamagePoints = critDamagePoints;
	}
	
	public void addExperience(long experience)
	{
		_experience += experience;
	}
	
	public void increaseLevel()
	{
		_level++;
	}
	
	public boolean isInUse()
	{
		return _inUse;
	}
	
	public void setInUse(boolean value)
	{
		_inUse = value;
	}
	
	public void addAttackPoints(byte attackPoints)
	{
		_attackPoints += attackPoints;
	}
	
	public void addDefensePoints(byte defensePoints)
	{
		_defensePoints += defensePoints;
	}
	
	public void addCritRatePoints(byte critRatePoints)
	{
		_critRatePoints += critRatePoints;
	}
	
	public void addCritDamagePoints(byte critDamagePoints)
	{
		_critDamagePoints += critDamagePoints;
	}
	
	public void increaseStage()
	{
		_stage++;
	}
}
