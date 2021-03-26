package net.runelite.client.plugins.superheat.tasks;

import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.superheat.BarType;
import net.runelite.client.plugins.superheat.Task;
import net.runelite.client.plugins.superheat.superheatPlugin;

public class IronSilverGoldTask extends Task{

    @Override
    public boolean validate()
    {
        return config.BarType().equals(BarType.IRON)
                || config.BarType().equals(BarType.SILVER)
                || config.BarType().equals(BarType.GOLD);
    }

    @Override
    public String getTaskDescription()
    {
        return "Cookin' Iron/Silver/Gold: " + superheatPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event)
    {


         int bar = config.BarType().getBarID();
         int ore = config.BarType().getFirstOre();

        if (!inventory.containsItem(nat)) {
            utils.sendGameMessage("out of nature runes");
            superheatPlugin.startBot = false;
        }

        if (!inventory.containsItem(ore)  && !bank.isOpen()) {
            superheatPlugin.status = "finding bank";
            openBank();
        }

        if (bank.isOpen())
        {
            superheatPlugin.status = "banking";
            if (inventory.containsItem(bar)) {
                bank.depositAllExcept(required);
                superheatPlugin.timeout = tickDelay();

            } else if (!inventory.containsItem(ore)) {
                if ( bank.contains(ore,27)) {
                    bank.withdrawAllItem(ore);
                    superheatPlugin.timeout = tickDelay();
                    timeout(ore);
                } else {
                    utils.sendGameMessage("out of ore");
                    superheatPlugin.startBot = false;
                }
            } else if (inventory.isFull()) {
                bank.close();
                superheatPlugin.timeout = tickDelay();
            }
        }

        if ( inventory.containsItem(ore) && !bank.isOpen() && inventory.containsItem(nat)) {
            superheatPlugin.status = "casting";
            targetItem = inventory.getWidgetItem(ore);
            castOnItem(targetItem);
        }

    }

}
