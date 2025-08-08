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
package org.l2jmobius.gameserver.model.events;

import org.l2jmobius.gameserver.model.events.holders.IBaseEvent;
import org.l2jmobius.gameserver.model.events.holders.OnDayNightChange;
import org.l2jmobius.gameserver.model.events.holders.OnServerStart;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureAttack;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureAttackAvoid;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureAttacked;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureDamageDealt;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureDamageReceived;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureDeath;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureHpChange;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureKilled;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureSee;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureSkillFinishCast;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureSkillUse;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureTeleport;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureTeleported;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureZoneEnter;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnCreatureZoneExit;
import org.l2jmobius.gameserver.model.events.holders.actor.creature.OnElementalSpiritUpgrade;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnAttackableAggroRangeEnter;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnAttackableAttack;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnAttackableFactionCall;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnAttackableHate;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnAttackableKill;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcCanBeSeen;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcDespawn;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcEventReceived;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcFirstTalk;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcManorBypass;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcMenuSelect;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcMoveFinished;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcMoveNodeArrived;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcMoveRouteFinished;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcSkillFinished;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcSkillSee;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcSpawn;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcTeleport;
import org.l2jmobius.gameserver.model.events.holders.actor.npc.OnNpcTeleportRequest;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnElementalSpiritLearn;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayableExpChanged;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerAbilityPointsChanged;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerAugment;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerBypass;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerCallToChangeClass;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerChangeToAwakenedClass;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerChat;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerCheatDeath;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerClanCreate;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerClanDestroy;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerClanJoin;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerClanLeaderChange;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerClanLeft;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerClanLvlUp;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerClanWHItemAdd;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerClanWHItemDestroy;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerClanWHItemTransfer;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerCreate;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerDelete;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerDlgAnswer;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerFameChanged;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerFishing;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerHennaAdd;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerHennaEnchant;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerHennaRemove;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerItemAdd;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerItemDestroy;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerItemDrop;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerItemEquip;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerItemPickup;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerItemTransfer;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerItemUnequip;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerLevelChanged;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerLoad;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerLogin;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerLogout;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerMenteeAdd;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerMenteeLeft;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerMenteeRemove;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerMenteeStatus;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerMentorStatus;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerMoveRequest;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerPKChanged;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerPressTutorialMark;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerProfessionCancel;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerProfessionChange;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerPvPChanged;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerPvPKill;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerQuestAbort;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerQuestAccept;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerQuestComplete;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerReputationChanged;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerRestore;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerSelect;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerSkillLearn;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerSocialAction;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerSubChange;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerSummonAgathion;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerSummonSpawn;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerSummonTalk;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerTakeHero;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerTransform;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnPlayerUnsummonAgathion;
import org.l2jmobius.gameserver.model.events.holders.actor.player.OnTrapAction;
import org.l2jmobius.gameserver.model.events.holders.clan.OnClanWarFinish;
import org.l2jmobius.gameserver.model.events.holders.clan.OnClanWarStart;
import org.l2jmobius.gameserver.model.events.holders.instance.OnInstanceCreated;
import org.l2jmobius.gameserver.model.events.holders.instance.OnInstanceDestroy;
import org.l2jmobius.gameserver.model.events.holders.instance.OnInstanceEnter;
import org.l2jmobius.gameserver.model.events.holders.instance.OnInstanceLeave;
import org.l2jmobius.gameserver.model.events.holders.instance.OnInstanceStatusChange;
import org.l2jmobius.gameserver.model.events.holders.item.OnItemBypassEvent;
import org.l2jmobius.gameserver.model.events.holders.item.OnItemCreate;
import org.l2jmobius.gameserver.model.events.holders.item.OnItemPurgeReward;
import org.l2jmobius.gameserver.model.events.holders.item.OnItemTalk;
import org.l2jmobius.gameserver.model.events.holders.item.OnItemUse;
import org.l2jmobius.gameserver.model.events.holders.item.OnMultisellBuyItem;
import org.l2jmobius.gameserver.model.events.holders.olympiad.OnOlympiadMatchResult;
import org.l2jmobius.gameserver.model.events.holders.sieges.OnCastleSiegeFinish;
import org.l2jmobius.gameserver.model.events.holders.sieges.OnCastleSiegeOwnerChange;
import org.l2jmobius.gameserver.model.events.holders.sieges.OnCastleSiegeStart;
import org.l2jmobius.gameserver.model.events.holders.sieges.OnFortSiegeFinish;
import org.l2jmobius.gameserver.model.events.holders.sieges.OnFortSiegeStart;
import org.l2jmobius.gameserver.model.events.returns.ChatFilterReturn;
import org.l2jmobius.gameserver.model.events.returns.DamageReturn;
import org.l2jmobius.gameserver.model.events.returns.LocationReturn;
import org.l2jmobius.gameserver.model.events.returns.TerminateReturn;
import org.l2jmobius.gameserver.util.ArrayUtil;

