package net.runelite.client.plugins.superheat.tasks;

import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.superheat.BarType;
import net.runelite.client.plugins.superheat.Task;
import net.runelite.client.plugins.superheat.superheatPlugin;

public class SteelTask  extends Task {

    @Override
    public boolean validate()
    {
        return config.BarType().equals(BarType.STEEL);
    }

    @Override
    public String getTaskDescription()
    {
        return "Cookin' Steel: " + superheatPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event)
    {


        if (!inventory.containsItem(nat)) {
            utils.sendGameMessage("out of nature runes");
            superheatPlugin.startBot = false;
        }

        if (!inventory.containsItem(steel | coal)  && !bank.isOpen()) {
            superheatPlugin.status = "finding bank";
            GameObject bank = object.findNearestBank();
            utils.doGameObjectActionMsTime(bank, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
            timeoutBank();
        }

        if (bank.isOpen())
        {
            superheatPlugin.status = "banking";
            if (inventory.containsItem(steelbar)) {
                bank.depositAllExcept(required);
                superheatPlugin.timeout = tickDelay();

            } else if (!inventory.containsItem(coal) && inventory.containsItem(steel)) {
                if ( bank.contains(coal, 27) ) {
                    bank.withdrawAllItem(coal);
                    superheatPlugin.timeout = tickDelay();
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }
            } else if (!inventory.containsItem(steel)) {
                if ( bank.contains(steel,9)) {
                    bank.withdrawItemAmount(steel, 9);
                    superheatPlugin.timeout = tickDelay();
                    timeout(steel);
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }
            } else if (inventory.isFull()) {
                bank.close();
                superheatPlugin.timeout = tickDelay();
            }
        }

        if ( inventory.containsItem(steel) && !bank.isOpen() && inventory.containsItem(nat)) {
            superheatPlugin.status = "casting";
            targetItem = inventory.getWidgetItem(steel);
            castOnItem(targetItem);
        }
    }

}
