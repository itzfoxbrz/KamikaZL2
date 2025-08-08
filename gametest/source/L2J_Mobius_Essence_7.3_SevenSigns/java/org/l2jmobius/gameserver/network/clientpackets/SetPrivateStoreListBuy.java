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
package org.l2jmobius.gameserver.network.clientpackets;

import static org.l2jmobius.gameserver.model.itemcontainer.Inventory.MAX_ADENA;

import java.util.Arrays;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.data.xml.EnsoulData;
import org.l2jmobius.gameserver.data.xml.ItemData;
import org.l2jmobius.gameserver.managers.PunishmentManager;
import org.l2jmobius.gameserver.model.TradeItem;
import org.l2jmobius.gameserver.model.TradeList;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.enums.creature.AttributeType;
import org.l2jmobius.gameserver.model.actor.enums.player.PrivateStoreType;
import org.l2jmobius.gameserver.model.ensoul.EnsoulOption;
import org.l2jmobius.gameserver.model.item.ItemTemplate;
import org.l2jmobius.gameserver.model.zone.ZoneId;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.ActionFailed;
import org.l2jmobius.gameserver.network.serverpackets.PrivateStoreManageListBuy;
import org.l2jmobius.gameserver.network.serverpackets.PrivateStoreMsgBuy;
import org.l2jmobius.gameserver.taskmanagers.AttackStanceTaskManager;

/**
 * @author Mobius
 */
public class SetPrivateStoreListBuy extends ClientPacket
{
	private TradeItem[] _items = null;
	
	@Override
	protected void readImpl()
	{
		final int count = readInt();
		if ((count < 1) || (count > Config.MAX_ITEM_IN_PACKET))
		{
			return;
		}
		
		_items = new TradeItem[count];
		for (int i = 0; i < count; i++)
		{
			final int itemId = readInt();
			final ItemTemplate template = ItemData.getInstance().getTemplate(itemId);
			if (template == null)
			{
				_items = null;
				return;
			}
			
			final int enchantLevel = readShort();
			readShort(); // Price per unit.
			
			final long cnt = readLong();
			final long price = readLong();
			if ((itemId < 1) || (cnt < 1) || (price < 0))
			{
				_items = null;
				return;
			}
			
			final int option1 = readInt();
			final int option2 = readInt();
			final short attackAttributeId = readShort();
			final int attackAttributeValue = readShort();
			final int defenceFire = readShort();
			final int defenceWater = readShort();
			final int defenceWind = readShort();
			final int defenceEarth = readShort();
			final int defenceHoly = readShort();
			final int defenceDark = readShort();
			final int visualId = readInt();
			final EnsoulOption[] soulCrystalOptions = new EnsoulOption[readByte()];
			for (int k = 0; k < soulCrystalOptions.length; k++)
			{
				soulCrystalOptions[k] = EnsoulData.getInstance().getOption(readInt());
			}
			final EnsoulOption[] soulCrystalSpecialOptions = new EnsoulOption[readByte()];
			for (int k = 0; k < soulCrystalSpecialOptions.length; k++)
			{
				soulCrystalSpecialOptions[k] = EnsoulData.getInstance().getOption(readInt());
			}
			
			// Unknown.
			readByte();
			readByte();
			readByte();
			readByte();
			readByte();
			readString();
			
			final TradeItem item = new TradeItem(template, cnt, price);
			item.setEnchant(enchantLevel);
			item.setAugmentation(option1, option2);
			item.setAttackElementType(AttributeType.findByClientId(attackAttributeId));
			item.setAttackElementPower(attackAttributeValue);
			item.setElementDefAttr(AttributeType.FIRE, defenceFire);
			item.setElementDefAttr(AttributeType.WATER, defenceWater);
			item.setElementDefAttr(AttributeType.WIND, defenceWind);
			item.setElementDefAttr(AttributeType.EARTH, defenceEarth);
			item.setElementDefAttr(AttributeType.HOLY, defenceHoly);
			item.setElementDefAttr(AttributeType.DARK, defenceDark);
			item.setVisualId(visualId);
			item.setSoulCrystalOptions(Arrays.asList(soulCrystalOptions));
			item.setSoulCrystalSpecialOptions(Arrays.asList(soulCrystalSpecialOptions));
			_items[i] = item;
		}
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (_items == null)
		{
			player.setPrivateStoreType(PrivateStoreType.NONE);
			player.broadcastUserInfo();
			return;
		}
		
		if (!player.getAccessLevel().allowTransaction())
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(player) || player.isInDuel())
		{
			player.sendPacket(SystemMessageId.WHILE_YOU_ARE_ENGAGED_IN_COMBAT_YOU_CANNOT_OPERATE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			player.sendPacket(new PrivateStoreManageListBuy(1, player));
			player.sendPacket(new PrivateStoreManageListBuy(2, player));
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (player.isInsideZone(ZoneId.NO_STORE))
		{
			player.sendPacket(new PrivateStoreManageListBuy(1, player));
			player.sendPacket(new PrivateStoreManageListBuy(2, player));
			player.sendPacket(SystemMessageId.YOU_CANNOT_OPEN_A_PRIVATE_STORE_HERE);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final TradeList tradeList = player.getBuyList();
		tradeList.clear();
		
		// Check maximum number of allowed slots for pvt shops
		if (_items.length > player.getPrivateBuyStoreLimit())
		{
			player.sendPacket(new PrivateStoreManageListBuy(1, player));
			player.sendPacket(new PrivateStoreManageListBuy(2, player));
			player.sendPacket(SystemMessageId.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		
		long totalCost = 0;
		for (TradeItem i : _items)
		{
			if ((MAX_ADENA / i.getCount()) < i.getPrice())
			{
				PunishmentManager.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to set price more than " + MAX_ADENA + " adena in Private Store - Buy.", Config.DEFAULT_PUNISH);
				return;
			}
			
			tradeList.addItemByItemId(i.getItem().getId(), i.getCount(), i.getPrice());
			totalCost += (i.getCount() * i.getPrice());
			if (totalCost > MAX_ADENA)
			{
				PunishmentManager.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to set total price more than " + MAX_ADENA + " adena in Private Store - Buy.", Config.DEFAULT_PUNISH);
				return;
			}
		}
		
		// Check for available funds
		if (totalCost > player.getAdena())
		{
			player.sendPacket(new PrivateStoreManageListBuy(1, player));
			player.sendPacket(new PrivateStoreManageListBuy(2, player));
			player.sendPacket(SystemMessageId.THE_PURCHASE_PRICE_IS_HIGHER_THAN_THE_AMOUNT_OF_MONEY_THAT_YOU_HAVE_AND_SO_YOU_CANNOT_OPEN_A_PERSONAL_STORE);
			return;
		}
		
		player.sitDown();
		player.setPrivateStoreType(PrivateStoreType.BUY);
		player.broadcastUserInfo();
		player.broadcastPacket(new PrivateStoreMsgBuy(player));
	}
}