/**
 * @author UnAfraid, Mobius
 */
public enum EventType
{
	// Attackable events
	ON_ATTACKABLE_AGGRO_RANGE_ENTER(OnAttackableAggroRangeEnter.class, void.class),
	ON_ATTACKABLE_ATTACK(OnAttackableAttack.class, void.class),
	ON_ATTACKABLE_FACTION_CALL(OnAttackableFactionCall.class, void.class),
	ON_ATTACKABLE_KILL(OnAttackableKill.class, void.class),
	
	// Castle events
	ON_CASTLE_SIEGE_FINISH(OnCastleSiegeFinish.class, void.class),
	ON_CASTLE_SIEGE_OWNER_CHANGE(OnCastleSiegeOwnerChange.class, void.class),
	ON_CASTLE_SIEGE_START(OnCastleSiegeStart.class, void.class),
	
	// Clan events
	ON_CLAN_WAR_FINISH(OnClanWarFinish.class, void.class),
	ON_CLAN_WAR_START(OnClanWarStart.class, void.class),
	
	// Creature events
	ON_CREATURE_ATTACK(OnCreatureAttack.class, void.class, TerminateReturn.class),
	ON_CREATURE_ATTACK_AVOID(OnCreatureAttackAvoid.class, void.class, void.class),
	ON_CREATURE_ATTACKED(OnCreatureAttacked.class, void.class, TerminateReturn.class),
	ON_CREATURE_DAMAGE_RECEIVED(OnCreatureDamageReceived.class, void.class, DamageReturn.class),
	ON_CREATURE_DAMAGE_DEALT(OnCreatureDamageDealt.class, void.class),
	ON_CREATURE_HP_CHANGE(OnCreatureHpChange.class, void.class),
	ON_CREATURE_DEATH(OnCreatureDeath.class, void.class),
	ON_CREATURE_KILLED(OnCreatureKilled.class, void.class, TerminateReturn.class),
	ON_CREATURE_SEE(OnCreatureSee.class, void.class),
	ON_CREATURE_SKILL_USE(OnCreatureSkillUse.class, void.class, TerminateReturn.class),
	ON_CREATURE_SKILL_FINISH_CAST(OnCreatureSkillFinishCast.class, void.class),
	ON_CREATURE_TELEPORT(OnCreatureTeleport.class, void.class, LocationReturn.class),
	ON_CREATURE_TELEPORTED(OnCreatureTeleported.class, void.class),
	ON_CREATURE_ZONE_ENTER(OnCreatureZoneEnter.class, void.class),
	ON_CREATURE_ZONE_EXIT(OnCreatureZoneExit.class, void.class),
	
	// Fortress events
	ON_FORT_SIEGE_FINISH(OnFortSiegeFinish.class, void.class),
	ON_FORT_SIEGE_START(OnFortSiegeStart.class, void.class),
	
	// Item events
	ON_ITEM_BYPASS_EVENT(OnItemBypassEvent.class, void.class),
	ON_ITEM_CREATE(OnItemCreate.class, void.class),
	ON_ITEM_USE(OnItemUse.class, void.class),
	ON_ITEM_TALK(OnItemTalk.class, void.class),
	ON_ITEM_PURGE_REWARD(OnItemPurgeReward.class, void.class),
	ON_MULTISELL_BUY_ITEM(OnMultisellBuyItem.class, void.class),
	
	// NPC events
	ON_NPC_CAN_BE_SEEN(OnNpcCanBeSeen.class, void.class, TerminateReturn.class),
	ON_NPC_EVENT_RECEIVED(OnNpcEventReceived.class, void.class),
	ON_NPC_FIRST_TALK(OnNpcFirstTalk.class, void.class),
	ON_NPC_HATE(OnAttackableHate.class, void.class, TerminateReturn.class),
	ON_NPC_MOVE_FINISHED(OnNpcMoveFinished.class, void.class),
	ON_NPC_MOVE_NODE_ARRIVED(OnNpcMoveNodeArrived.class, void.class),
	ON_NPC_MOVE_ROUTE_FINISHED(OnNpcMoveRouteFinished.class, void.class),
	ON_NPC_QUEST_START(null, void.class),
	ON_NPC_SKILL_FINISHED(OnNpcSkillFinished.class, void.class),
	ON_NPC_SKILL_SEE(OnNpcSkillSee.class, void.class),
	ON_NPC_SPAWN(OnNpcSpawn.class, void.class),
	ON_NPC_TALK(null, void.class),
	ON_NPC_TELEPORT(OnNpcTeleport.class, void.class),
	ON_NPC_MANOR_BYPASS(OnNpcManorBypass.class, void.class),
	ON_NPC_MENU_SELECT(OnNpcMenuSelect.class, void.class),
	ON_NPC_DESPAWN(OnNpcDespawn.class, void.class),
	ON_NPC_TELEPORT_REQUEST(OnNpcTeleportRequest.class, void.class, TerminateReturn.class),
	
