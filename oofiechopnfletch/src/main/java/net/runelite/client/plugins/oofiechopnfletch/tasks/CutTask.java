package net.runelite.client.plugins.oofiechopnfletch.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.client.plugins.iutils.TimeoutUntil;
import net.runelite.client.plugins.oofiechopnfletch.Task;
import net.runelite.client.plugins.oofiechopnfletch.OofieChopnFletchPlugin;

@Slf4j
public class CutTask extends Task
{

    @Override
    public boolean validate()
    {
        return !inventory.isFull() && !inventory.containsItem(getCraftedItems());
    }

    @Override
    public String getTaskDescription()
    {
        return OofieChopnFletchPlugin.status = "Cutting Trees";
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Player player = client.getLocalPlayer();
        if (player != null)
        {
            if (!playerUtils.isAnimating() || !playerUtils.isMoving())
            {
                GameObject tree = new GameObjectQuery()
                        .actionEquals("Chop down")
                        .nameEquals(config.tree().getName()) //double check names - "Magic" or "Magic tree"?
                        .result(client)
                        .nearestTo(client.getLocalPlayer());

                utils.doGameObjectActionMsTime(tree, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());

                OofieChopnFletchPlugin.conditionTimeout = new TimeoutUntil(
                        () -> !playerUtils.isAnimating(),
                        () -> playerUtils.isAnimating() || playerUtils.isMoving(),
                        6);

                OofieChopnFletchPlugin.timeout = tickDelay();


            }
        }
    }
}