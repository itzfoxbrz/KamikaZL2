/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jmobius.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.l2jmobius.commons.util.IXmlReader;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.enums.player.MacroType;
import org.l2jmobius.gameserver.model.actor.enums.player.PlayerClass;
import org.l2jmobius.gameserver.model.actor.enums.player.ShortcutType;
import org.l2jmobius.gameserver.model.actor.holders.player.Macro;
import org.l2jmobius.gameserver.model.actor.holders.player.MacroCmd;
import org.l2jmobius.gameserver.model.actor.holders.player.Shortcut;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.serverpackets.ShortcutRegister;

/**
 * @author Zoey76
 */
public class InitialShortcutData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(InitialShortcutData.class.getName());
	
	private final Map<PlayerClass, List<Shortcut>> _initialShortcutData = new EnumMap<>(PlayerClass.class);
	private final List<Shortcut> _initialGlobalShortcutList = new ArrayList<>();
	private final Map<Integer, Macro> _macroPresets = new HashMap<>();
	
	protected InitialShortcutData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_initialShortcutData.clear();
		_initialGlobalShortcutList.clear();
		
		parseDatapackFile("data/stats/initialShortcuts.xml");
		
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _initialGlobalShortcutList.size() + " initial global shortcuts data.");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _initialShortcutData.size() + " initial shortcuts data.");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _macroPresets.size() + " macro presets.");
	}
	
	@Override
	public void parseDocument(Document document, File file)
	{
		for (Node n = document.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equals(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					switch (d.getNodeName())
					{
						case "shortcuts":
						{
							parseShortcuts(d);
							break;
						}
						case "macros":
						{
							parseMacros(d);
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Parses a shortcut.
	 * @param d the node
	 */
	private void parseShortcuts(Node d)
	{
		NamedNodeMap attrs = d.getAttributes();
		final Node classIdNode = attrs.getNamedItem("classId");
		final List<Shortcut> list = new ArrayList<>();
		for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling())
		{
			if ("page".equals(c.getNodeName()))
			{
				attrs = c.getAttributes();
				final int pageId = parseInteger(attrs, "pageId");
				for (Node b = c.getFirstChild(); b != null; b = b.getNextSibling())
				{
					if ("slot".equals(b.getNodeName()))
					{
						list.add(createShortcut(pageId, b));
					}
				}
			}
		}
		
		if (classIdNode != null)
		{
			_initialShortcutData.put(PlayerClass.getPlayerClass(Integer.parseInt(classIdNode.getNodeValue())), list);
		}
		else
		{
			_initialGlobalShortcutList.addAll(list);
		}
	}
	
	/**
	 * Parses a macro.
	 * @param d the node
	 */
	private void parseMacros(Node d)
	{
		for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling())
		{
			if ("macro".equals(c.getNodeName()))
			{
				NamedNodeMap attrs = c.getAttributes();
				if (!parseBoolean(attrs, "enabled", true))
				{
					continue;
				}
				
				final int macroId = parseInteger(attrs, "macroId");
				final int icon = parseInteger(attrs, "icon");
				final String name = parseString(attrs, "name");
				final String description = parseString(attrs, "description");
				final String acronym = parseString(attrs, "acronym");
				final List<MacroCmd> commands = new ArrayList<>(1);
				int entry = 0;
				for (Node b = c.getFirstChild(); b != null; b = b.getNextSibling())
				{
					if ("command".equals(b.getNodeName()))
					{
						attrs = b.getAttributes();
						final MacroType type = parseEnum(attrs, MacroType.class, "type");
						int d1 = 0;
						int d2 = 0;
						final String cmd = b.getTextContent();
						switch (type)
						{
							case SKILL:
							{
								d1 = parseInteger(attrs, "skillId"); // Skill ID
								d2 = parseInteger(attrs, "skillLevel", 0); // Skill level
								break;
							}
							case ACTION:
							{
								// Not handled by client.
								d1 = parseInteger(attrs, "actionId");
								break;
							}
							case TEXT:
							{
								// Doesn't have numeric parameters.
								break;
							}
							case SHORTCUT:
							{
								d1 = parseInteger(attrs, "page"); // Page
								d2 = parseInteger(attrs, "slot", 0); // Slot
								break;
							}
							case ITEM:
							{
								// Not handled by client.
								d1 = parseInteger(attrs, "itemId");
								break;
							}
							case DELAY:
							{
								d1 = parseInteger(attrs, "delay"); // Delay in seconds
								break;
							}
						}
						commands.add(new MacroCmd(entry++, type, d1, d2, cmd));
					}
				}
				_macroPresets.put(macroId, new Macro(macroId, icon, name, description, acronym, commands));
			}
		}
	}
	
	/**
	 * Parses a node an create a shortcut from it.
	 * @param pageId the page ID
	 * @param b the node to parse
	 * @return the new shortcut
	 */
	private Shortcut createShortcut(int pageId, Node b)
	{
		final NamedNodeMap attrs = b.getAttributes();
		final int slotId = parseInteger(attrs, "slotId");
		final ShortcutType shortcutType = parseEnum(attrs, ShortcutType.class, "shortcutType");
		final int shortcutId = parseInteger(attrs, "shortcutId");
		final int shortcutLevel = parseInteger(attrs, "shortcutLevel", 0);
		final int characterType = parseInteger(attrs, "characterType", 0);
		return new Shortcut(slotId, pageId, shortcutType, shortcutId, shortcutLevel, 0, characterType);
	}
	
	/**
	 * Retrieves the list of shortcuts specific to the given class ID.
	 * @param cId the {@link PlayerClass} for which to retrieve the shortcut list.
	 * @return a list of {@link Shortcut} objects for the specified class ID, or {@code null} if no shortcuts are found.
	 */
	public List<Shortcut> getShortcutList(PlayerClass cId)
	{
		return _initialShortcutData.get(cId);
	}
	
	/**
	 * Retrieves the list of shortcuts specific to the given class ID represented as an integer.
	 * @param cId the integer ID of the class for which to retrieve the shortcut list.
	 * @return a list of {@link Shortcut} objects for the specified class ID, or {@code null} if no shortcuts are found.
	 */
	public List<Shortcut> getShortcutList(int cId)
	{
		return _initialShortcutData.get(PlayerClass.getPlayerClass(cId));
	}
	
	/**
	 * Retrieves the global list of shortcuts available to all players.
	 * @return a list of global {@link Shortcut} objects.
	 */
	public List<Shortcut> getGlobalMacroList()
	{
		return _initialGlobalShortcutList;
	}
	
	/**
	 * Registers all available shortcuts for the specified player, including global shortcuts and those specific to the player's class.<br>
	 * This method ensures that item, skill, and macro shortcuts are correctly associated with the player, creating new shortcuts based on the player's current inventory, skills, and available macros.
	 * @param player the {@link Player} for whom to register the shortcuts.
	 */
	public void registerAllShortcuts(Player player)
	{
		if (player == null)
		{
			return;
		}
		
		// Register global shortcuts.
		for (Shortcut shortcut : _initialGlobalShortcutList)
		{
			int shortcutId = shortcut.getId();
			switch (shortcut.getType())
			{
				case ITEM:
				{
					final Item item = player.getInventory().getItemByItemId(shortcutId);
					if (item == null)
					{
						continue;
					}
					shortcutId = item.getObjectId();
					break;
				}
				case SKILL:
				{
					if (!player.getSkills().containsKey(shortcutId))
					{
						continue;
					}
					break;
				}
				case MACRO:
				{
					final Macro macro = _macroPresets.get(shortcutId);
					if (macro == null)
					{
						continue;
					}
					player.registerMacro(macro);
					break;
				}
			}
			
			// Register shortcut.
			final Shortcut newShortcut = new Shortcut(shortcut.getSlot(), shortcut.getPage(), shortcut.getType(), shortcutId, shortcut.getLevel(), shortcut.getSubLevel(), shortcut.getCharacterType());
			player.sendPacket(new ShortcutRegister(newShortcut, player));
			player.registerShortcut(newShortcut);
		}
		
		// Register class specific shortcuts.
		if (_initialShortcutData.containsKey(player.getPlayerClass()))
		{
			for (Shortcut shortcut : _initialShortcutData.get(player.getPlayerClass()))
			{
				int shortcutId = shortcut.getId();
				switch (shortcut.getType())
				{
					case ITEM:
					{
						final Item item = player.getInventory().getItemByItemId(shortcutId);
						if (item == null)
						{
							continue;
						}
						shortcutId = item.getObjectId();
						break;
					}
					case SKILL:
					{
						if (!player.getSkills().containsKey(shortcut.getId()))
						{
							continue;
						}
						break;
					}
					case MACRO:
					{
						final Macro macro = _macroPresets.get(shortcutId);
						if (macro == null)
						{
							continue;
						}
						player.registerMacro(macro);
						break;
					}
				}
				
				// Register shortcut.
				final Shortcut newShortcut = new Shortcut(shortcut.getSlot(), shortcut.getPage(), shortcut.getType(), shortcutId, shortcut.getLevel(), shortcut.getSubLevel(), shortcut.getCharacterType());
				player.sendPacket(new ShortcutRegister(newShortcut, player));
				player.registerShortcut(newShortcut);
			}
		}
	}
	
	public static InitialShortcutData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final InitialShortcutData INSTANCE = new InitialShortcutData();
	}
}
