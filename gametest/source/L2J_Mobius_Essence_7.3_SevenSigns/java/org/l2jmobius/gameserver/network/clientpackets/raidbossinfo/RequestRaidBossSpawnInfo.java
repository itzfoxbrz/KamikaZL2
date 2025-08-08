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
package org.l2jmobius.gameserver.network.clientpackets.raidbossinfo;

import java.util.HashMap;
import java.util.Map;

import org.l2jmobius.gameserver.managers.DBSpawnManager;
import org.l2jmobius.gameserver.managers.GrandBossManager;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.enums.npc.RaidBossStatus;
import org.l2jmobius.gameserver.model.actor.instance.GrandBoss;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.raidbossinfo.ExRaidBossSpawnInfo;

/**
 * @author Mobius
 */
public class RequestRaidBossSpawnInfo extends ClientPacket
{
	private static final int BAIUM = 29020;
	
	private final Map<Integer, RaidBossStatus> _statuses = new HashMap<>();
	
	@Override
	protected void readImpl()
	{
		final int count = readInt();
		for (int i = 0; i < count; i++)
		{
			final int bossId = readInt();
			final GrandBoss boss = GrandBossManager.getInstance().getBoss(bossId);
			if (boss == null)
			{
				final RaidBossStatus status = DBSpawnManager.getInstance().getStatus(bossId);
				if (status != RaidBossStatus.UNDEFINED)
				{
					final Npc npc = DBSpawnManager.getInstance().getNpc(bossId);
					if ((npc != null) && npc.isInCombat())
					{
						_statuses.put(bossId, RaidBossStatus.COMBAT);
					}
					else
					{
						_statuses.put(bossId, status);
					}
				}
				else
				{
					_statuses.put(bossId, RaidBossStatus.DEAD);
					// PacketLogger.warning("Could not find spawn info for boss " + bossId + ".");
				}
			}
			else
			{
				if (boss.isDead() || !boss.isSpawned())
				{
					if ((bossId == BAIUM) && (GrandBossManager.getInstance().getStatus(BAIUM) == 0))
					{
						_statuses.put(bossId, RaidBossStatus.ALIVE);
					}
					else
					{
						_statuses.put(bossId, RaidBossStatus.DEAD);
					}
				}
				else if (boss.isInCombat())
				{
					_statuses.put(bossId, RaidBossStatus.COMBAT);
				}
				else
				{
					_statuses.put(bossId, RaidBossStatus.ALIVE);
				}
			}
		}
	}
	
	@Override
	protected void runImpl()
	{
		getClient().sendPacket(new ExRaidBossSpawnInfo(_statuses));
	}
}
