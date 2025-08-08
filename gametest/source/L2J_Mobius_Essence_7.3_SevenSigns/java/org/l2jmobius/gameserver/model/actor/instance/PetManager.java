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
package org.l2jmobius.gameserver.model.actor.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

import org.l2jmobius.Config;
import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.gameserver.data.xml.NpcData;
import org.l2jmobius.gameserver.data.xml.PetDataTable;
import org.l2jmobius.gameserver.managers.PunishmentManager;
import org.l2jmobius.gameserver.model.PetData;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.Summon;
import org.l2jmobius.gameserver.model.actor.enums.creature.InstanceType;
import org.l2jmobius.gameserver.model.actor.templates.NpcTemplate;
import org.l2jmobius.gameserver.model.item.enums.ItemProcessType;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.InventoryUpdate;
import org.l2jmobius.gameserver.network.serverpackets.MagicSkillLaunched;
import org.l2jmobius.gameserver.network.serverpackets.MagicSkillUse;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;

public class PetManager extends Merchant
{
	public PetManager(NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.PetManager);
	}
	
	@Override
	public String getHtmlPath(int npcId, int value, Player player)
	{
		String pom = "";
		if (value == 0)
		{
			pom = Integer.toString(npcId);
		}
		else
		{
			pom = npcId + "-" + value;
		}
		return "data/html/petmanager/" + pom + ".htm";
	}
	
	@Override
	public void showChatWindow(Player player)
	{
		String filename = "data/html/petmanager/" + getId() + ".htm";
		if ((getId() == 36478) && player.hasSummon())
		{
			filename = "data/html/petmanager/restore-unsummonpet.htm";
		}
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(player, filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("exchange"))
		{
			final String[] params = command.split(" ");
			final int val = Integer.parseInt(params[1]);
			switch (val)
			{
				case 1:
				{
					exchange(player, 7585, 6650);
					break;
				}
				case 2:
				{
					exchange(player, 7583, 6648);
					break;
				}
				case 3:
				{
					exchange(player, 7584, 6649);
					break;
				}
			}
		}
		else if (command.startsWith("evolve"))
		{
			final String[] params = command.split(" ");
			final int val = Integer.parseInt(params[1]);
			boolean ok = false;
			switch (val)
			{
				// Info evolve(player, "curent pet summon item", "new pet summon item", "level required to evolve")
				// To ignore evolve just put value 0 where do you like example: evolve(player, 0, 9882, 55);
				case 1:
				{
					ok = doEvolve(player, this, 2375, 9882, 55);
					break;
				}
				case 2:
				{
					ok = doEvolve(player, this, 9882, 10426, 70);
					break;
				}
				case 3:
				{
					ok = doEvolve(player, this, 6648, 10311, 55);
					break;
				}
				case 4:
				{
					ok = doEvolve(player, this, 6650, 10313, 55);
					break;
				}
				case 5:
				{
					ok = doEvolve(player, this, 6649, 10312, 55);
					break;
				}
			}
			if (!ok)
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile(player, "data/html/petmanager/evolve_no.htm");
				player.sendPacket(html);
			}
		}
		else if (command.startsWith("restore"))
		{
			final String[] params = command.split(" ");
			final int val = Integer.parseInt(params[1]);
			boolean ok = false;
			switch (val)
			{
				// Info evolve(player, "curent pet summon item", "new pet summon item", "level required to evolve")
				case 1:
				{
					ok = doRestore(player, this, 10307, 9882, 55);
					break;
				}
				case 2:
				{
					ok = doRestore(player, this, 10611, 10426, 70);
					break;
				}
				case 3:
				{
					ok = doRestore(player, this, 10308, 4422, 55);
					break;
				}
				case 4:
				{
					ok = doRestore(player, this, 10309, 4423, 55);
					break;
				}
				case 5:
				{
					ok = doRestore(player, this, 10310, 4424, 55);
					break;
				}
			}
			if (!ok)
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile(player, "data/html/petmanager/restore_no.htm");
				player.sendPacket(html);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	public void exchange(Player player, int itemIdtake, int itemIdgive)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		if (player.destroyItemByItemId(null, itemIdtake, 1, this, true))
		{
			player.addItem(ItemProcessType.NONE, itemIdgive, 1, this, true);
			html.setFile(player, "data/html/petmanager/" + getId() + ".htm");
			player.sendPacket(html);
		}
		else
		{
			html.setFile(player, "data/html/petmanager/exchange_no.htm");
			player.sendPacket(html);
		}
	}
	
	private boolean doEvolve(Player player, Npc npc, int itemIdtake, int itemIdgive, int petminLevel)
	{
		if ((itemIdtake == 0) || (itemIdgive == 0) || (petminLevel == 0))
		{
			return false;
		}
		
		final Summon pet = player.getPet();
		if (pet == null)
		{
			return false;
		}
		
		final Pet currentPet = pet.asPet();
		if (currentPet.isAlikeDead())
		{
			PunishmentManager.handleIllegalPlayerAction(player, player + " tried to use death pet exploit!", Config.DEFAULT_PUNISH);
			return false;
		}
		
		Item item = null;
		long petexp = currentPet.getStat().getExp();
		final String oldname = currentPet.getName();
		final int oldX = currentPet.getX();
		final int oldY = currentPet.getY();
		final int oldZ = currentPet.getZ();
		final PetData oldData = PetDataTable.getInstance().getPetDataByItemId(itemIdtake);
		if (oldData == null)
		{
			return false;
		}
		
		final int oldnpcID = oldData.getNpcId();
		if ((currentPet.getStat().getLevel() < petminLevel) || (currentPet.getId() != oldnpcID))
		{
			return false;
		}
		
		final PetData petData = PetDataTable.getInstance().getPetDataByItemId(itemIdgive);
		if (petData == null)
		{
			return false;
		}
		
		final int npcID = petData.getNpcId();
		if (npcID == 0)
		{
			return false;
		}
		
		final NpcTemplate npcTemplate = NpcData.getInstance().getTemplate(npcID);
		currentPet.unSummon(player);
		
		// deleting old pet item
		currentPet.destroyControlItem(player, true);
		item = player.getInventory().addItem(ItemProcessType.RESTORE, itemIdgive, 1, player, npc);
		
		// Summoning new pet
		final Pet petSummon = Pet.spawnPet(npcTemplate, player, item);
		if (petSummon == null)
		{
			return false;
		}
		
		// Fix for non-linear baby pet exp
		final long _minimumexp = petSummon.getStat().getExpForLevel(petminLevel);
		if (petexp < _minimumexp)
		{
			petexp = _minimumexp;
		}
		
		petSummon.getStat().addExp(petexp);
		petSummon.setCurrentHp(petSummon.getMaxHp());
		petSummon.setCurrentMp(petSummon.getMaxMp());
		petSummon.setCurrentFed(petSummon.getMaxFed());
		petSummon.setTitle(player.getName());
		petSummon.setName(oldname);
		petSummon.setRunning();
		petSummon.storeMe();
		
		player.setPet(petSummon);
		
		player.sendPacket(new MagicSkillUse(npc, 2046, 1, 1000, 600000));
		player.sendPacket(SystemMessageId.SUMMONING_YOUR_PET);
		petSummon.spawnMe(oldX, oldY, oldZ);
		petSummon.startFeed();
		item.setEnchantLevel(petSummon.getLevel());
		
		ThreadPool.schedule(new EvolveFinalizer(player, petSummon), 900);
		if (petSummon.getCurrentFed() <= 0)
		{
			ThreadPool.schedule(new EvolveFeedWait(player, petSummon), 60000);
		}
		else
		{
			petSummon.startFeed();
		}
		
		return true;
	}
	
	private boolean doRestore(Player player, Npc npc, int itemIdtake, int itemIdgive, int petminLevel)
	{
		if ((itemIdtake == 0) || (itemIdgive == 0) || (petminLevel == 0))
		{
			return false;
		}
		
		final Item item = player.getInventory().getItemByItemId(itemIdtake);
		if (item == null)
		{
			return false;
		}
		
		int oldpetlvl = item.getEnchantLevel();
		if (oldpetlvl < petminLevel)
		{
			oldpetlvl = petminLevel;
		}
		
		final PetData oldData = PetDataTable.getInstance().getPetDataByItemId(itemIdtake);
		if (oldData == null)
		{
			return false;
		}
		
		final PetData petData = PetDataTable.getInstance().getPetDataByItemId(itemIdgive);
		if (petData == null)
		{
			return false;
		}
		
		final int npcId = petData.getNpcId();
		if (npcId == 0)
		{
			return false;
		}
		
		final NpcTemplate npcTemplate = NpcData.getInstance().getTemplate(npcId);
		
		// deleting old pet item
		final Item removedItem = player.getInventory().destroyItem(ItemProcessType.RESTORE, item, player, npc);
		final SystemMessage sm = new SystemMessage(SystemMessageId.S1_DISAPPEARED);
		sm.addItemName(removedItem);
		player.sendPacket(sm);
		
		// Give new pet item
		final Item addedItem = player.getInventory().addItem(ItemProcessType.RESTORE, itemIdgive, 1, player, npc);
		
		// Summoning new pet
		final Pet petSummon = Pet.spawnPet(npcTemplate, player, addedItem);
		if (petSummon == null)
		{
			return false;
		}
		
		final long _maxexp = petSummon.getStat().getExpForLevel(oldpetlvl);
		petSummon.getStat().addExp(_maxexp);
		petSummon.setCurrentHp(petSummon.getMaxHp());
		petSummon.setCurrentMp(petSummon.getMaxMp());
		petSummon.setCurrentFed(petSummon.getMaxFed());
		petSummon.setTitle(player.getName());
		petSummon.setRunning();
		petSummon.storeMe();
		
		player.setPet(petSummon);
		
		player.sendPacket(new MagicSkillUse(npc, 2046, 1, 1000, 600000));
		player.sendPacket(SystemMessageId.SUMMONING_YOUR_PET);
		petSummon.spawnMe(player.getX(), player.getY(), player.getZ());
		petSummon.startFeed();
		addedItem.setEnchantLevel(petSummon.getLevel());
		
		// Inventory update
		final InventoryUpdate iu = new InventoryUpdate();
		iu.addRemovedItem(removedItem);
		player.sendInventoryUpdate(iu);
		
		player.broadcastUserInfo();
		
		ThreadPool.schedule(new EvolveFinalizer(player, petSummon), 900);
		if (petSummon.getCurrentFed() <= 0)
		{
			ThreadPool.schedule(new EvolveFeedWait(player, petSummon), 60000);
		}
		else
		{
			petSummon.startFeed();
		}
		
		// pet control item no longer exists, delete the pet from the db
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?"))
		{
			ps.setInt(1, removedItem.getObjectId());
			ps.execute();
		}
		catch (Exception e)
		{
			// Ignore.
		}
		return true;
	}
	
	private class EvolveFeedWait implements Runnable
	{
		private final Player _player;
		private final Pet _petSummon;
		
		EvolveFeedWait(Player player, Pet petSummon)
		{
			_player = player;
			_petSummon = petSummon;
		}
		
		@Override
		public void run()
		{
			try
			{
				if (_petSummon.getCurrentFed() <= 0)
				{
					_petSummon.unSummon(_player);
				}
				else
				{
					_petSummon.startFeed();
				}
			}
			catch (Exception e)
			{
				LOGGER.log(Level.WARNING, "", e);
			}
		}
	}
	
	private class EvolveFinalizer implements Runnable
	{
		private final Player _player;
		private final Pet _petSummon;
		
		EvolveFinalizer(Player player, Pet petSummon)
		{
			_player = player;
			_petSummon = petSummon;
		}
		
		@Override
		public void run()
		{
			try
			{
				_player.sendPacket(new MagicSkillLaunched(_player, 2046, 1));
				_petSummon.setFollowStatus(true);
				_petSummon.setShowSummonAnimation(false);
			}
			catch (Throwable e)
			{
				LOGGER.log(Level.WARNING, "", e);
			}
		}
	}
}
