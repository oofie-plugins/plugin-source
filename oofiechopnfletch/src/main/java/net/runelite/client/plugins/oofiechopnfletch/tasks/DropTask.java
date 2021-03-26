package net.runelite.client.plugins.oofiechopnfletch.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.oofiechopnfletch.Task;
import net.runelite.client.plugins.oofiechopnfletch.OofieChopnFletchPlugin;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class DropTask extends Task
{
    private final Set<Integer> keepItems = new HashSet<>();

    @Override
    public boolean validate()
    {
        int logs = config.tree().getLogID();
        return inventory.containsItem(getCraftedItems()) && !inventory.containsItem(logs);
    }

    @Override
    public String getTaskDescription()
    {
        return OofieChopnFletchPlugin.status = "Dropping";
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Player player = client.getLocalPlayer();
        if (player != null)
        {
            keepItems.clear(); //Clean list incase it was changed
            keepItems.addAll(utils.stringToIntList(config.keep())); //Check list for items

            if (inventory.containsItem(getCraftedItems()))
            {
                inventory.dropAllExcept(keepItems, true, config.dropMin(), config.dropMax());
            }
        }
    }
}