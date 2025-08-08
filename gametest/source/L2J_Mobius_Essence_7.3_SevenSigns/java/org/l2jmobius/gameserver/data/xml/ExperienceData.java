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
package org.l2jmobius.gameserver.data.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

import org.l2jmobius.Config;
import org.l2jmobius.commons.util.IXmlReader;

/**
 * This class holds the Experience points for each level for players and pets.
 * <p>
 * It provides functionality to load experience data from an XML file and retrieve the experience points required for a given level. It also maintains the maximum levels for players and pets.
 * </p>
 * <p>
 * <strong>XML Structure:</strong>
 * </p>
 * The XML file must have a root element containing the `maxLevel` and `maxPetLevel` attributes, along with nested `experience` elements that define the experience points for each level.
 * @author Mobius
 */
public class ExperienceData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(ExperienceData.class.getName());
	
	private final Map<Integer, Long> _expTable = new HashMap<>();
	private final Map<Integer, Double> _traningRateTable = new HashMap<>();
	
	private int _maxLevel;
	private int _maxPetLevel;
	
	protected ExperienceData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_expTable.clear();
		_traningRateTable.clear();
		parseDatapackFile("data/stats/experience.xml");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _expTable.size() + " levels.");
		LOGGER.info(getClass().getSimpleName() + ": Max Player Level is " + (_maxLevel - 1) + ".");
		LOGGER.info(getClass().getSimpleName() + ": Max Pet Level is " + (_maxPetLevel - 1) + ".");
	}
	
	@Override
	public void parseDocument(Document document, File file)
	{
		forEach(document, tableNode ->
		{
			final NamedNodeMap tableAttr = tableNode.getAttributes();
			_maxLevel = Integer.parseInt(tableAttr.getNamedItem("maxLevel").getNodeValue()) + 1;
			_maxPetLevel = Integer.parseInt(tableAttr.getNamedItem("maxPetLevel").getNodeValue()) + 1;
			if (_maxLevel > Config.PLAYER_MAXIMUM_LEVEL)
			{
				_maxLevel = Config.PLAYER_MAXIMUM_LEVEL;
			}
			if (_maxPetLevel > (_maxLevel + 1))
			{
				_maxPetLevel = _maxLevel + 1; // Pet level should not exceed owner level.
			}
			
			forEach(tableNode, "experience", experienceNode ->
			{
				final NamedNodeMap attrs = experienceNode.getAttributes();
				final int level = parseInteger(attrs, "level");
				if ((level > Config.PLAYER_MAXIMUM_LEVEL) || (level > _maxLevel))
				{
					return;
				}
				
				_expTable.put(level, parseLong(attrs, "tolevel"));
				_traningRateTable.put(level, parseDouble(attrs, "trainingRate"));
			});
		});
	}
	
	/**
	 * Retrieves the experience points required to reach the specified level.
	 * @param level the level for which experience points are required
	 * @return the experience points needed to reach the specified level; if the specified level exceeds {@code Config.PLAYER_MAXIMUM_LEVEL}, returns the experience points for {@code Config.PLAYER_MAXIMUM_LEVEL}
	 */
	public long getExpForLevel(int level)
	{
		if (level > Config.PLAYER_MAXIMUM_LEVEL)
		{
			return _expTable.get(Config.PLAYER_MAXIMUM_LEVEL);
		}
		
		return _expTable.get(level);
	}
	
	/**
	 * Retrieves the training rate for a specified level.
	 * @param level the level for which the training rate is required
	 * @return the training rate at the specified level; if the specified level exceeds {@code Config.PLAYER_MAXIMUM_LEVEL}, returns the training rate for {@code Config.PLAYER_MAXIMUM_LEVEL}
	 */
	public double getTrainingRate(int level)
	{
		if (level > Config.PLAYER_MAXIMUM_LEVEL)
		{
			return _traningRateTable.get(Config.PLAYER_MAXIMUM_LEVEL);
		}
		
		return _traningRateTable.get(level);
	}
	
	/**
	 * Returns the maximum level acquirable by a player in the game.
	 * @return the maximum player level as an integer
	 */
	public int getMaxLevel()
	{
		return _maxLevel;
	}
	
	/**
	 * Returns the maximum level acquirable by a pet in the game.
	 * @return the maximum pet level as an integer, limited by the player level
	 */
	public int getMaxPetLevel()
	{
		return _maxPetLevel;
	}
	
	public static ExperienceData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ExperienceData INSTANCE = new ExperienceData();
	}
}
