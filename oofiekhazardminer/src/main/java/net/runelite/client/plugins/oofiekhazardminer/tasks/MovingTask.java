package net.runelite.client.plugins.oofiekhazardminer.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.oofiekhazardminer.Task;
import net.runelite.client.plugins.oofiekhazardminer.OofieKhazardMinerPlugin;

@Slf4j
public class MovingTask extends Task
{

    @Override
    public boolean validate()
    {
        return playerUtils.isMoving(OofieKhazardMinerPlugin.beforeLoc);
    }

    @Override
    public String getTaskDescription()
    {
        return OofieKhazardMinerPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Player player = client.getLocalPlayer();
        if (player != null)
        {
            playerUtils.handleRun(20, 30);
            OofieKhazardMinerPlugin.timeout = tickDelay();
        }
    }
}