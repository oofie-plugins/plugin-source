package net.runelite.client.plugins.superheat;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.iutils.*;

import java.util.List;

import static net.runelite.client.plugins.iutils.Banks.BANK_SET;

@Slf4j
public abstract class Task
{
	public Task()
	{
	}

	@Inject
	public Client client;

	@Inject
	public superheatConfig config;

	@Inject
	public iUtils utils;

	@Inject
	public MenuUtils menu;

	@Inject
	public MouseUtils mouse;

	@Inject
	public CalculationUtils calc;

	@Inject
	public PlayerUtils playerUtils;

	@Inject
	public ObjectUtils object;

	@Inject
	public InventoryUtils inventory;

	@Inject
	public BankUtils bank;

	public WidgetItem targetItem;

	public MenuEntry targetMenu;

	public abstract boolean validate();

	//!items
	public int nat = ItemID.NATURE_RUNE;
	public int coal = ItemID.COAL;

	public int rune = ItemID.RUNITE_ORE;
	public int runebar = ItemID.RUNITE_BAR;

	public int adam = ItemID.ADAMANTITE_ORE;
	public int addybar = ItemID.ADAMANTITE_BAR;

	public int meth = ItemID.MITHRIL_ORE;
	public int methbar = ItemID.MITHRIL_BAR;

	public int steel = ItemID.IRON_ORE;
	public int steelbar = ItemID.STEEL_BAR;

	public int copper = ItemID.COPPER_ORE;
	public int tin = ItemID.TIN_ORE;
	public int bronzebar = ItemID.BRONZE_BAR;

	public List required = List.of(nat);



	public long sleepDelay()
	{
		superheatPlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
		return superheatPlugin.sleepLength;
	}

	public int tickDelay()
	{
		superheatPlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
		return superheatPlugin.tickLength;
	}


	public String getTaskDescription()
	{
		return this.getClass().getSimpleName();
	}

	public void onGameTick(GameTick event)
	{
		return;
	}


	public void castOnItem(WidgetItem item) {
		targetMenu = new MenuEntry("", "", item.getId(), MenuAction.ITEM_USE_ON_WIDGET.getId(), item.getIndex(), 9764864, true);
		superheatPlugin.timeout = 2 + tickDelay();
		utils.oneClickCastSpell(WidgetInfo.SPELL_SUPERHEAT_ITEM, targetMenu, item.getCanvasBounds().getBounds(), sleepDelay());
	}

	public void timeout(int item) {
		superheatPlugin.conditionTimeout = new TimeoutUntil(
				() -> inventory.containsItem(item),
				5);
	}

	public void timeoutBank() {
		superheatPlugin.conditionTimeout = new TimeoutUntil(
				() -> bank.isOpen(),
				5);
	}

	public void openBank()
	{
		NPC geNPC = new NPCQuery()
				.idEquals(1634,3089,1633,1613)
				.result(client)
				.nearestTo(client.getLocalPlayer());

		GameObject BANK = new GameObjectQuery()
				.idEquals(BANK_SET)
				.result(client)
				.nearestTo(client.getLocalPlayer());


		if (geNPC != null) {
			utils.doNpcActionMsTime(geNPC, MenuAction.NPC_THIRD_OPTION.getId(), sleepDelay());
		} else {
			if (BANK != null) {
				utils.doGameObjectActionMsTime(BANK, MenuAction.GAME_OBJECT_FOURTH_OPTION.getId(), sleepDelay());
			}
		}
		superheatPlugin.conditionTimeout = new TimeoutUntil(
				()->bank.isOpen(),
				3);
	}

}
