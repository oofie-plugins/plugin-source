package net.runelite.client.plugins.superheat.tasks;

import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.superheat.BarType;
import net.runelite.client.plugins.superheat.Task;
import net.runelite.client.plugins.superheat.superheatPlugin;

public class AdamTask  extends Task {

    @Override
    public boolean validate() {
        return config.BarType().equals(BarType.ADAMANTITE);
    }

    @Override
    public String getTaskDescription() {
        return "Cookin' Adam: " + superheatPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event) {


        if (!inventory.containsItem(nat)) {
            utils.sendGameMessage("out of nature runes");
            superheatPlugin.startBot = false;
        }

        if (!inventory.containsItem(adam | coal) && !bank.isOpen()) {
           superheatPlugin.status = "finding bank";
           GameObject bank = object.findNearestBank();
           utils.doGameObjectActionMsTime(bank, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
            timeoutBank();
        }

        if (bank.isOpen())
        {
            superheatPlugin.status = "banking";
            if (inventory.containsItem(addybar)) {
                bank.depositAllExcept(required);
                superheatPlugin.timeout = tickDelay();

            } else if (!inventory.containsItem(coal) && inventory.containsItem(adam)) {
                if ( bank.contains(coal, 27) ) {
                    bank.withdrawAllItem(coal);
                    superheatPlugin.timeout = 1 + tickDelay();
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }
            } else if (!inventory.containsItem(adam)) {
                if ( bank.contains(adam,3)) {
                    bank.withdrawItemAmount(adam, 3);
                    superheatPlugin.timeout = 1 + tickDelay();
                    timeout(adam);
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }
            } else if (inventory.isFull()) {
                    bank.close();
                    superheatPlugin.timeout = tickDelay();
                }
        }

        if ( inventory.containsItem(adam) && !bank.isOpen() && inventory.containsItem(nat)) {
            superheatPlugin.status = "casting";
            targetItem = inventory.getWidgetItem(adam);
            castOnItem(targetItem);
        }

    }

}
