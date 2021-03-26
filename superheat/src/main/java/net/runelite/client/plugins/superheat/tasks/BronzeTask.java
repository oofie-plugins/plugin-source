package net.runelite.client.plugins.superheat.tasks;

import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.superheat.BarType;
import net.runelite.client.plugins.superheat.Task;
import net.runelite.client.plugins.superheat.superheatPlugin;

public class BronzeTask  extends Task {

    @Override
    public boolean validate()
    {
        return config.BarType().equals(BarType.BRONZE);
    }

    @Override
    public String getTaskDescription()
    {
        return "Cookin' Bronze: " + superheatPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event)
    {

        if (!inventory.containsItem(nat)) {
            utils.sendGameMessage("out of nature runes");
            superheatPlugin.startBot = false;
        }

        if (!inventory.containsItem(copper)  && !bank.isOpen()) {
            superheatPlugin.status = "finding bank";
            GameObject bank = object.findNearestBank();
            utils.doGameObjectActionMsTime(bank, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
            timeoutBank();
        }

        if (bank.isOpen()) {
            superheatPlugin.status = "banking";
            if (inventory.containsItem(bronzebar)) {
                bank.depositAllExcept(required);
                superheatPlugin.timeout = tickDelay();

            } else if (!inventory.containsItem(copper) && inventory.containsItem(tin)) {
                if ( bank.contains(copper, 14)) {
                    bank.withdrawAllItem(copper);
                    superheatPlugin.timeout = tickDelay();
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }

            } else if (!inventory.containsItem(tin)) {
                if ( bank.contains(tin,14)) {
                    bank.withdrawItemAmount(tin, 14);
                    superheatPlugin.timeout = tickDelay();
                    timeout(tin);
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }
            } else if (inventory.isFull()) {
                bank.close();
                superheatPlugin.timeout = tickDelay();
                }
        }

        if ( inventory.containsItem(copper) && !bank.isOpen() && inventory.containsItem(nat)) {
            superheatPlugin.status = "casting";
            targetItem = inventory.getWidgetItem(copper);
            castOnItem(targetItem);
        }
    }

}
