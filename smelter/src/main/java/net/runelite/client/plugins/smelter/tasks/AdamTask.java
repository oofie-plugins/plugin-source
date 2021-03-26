package net.runelite.client.plugins.smelter.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.iutils.TimeoutUntil;
import net.runelite.client.plugins.smelter.BarType;
import net.runelite.client.plugins.smelter.Task;
import net.runelite.client.plugins.smelter.SmelterPlugin;

import static net.runelite.client.plugins.smelter.SmelterPlugin.conditionTimeout;

@Slf4j
public class AdamTask extends Task
{

    @Override
    public boolean validate()
    {
        return config.BarType().equals(BarType.ADAMANTITE);
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
            SmelterPlugin.timeout = 1 + tickDelay();
        }

        if ( !inv.containsItem(adam) && !bank.isOpen() ) {
            openBank();
            SmelterPlugin.timeout = tickDelay();
        }

        if ( bank.isOpen() )
        {
            if ( inv.containsItem(addybar) ) {
                bank.depositAll();

            } else if ( !inv.containsItem(adam) ) {
                if ( bank.contains(adam, 4) ) {
                    bank.withdrawItemAmount(adam, 4);
                    timeout(adam);
                } else {
                    utils.sendGameMessage("out of item 1");
                }

            } else if ( !inv.containsItem(coal) && inv.containsItem(adam) ) {
                if ( bank.contains(coal, 24) ) {
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
                    ()-> !inv.containsItem(adam),
                    ()-> playerUtils.isAnimating(),
                    5);
        }
        // click furnace
        if (optionMenu == null && inv.containsItem(adam) && !bank.isOpen()) {
            useFurnace();
            SmelterPlugin.timeout = tickDelay();
        }

    }
}