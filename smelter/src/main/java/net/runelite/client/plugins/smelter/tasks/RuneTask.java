package net.runelite.client.plugins.smelter.tasks;

import lombok.extern.slf4j.Slf4j;
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
public class RuneTask extends Task
{

    @Override
    public boolean validate()
    {
        return config.BarType().equals(BarType.RUNITE);
    }

    @Override
    public String getTaskDescription()
    {
        return SmelterPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Widget lvlup = client.getWidget(WidgetInfo.LEVEL_UP_SKILL);

        if (playerUtils.isAnimating()) {
            SmelterPlugin.status = "Smelting";
        }

        if ( !inv.containsItem(rune) && !bank.isOpen() ) {
            SmelterPlugin.timeout = tickDelay();
            openBank();
            SmelterPlugin.timeout = tickDelay();
        }

        if ( bank.isOpen() ) {

            if ( inv.containsItem(runebar) ) {
                bank.depositAll();

            } else if ( !inv.containsItem(rune) ) {
                if ( bank.contains(rune, 3) ) {
                    bank.withdrawItemAmount(rune, 3);
                    timeout(rune);
                } else {
                    utils.sendGameMessage("out of item 1");
                }

            } else if ( !inv.containsItem(coal) && inv.containsItem(rune) ) {
                if ( bank.contains(coal, 25) ) {
                    bank.withdrawAllItem(coal);
                    timeout(coal);
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
            handleLvlUp(steel);

        }
        // select bar
        Widget optionMenu = client.getWidget(270, 4);
        if (optionMenu != null) {
            entry = new MenuEntry("", "", 1, MenuAction.CC_OP.getId(), -1, SmelterPlugin.param, false );
            utils.doActionMsTime(entry, optionMenu.getBounds(), sleepDelay());
            conditionTimeout = new TimeoutUntil(
                    ()-> !inv.containsItem(rune),
                    ()-> playerUtils.isAnimating(),
                    5);
        }
        // click furnace
        if (optionMenu == null && inv.containsItem(rune) && !bank.isOpen()) {
            useFurnace();
            SmelterPlugin.timeout = tickDelay();
        }
    }
}