	// Olympiad events
	ON_OLYMPIAD_MATCH_RESULT(OnOlympiadMatchResult.class, void.class),
	
	// Playable events
	ON_PLAYABLE_EXP_CHANGED(OnPlayableExpChanged.class, void.class, TerminateReturn.class),
	
	// Player events
	ON_PLAYER_AUGMENT(OnPlayerAugment.class, void.class),
	ON_PLAYER_BYPASS(OnPlayerBypass.class, void.class, TerminateReturn.class),
	ON_PLAYER_CALL_TO_CHANGE_CLASS(OnPlayerCallToChangeClass.class, void.class),
	ON_PLAYER_CHAT(OnPlayerChat.class, void.class, ChatFilterReturn.class),
	ON_PLAYER_ABILITY_POINTS_CHANGED(OnPlayerAbilityPointsChanged.class, void.class),
	// Clan events
	ON_PLAYER_CLAN_CREATE(OnPlayerClanCreate.class, void.class),
	ON_PLAYER_CLAN_DESTROY(OnPlayerClanDestroy.class, void.class),
	ON_PLAYER_CLAN_JOIN(OnPlayerClanJoin.class, void.class),
	ON_PLAYER_CLAN_LEADER_CHANGE(OnPlayerClanLeaderChange.class, void.class),
	ON_PLAYER_CLAN_LEFT(OnPlayerClanLeft.class, void.class),
	ON_PLAYER_CLAN_LEVELUP(OnPlayerClanLvlUp.class, void.class),
	// Clan warehouse events
	ON_PLAYER_CLAN_WH_ITEM_ADD(OnPlayerClanWHItemAdd.class, void.class),
	ON_PLAYER_CLAN_WH_ITEM_DESTROY(OnPlayerClanWHItemDestroy.class, void.class),
	ON_PLAYER_CLAN_WH_ITEM_TRANSFER(OnPlayerClanWHItemTransfer.class, void.class),
	ON_PLAYER_CREATE(OnPlayerCreate.class, void.class),
	ON_PLAYER_DELETE(OnPlayerDelete.class, void.class),
	ON_PLAYER_DLG_ANSWER(OnPlayerDlgAnswer.class, void.class, TerminateReturn.class),
	ON_PLAYER_FAME_CHANGED(OnPlayerFameChanged.class, void.class),
	ON_PLAYER_FISHING(OnPlayerFishing.class, void.class),
	// Henna events
	ON_PLAYER_HENNA_ADD(OnPlayerHennaAdd.class, void.class),
	ON_PLAYER_HENNA_REMOVE(OnPlayerHennaRemove.class, void.class),
	ON_PLAYER_HENNA_ENCHANT(OnPlayerHennaEnchant.class, void.class),
	// Inventory events
	ON_PLAYER_ITEM_ADD(OnPlayerItemAdd.class, void.class),
	ON_PLAYER_ITEM_DESTROY(OnPlayerItemDestroy.class, void.class),
	ON_PLAYER_ITEM_DROP(OnPlayerItemDrop.class, void.class),
	ON_PLAYER_ITEM_PICKUP(OnPlayerItemPickup.class, void.class),
	ON_PLAYER_ITEM_TRANSFER(OnPlayerItemTransfer.class, void.class),
	ON_PLAYER_ITEM_EQUIP(OnPlayerItemEquip.class, void.class),
	ON_PLAYER_ITEM_UNEQUIP(OnPlayerItemUnequip.class, void.class),
	// Mentoring events
	ON_PLAYER_MENTEE_ADD(OnPlayerMenteeAdd.class, void.class),
	ON_PLAYER_MENTEE_LEFT(OnPlayerMenteeLeft.class, void.class),
	ON_PLAYER_MENTEE_REMOVE(OnPlayerMenteeRemove.class, void.class),
	ON_PLAYER_MENTEE_STATUS(OnPlayerMenteeStatus.class, void.class),
	ON_PLAYER_MENTOR_STATUS(OnPlayerMentorStatus.class, void.class),
	// Other player events
	ON_PLAYER_REPUTATION_CHANGED(OnPlayerReputationChanged.class, void.class),
	ON_PLAYER_LEVEL_CHANGED(OnPlayerLevelChanged.class, void.class),
	ON_PLAYER_LOGIN(OnPlayerLogin.class, void.class),
	ON_PLAYER_LOGOUT(OnPlayerLogout.class, void.class),
	ON_PLAYER_LOAD(OnPlayerLoad.class, void.class),
	ON_PLAYER_PK_CHANGED(OnPlayerPKChanged.class, void.class),
	ON_PLAYER_PRESS_TUTORIAL_MARK(OnPlayerPressTutorialMark.class, void.class),
	ON_PLAYER_MOVE_REQUEST(OnPlayerMoveRequest.class, void.class, TerminateReturn.class),
	ON_PLAYER_PROFESSION_CHANGE(OnPlayerProfessionChange.class, void.class),
	ON_PLAYER_PROFESSION_CANCEL(OnPlayerProfessionCancel.class, void.class),
	ON_PLAYER_CHANGE_TO_AWAKENED_CLASS(OnPlayerChangeToAwakenedClass.class, void.class),
	ON_PLAYER_PVP_CHANGED(OnPlayerPvPChanged.class, void.class),
	ON_PLAYER_PVP_KILL(OnPlayerPvPKill.class, void.class),
	ON_PLAYER_CHEAT_DEATH(OnPlayerCheatDeath.class, void.class),
	ON_PLAYER_RESTORE(OnPlayerRestore.class, void.class),
	ON_PLAYER_SELECT(OnPlayerSelect.class, void.class, TerminateReturn.class),
	ON_PLAYER_SOCIAL_ACTION(OnPlayerSocialAction.class, void.class),
	ON_PLAYER_SKILL_LEARN(OnPlayerSkillLearn.class, void.class),
	ON_PLAYER_SUMMON_SPAWN(OnPlayerSummonSpawn.class, void.class),
	ON_PLAYER_SUMMON_TALK(OnPlayerSummonTalk.class, void.class),
	ON_PLAYER_TAKE_HERO(OnPlayerTakeHero.class, void.class),
	ON_PLAYER_TRANSFORM(OnPlayerTransform.class, void.class),
	ON_PLAYER_SUB_CHANGE(OnPlayerSubChange.class, void.class),
	ON_PLAYER_QUEST_ACCEPT(OnPlayerQuestAccept.class, void.class),
	ON_PLAYER_QUEST_ABORT(OnPlayerQuestAbort.class, void.class),
	ON_PLAYER_QUEST_COMPLETE(OnPlayerQuestComplete.class, void.class),
	ON_PLAYER_SUMMON_AGATHION(OnPlayerSummonAgathion.class, void.class),
	ON_PLAYER_UNSUMMON_AGATHION(OnPlayerUnsummonAgathion.class, void.class),
	
