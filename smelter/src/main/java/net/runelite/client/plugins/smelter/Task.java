package net.runelite.client.plugins.smelter;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.smelter.BarType;
import net.runelite.client.plugins.iutils.*;
import net.runelite.client.plugins.iutils.TimeoutUntil;
import net.runelite.client.plugins.iutils.TimeoutWhile;


import java.util.List;

@Slf4j
public abstract class Task
{
	public Task()
	{
	}

	@Inject
	public Client client;

	@Inject
	public SmelterConfig config;

	@Inject
	public iUtils utils;

	@Inject
	public MenuUtils menu;

	@Inject
	public MouseUtils mouse;

	@Inject
	public InventoryUtils inv;

	@Inject
	public ObjectUtils obj;

	@Inject
	public CalculationUtils calc;

	@Inject
	public PlayerUtils playerUtils;

	@Inject
	public BankUtils bank;

	public MenuEntry entry;

	public GameObject targetObject;

	public abstract boolean validate();

//	public boolean openBank;

	//!items
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

	public int soda = ItemID.SODA_ASH;
	public int sand = ItemID.BUCKET_OF_SAND;

	public List ores = List.of(rune, adam, meth, steel, copper, soda);



	public WorldPoint Furnace = new WorldPoint(3109, 3499, 0);

	public long sleepDelay()
	{
		SmelterPlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
		return SmelterPlugin.sleepLength;
	}

	public int tickDelay()
	{
		SmelterPlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
		return SmelterPlugin.tickLength;
	}

	public String getTaskDescription()
	{
		return this.getClass().getSimpleName();
	}

	public void onGameTick(GameTick event)
	{
		return;
	}

	public void timeout(int item) {
		SmelterPlugin.conditionTimeout = new TimeoutUntil(
				() -> inv.containsItem(item),
				5);
	}


	public void useFurnace()
	{
		SmelterPlugin.status = "finding furnace";
		targetObject = obj.findNearestGameObject(16469);

		if (targetObject != null)
		{
			entry = new MenuEntry("Smelt", "<col=ffff>Furnace", targetObject.getId(), 4, targetObject.getSceneMinLocation().getX(), targetObject.getSceneMinLocation().getY(), false);
			menu.setEntry(entry);
			mouse.delayMouseClick(targetObject.getConvexHull().getBounds(), sleepDelay());
		}
		SmelterPlugin.conditionTimeout = new TimeoutUntil(
				() -> client.getLocalPlayer().getWorldLocation().equals(Furnace),
				() -> playerUtils.isMoving(),
				4);
	}

	public void openBank() {
		SmelterPlugin.status = "finding bank";
		GameObject BANK = obj.findNearestGameObject(10355);
		utils.doGameObjectActionMsTime(BANK, MenuAction.GAME_OBJECT_SECOND_OPTION.getId(), sleepDelay());
		SmelterPlugin.conditionTimeout = new TimeoutUntil(
				() -> bank.isOpen(),
				() -> playerUtils.isMoving(),
				3);
	}

	public void handleLvlUp(int item) {
		if ( inv.containsItem(item) ) {
			useFurnace();
			SmelterPlugin.timeout = tickDelay();
		} else {
			openBank();
			SmelterPlugin.timeout = tickDelay();
		}

	}
//	public boolean isIdle() {
//		Widget optionMenu = client.getWidget(270, 4);
//		if (!client.getLocalPlayer().getWorldLocation().equals(Furnace)) { return false; }
//		if (optionMenu != null) {
//			return false;
//		}
//		return !playerUtils.isMoving() && !playerUtils.isAnimating();
//		}
//
//	public void handleIdle(int item) {
//		if (isIdle()) {
//			if (inv.containsItem(item)) {
//				useFurnace();
//			} else {
//				openBank();
//			}
//			SmelterPlugin.conditionTimeout = new TimeoutUntil(
//					() -> playerUtils.isAnimating(),
//					() -> playerUtils.isAnimating(),
//					4);
//		}
//	}

}
