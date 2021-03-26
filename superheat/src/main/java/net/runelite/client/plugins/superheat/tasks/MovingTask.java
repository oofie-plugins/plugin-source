package net.runelite.client.plugins.superheat.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.superheat.Task;
import net.runelite.client.plugins.superheat.superheatPlugin;

@Slf4j
public class MovingTask extends Task
{

	@Override
	public boolean validate()
	{
		return playerUtils.isMoving(superheatPlugin.beforeLoc);
	}

	@Override
	public String getTaskDescription()
	{
		return superheatPlugin.status;
	}

	@Override
	public void onGameTick(GameTick event)
	{
		Player player = client.getLocalPlayer();
		if (player != null)
		{
			playerUtils.handleRun(20, 30);
			superheatPlugin.timeout = tickDelay();
		}
	}
}