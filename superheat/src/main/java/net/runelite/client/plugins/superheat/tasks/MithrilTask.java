package net.runelite.client.plugins.superheat.tasks;

import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.superheat.BarType;
import net.runelite.client.plugins.superheat.Task;
import net.runelite.client.plugins.superheat.superheatPlugin;

public class MithrilTask  extends Task {

    @Override
    public boolean validate()
    {
        return config.BarType().equals(BarType.MITHRIL);
    }

    @Override
    public String getTaskDescription()
    {
        return "Cookin' Meth: " + superheatPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event)
    {


        if (!inventory.containsItem(nat)) {
            utils.sendGameMessage("out of nature runes");
            superheatPlugin.startBot = false;
        }

        if (!inventory.containsItem(meth | coal)  && !bank.isOpen()) {
            superheatPlugin.status = "finding bank";
            GameObject bank = object.findNearestBank();
            utils.doGameObjectActionMsTime(bank, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
            timeoutBank();
        }

        if (bank.isOpen())
        {
            superheatPlugin.status = "banking";
            if (inventory.containsItem(methbar)) {
                bank.depositAllExcept(required);
                superheatPlugin.timeout = tickDelay();

            } else if (!inventory.containsItem(coal) && inventory.containsItem(meth)) {
                if ( bank.contains(coal, 27) ) {
                    bank.withdrawAllItem(coal);
                    superheatPlugin.timeout = tickDelay();
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }
            } else if (!inventory.containsItem(meth)) {
                if ( bank.contains(meth,5)) {
                    bank.withdrawItemAmount(meth, 5);
                    superheatPlugin.timeout = tickDelay();
                    timeout(meth);
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }
            } else if (inventory.isFull()) {
                bank.close();
                superheatPlugin.timeout = tickDelay();
            }
        }

        if ( inventory.containsItem(meth) && !bank.isOpen() && inventory.containsItem(nat)) {
            superheatPlugin.status = "casting";
            targetItem = inventory.getWidgetItem(meth);
            castOnItem(targetItem);
        }
    }

}
