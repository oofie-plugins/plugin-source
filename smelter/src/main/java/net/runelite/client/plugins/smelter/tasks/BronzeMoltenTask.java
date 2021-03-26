package net.runelite.client.plugins.smelter.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.iutils.TimeoutUntil;
import net.runelite.client.plugins.smelter.BarType;
import net.runelite.client.plugins.smelter.Task;
import net.runelite.client.plugins.smelter.SmelterPlugin;

import static net.runelite.client.plugins.smelter.SmelterPlugin.conditionTimeout;

@Slf4j
public class BronzeMoltenTask extends Task {

    @Override
    public boolean validate() {
        return config.BarType().equals(BarType.BRONZE)
                || config.BarType().equals(BarType.MOLTEN_GLASS);
    }

    @Override
    public String getTaskDescription() {
        return SmelterPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event) {
        int bar = config.BarType().getBarID();
        int item1 = config.BarType().getFirstOre();
        int item2 = config.BarType().getSecondOre();
        Widget lvlup = client.getWidget(WidgetInfo.LEVEL_UP_SKILL);

        if (playerUtils.isAnimating()) {
            SmelterPlugin.status = "Smelting";
        }

        if ( !inv.containsItem(item1 | item2) && !bank.isOpen() ) {
            SmelterPlugin.timeout = tickDelay();
            openBank();
            SmelterPlugin.timeout = tickDelay();
        }

        if ( bank.isOpen() )
        {

            if ( inv.containsItem(bar) ) {
                bank.depositAll();

            } else if ( !inv.containsItem(item1) ) {
                if ( bank.contains(item1, 14) ) {
                    bank.withdrawItemAmount(item1, 14);
                    timeout(item1);
                } else {
                    utils.sendGameMessage("out of item 1");
                }

            } else if ( !inv.containsItem(item2) && inv.containsItem(item1) ) {
                if ( bank.contains(item2, 14) ) {
                    bank.withdrawAllItem(item2);
                    timeout(item2);
                } else {
                    utils.sendGameMessage("out of item 2");
                }

            } else if (inv.isFull()) {
                useFurnace();
                SmelterPlugin.timeout = tickDelay();
            }
        }
        //handle lvl up
        if (lvlup != null ) {
            handleLvlUp(item1);
        }
        // select bar
        Widget optionMenu = client.getWidget(270, 4);
        if (optionMenu != null) {
            entry = new MenuEntry("", "", 1, MenuAction.CC_OP.getId(), -1, config.BarType().getParam1(), false );
            utils.doActionMsTime(entry, optionMenu.getBounds(), sleepDelay());
            conditionTimeout = new TimeoutUntil(
                    ()-> !inv.containsItem(item1),
                    ()-> playerUtils.isAnimating(),
                    5);
        }
        // click furnace
        if (optionMenu == null && inv.containsItem(item1) && !bank.isOpen()) {
            useFurnace();
            SmelterPlugin.timeout = tickDelay();
        }

    }

}