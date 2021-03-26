package net.runelite.client.plugins.oofiechopnfletch.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.oofiechopnfletch.Task;
import net.runelite.client.plugins.oofiechopnfletch.OofieChopnFletchPlugin;

@Slf4j
public class MovingTask extends Task
{

    @Override
    public boolean validate()
    {
        return playerUtils.isMoving(OofieChopnFletchPlugin.beforeLoc);
    }

    @Override
    public String getTaskDescription()
    {
        return OofieChopnFletchPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Player player = client.getLocalPlayer();
        if (player != null)
        {
            playerUtils.handleRun(20, 30);
            OofieChopnFletchPlugin.timeout = tickDelay();
        }
    }
}