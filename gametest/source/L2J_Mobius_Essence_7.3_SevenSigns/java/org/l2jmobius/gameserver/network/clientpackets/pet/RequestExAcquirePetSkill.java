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
package org.l2jmobius.gameserver.network.clientpackets.pet;

import java.util.Optional;

import org.l2jmobius.gameserver.data.xml.PetAcquireList;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.Pet;
import org.l2jmobius.gameserver.model.item.enums.ItemProcessType;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.holders.PetSkillAcquireHolder;
import org.l2jmobius.gameserver.network.serverpackets.pet.ExPetSkillList;

/**
 * @author Berezkin Nikolay
 */
public class RequestExAcquirePetSkill extends ClientPacket
{
	private int skillId, skillLevel;
	
	@Override
	protected void readImpl()
	{
		skillId = readInt();
		skillLevel = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		final Pet pet = player.getPet();
		if (pet == null)
		{
			return;
		}
		final Skill skill = SkillData.getInstance().getSkill(skillId, skillLevel);
		if (skill == null)
		{
			return;
		}
		final Optional<PetSkillAcquireHolder> reqItem = PetAcquireList.getInstance().getSkills(pet.getPetData().getType()).stream().filter(it -> (it.getSkillId() == skillId) && (it.getSkillLevel() == skillLevel)).findFirst();
		if (reqItem.isPresent())
		{
			if (reqItem.get().getItem() != null)
			{
				if (player.destroyItemByItemId(ItemProcessType.FEE, reqItem.get().getItem().getId(), reqItem.get().getItem().getCount(), null, true))
				{
					pet.addSkill(skill);
					pet.storePetSkills(skillId, skillLevel);
					player.sendPacket(new ExPetSkillList(false, pet));
				}
			}
			else
			{
				pet.addSkill(skill);
				player.sendPacket(new ExPetSkillList(false, pet));
			}
		}
	}
}
