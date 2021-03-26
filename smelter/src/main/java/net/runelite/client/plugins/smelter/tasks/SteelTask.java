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
public class SteelTask extends Task
{

    @Override
    public boolean validate()
    {
        return config.BarType().equals(BarType.STEEL);
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
        if (config.f2p()) {
            int param = config.BarType().getParam1() - 1;
        }
        if (playerUtils.isAnimating()) {
            SmelterPlugin.status = "Smelting";
        }

        if ( !inv.containsItem(steel) && !bank.isOpen() ) {
            SmelterPlugin.timeout = tickDelay();
            openBank();
            SmelterPlugin.timeout = tickDelay();
        }

        if ( bank.isOpen() ) {

            if ( inv.containsItem(steelbar) ) {
                bank.depositAll();

            } else if ( !inv.containsItem(steel) ) {
                if ( bank.contains(steel, 9) ) {
                    bank.withdrawItemAmount(steel, 9);
                    timeout(steel);
                } else {
                    utils.sendGameMessage("out of item 1");
                }

            } else if ( !inv.containsItem(coal) && inv.containsItem(steel) ) {
                if ( bank.contains(coal, 19) ) {
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
                    ()-> !inv.containsItem(steel),
                    ()-> playerUtils.isAnimating(),
                    5);
        }
        // click furnace
        if (optionMenu == null && inv.containsItem(steel) && !bank.isOpen()) {
            useFurnace();
            SmelterPlugin.timeout = tickDelay();
        }
    }
}