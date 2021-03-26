package net.runelite.client.plugins.oofiechopnfletch.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.iutils.TimeoutUntil;
import net.runelite.client.plugins.oofiechopnfletch.Task;
import net.runelite.client.plugins.oofiechopnfletch.OofieChopnFletchPlugin;

@Slf4j
public class FletchTask extends Task
{

    @Override
    public boolean validate()
    {
        int logs = config.tree().getLogID();
        return inventory.containsItem(logs);
    }

    @Override
    public String getTaskDescription()
    {
        return OofieChopnFletchPlugin.status = "Fletching";
    }

    @Override
    public void onGameTick(GameTick event)
    {
        int logs = config.tree().getLogID();
        WidgetItem log = inventory.getWidgetItem(config.tree().getLogID());
        WidgetItem knife = inventory.getWidgetItem(ItemID.KNIFE);
        Widget option = client.getWidget(270, 0);
        Player player = client.getLocalPlayer();

        if (logsCut == 0) {
            logsCut = inventory.getItemCount(config.tree().getLogID(), false);
        }

        if (player != null)
        {
            if (option == null && !playerUtils.isAnimating())
            {

                entry = new MenuEntry("", "", log.getId(), MenuAction.ITEM_USE_ON_WIDGET_ITEM.getId(), log.getIndex(), 9764864, false);
                utils.doModifiedActionMsTime(entry, knife.getId(), knife.getIndex(), MenuAction.ITEM_USE_ON_WIDGET_ITEM.getId(), log.getCanvasBounds(), sleepDelay());
                OofieChopnFletchPlugin.timeout = tickDelay();


            }

            else if (option != null)
            {
                chooseOption();

            }
        }
    }

    private void chooseOption()
    {
        WidgetItem log = inventory.getWidgetItem(config.tree().getLogID());
        Widget option = client.getWidget(270, 0);

        if (option != null) //make sure the skill selection exists before clicking
        {
            entry = new MenuEntry("", "", 1, MenuAction.CC_OP.getId(), -1, 17694730+config.param(), false);

            utils.doActionMsTime(entry, option.getBounds(), sleepDelay());

            OofieChopnFletchPlugin.conditionTimeout = new TimeoutUntil( //We don't do anything until all the logs are fletched, if we still have logs --> fletch again.
                    () -> !inventory.containsItem(log.getId()),
                    () -> playerUtils.isAnimating(), //if we are animation --> extend timeout
                    5);

            OofieChopnFletchPlugin.timeout = tickDelay();
        }
    }
}