	// Trap events
	ON_TRAP_ACTION(OnTrapAction.class, void.class),
	
	// Elemental spirit events
	ON_ELEMENTAL_SPIRIT_UPGRADE(OnElementalSpiritUpgrade.class, void.class),
	ON_ELEMENTAL_SPIRIT_LEARN(OnElementalSpiritLearn.class, void.class),
	
	// Instance events
	ON_INSTANCE_CREATED(OnInstanceCreated.class, void.class),
	ON_INSTANCE_DESTROY(OnInstanceDestroy.class, void.class),
	ON_INSTANCE_ENTER(OnInstanceEnter.class, void.class),
	ON_INSTANCE_LEAVE(OnInstanceLeave.class, void.class),
	ON_INSTANCE_STATUS_CHANGE(OnInstanceStatusChange.class, void.class),
	
	// Server events
	ON_SERVER_START(OnServerStart.class, void.class),
	ON_DAY_NIGHT_CHANGE(OnDayNightChange.class, void.class);
	
	private final Class<? extends IBaseEvent> _eventClass;
	private final Class<?>[] _returnClass;
	
	EventType(Class<? extends IBaseEvent> eventClass, Class<?>... returnClasss)
	{
		_eventClass = eventClass;
		_returnClass = returnClasss;
	}
	
	public Class<? extends IBaseEvent> getEventClass()
	{
		return _eventClass;
	}
	
	public Class<?>[] getReturnClasses()
	{
		return _returnClass;
	}
	
	public boolean isEventClass(Class<?> clazz)
	{
		return _eventClass == clazz;
	}
	
	public boolean isReturnClass(Class<?> clazz)
	{
		return ArrayUtil.contains(_returnClass, clazz);
	}
}
