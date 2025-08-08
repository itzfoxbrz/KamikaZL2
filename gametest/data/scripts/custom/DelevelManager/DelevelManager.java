package custom.DelevelManager;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.data.xml.ExperienceData;
import org.l2jmobius.gameserver.data.xml.ItemData;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.ItemTemplate;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;

import ai.AbstractNpcAI;

public class DelevelManager extends AbstractNpcAI
{
	private static final String BASE_PATH = "data/scripts/custom/DelevelManager/";

	private DelevelManager()
	{
		addStartNpc(Config.DELEVEL_MANAGER_NPCID);
		addTalkId(Config.DELEVEL_MANAGER_NPCID);
		addFirstTalkId(Config.DELEVEL_MANAGER_NPCID);
	}

	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		sendPage(player, npc, "1002000.htm");
		return null;
	}

	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if (!Config.DELEVEL_MANAGER_ENABLED)
		{
			return null;
		}

		switch (event)
		{
			case "main":
			{
				sendPage(player, npc, "1002000.htm");
				return null;
			}
			case "delevel":
			{
				if (player.getLevel() <= Config.DELEVEL_MANAGER_MINIMUM_DELEVEL)
				{
					sendPage(player, npc, "1002000-min.htm");
					return null;
				}

				final long targetExpForPrevLevel = ExperienceData.getInstance().getExpForLevel(player.getLevel() - 1);
				final long toRemove = player.getExp() - targetExpForPrevLevel;

				if (toRemove > 0)
				{
					player.getStat().removeExpAndSp(toRemove, 0);

					// Reward instead of charging
					giveItems(player, Config.DELEVEL_MANAGER_ITEMID, Config.DELEVEL_MANAGER_ITEMCOUNT);

					player.broadcastUserInfo();
					sendPage(player, npc, "1002000-ok.htm");
					return null;
				}

				// Fallback
				sendPage(player, npc, "1002000-err.htm");
				return null;
			}
		}
		return null;
	}

	private void sendPage(Player player, Npc npc, String fileName)
	{
		// Usa o construtor por objectId (compatível com Mobius)
		final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
		html.setFile(player, BASE_PATH + fileName);

		// Placeholders dinâmicos
		html.replace("%ITEM_NAME%", getItemName(Config.DELEVEL_MANAGER_ITEMID));
		html.replace("%ITEM_COUNT%", String.valueOf(Config.DELEVEL_MANAGER_ITEMCOUNT));
		html.replace("%MIN_LEVEL%", String.valueOf(Config.DELEVEL_MANAGER_MINIMUM_DELEVEL));

		player.sendPacket(html);
	}

	private String getItemName(int itemId)
	{
		final ItemTemplate it = ItemData.getInstance().getTemplate(itemId);
		return (it != null) ? it.getName() : ("Item (" + itemId + ")");
	}

	public static void main(String[] args)
	{
		new DelevelManager();
	}
}
