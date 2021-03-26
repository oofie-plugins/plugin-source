package net.runelite.client.plugins.smelter.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.smelter.Task;
import net.runelite.client.plugins.smelter.SmelterPlugin;

@Slf4j
public class MovingTask extends Task
{

	@Override
	public boolean validate()
	{
		return playerUtils.isMoving(SmelterPlugin.beforeLoc);
	}

	@Override
	public String getTaskDescription()
	{
		return SmelterPlugin.status;
	}

	@Override
	public void onGameTick(GameTick event)
	{
		Player player = client.getLocalPlayer();
		if (player != null)
		{
			playerUtils.handleRun(20, 30);
			SmelterPlugin.timeout = tickDelay();
		}
	}
}