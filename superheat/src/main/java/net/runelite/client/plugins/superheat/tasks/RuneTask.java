package net.runelite.client.plugins.superheat.tasks;

import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.superheat.BarType;
import net.runelite.client.plugins.superheat.Task;
import net.runelite.client.plugins.superheat.TimeoutUntil;
import net.runelite.client.plugins.superheat.superheatPlugin;

public class RuneTask  extends Task {

    @Override
    public boolean validate()
    {
        return config.BarType().equals(BarType.RUNITE);
    }

    @Override
    public String getTaskDescription()
    {
        return "Rune" + superheatPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event)
    {

        if (!inventory.containsItem(nat)) {
            utils.sendGameMessage("out of nature runes");
            superheatPlugin.startBot = false;
        }

        if (!inventory.containsItem(rune | coal)  && !bank.isOpen()) {
            superheatPlugin.status = "finding bank";
            GameObject bank = object.findNearestBank();
            utils.doGameObjectActionMsTime(bank, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
            timeoutBank();
        }

        if (bank.isOpen())
        {
            superheatPlugin.status = "banking";
            if (inventory.containsItem(runebar)) {
                bank.depositAllExcept(required);
                superheatPlugin.timeout = tickDelay();

            } else if (!inventory.containsItem(coal) && inventory.containsItem(rune)) {
                if ( bank.contains(coal, 27) ) {
                    bank.withdrawAllItem(coal);
                    superheatPlugin.timeout = tickDelay();
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }
            } else if (!inventory.containsItem(rune)) {
                if ( bank.contains(rune,2)) {
                    bank.withdrawItemAmount(rune, 2);
                    timeout(rune);
                    superheatPlugin.timeout = tickDelay();
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }
            } else if (inventory.isFull()) {
                bank.close();
                superheatPlugin.timeout = tickDelay();
            }
        }

        if ( inventory.containsItem(rune) && !bank.isOpen() && inventory.containsItem(nat)) {
            superheatPlugin.status = "casting";
            targetItem = inventory.getWidgetItem(rune);
            castOnItem(targetItem);
        }
    }


}