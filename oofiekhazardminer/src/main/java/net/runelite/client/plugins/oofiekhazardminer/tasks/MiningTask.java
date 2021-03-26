package net.runelite.client.plugins.oofiekhazardminer.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iutils.TimeoutUntil;
import net.runelite.client.plugins.iutils.TimeoutWhile;
import net.runelite.client.plugins.oofiekhazardminer.Task;
import net.runelite.client.plugins.oofiekhazardminer.OofieKhazardMinerPlugin;

@Slf4j
public class MiningTask extends Task
{

    WorldArea IRON_ORE = new WorldArea(new WorldPoint(2622,3146,0),new WorldPoint(2629,3153,0));

    @Override
    public boolean validate()
    {
        return !inventory.isFull() && !bank.isOpen();
    }

    @Override
    public String getTaskDescription()
    {
        return OofieKhazardMinerPlugin.status = "Mining";
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Player player = client.getLocalPlayer();
        if (player != null)
        {
            if (!IRON_ORE.intersectsWith(player.getWorldArea())) {
                walk.sceneWalk((new WorldPoint(2626 + calc.getRandomIntBetweenRange(0, 1), 3151 + calc.getRandomIntBetweenRange(0, 2), 0)), 1, sleepDelay());
                OofieKhazardMinerPlugin.conditionTimeout = new TimeoutUntil(
                        ()-> IRON_ORE.intersectsWith(client.getLocalPlayer().getWorldArea()),
                        ()-> playerUtils.isMoving(),
                        5);
            } else {
                OofieKhazardMinerPlugin.timeout = 1;
                GameObject ironOre = object.findNearestGameObject(11365,11364);
                utils.doGameObjectActionMsTime(ironOre, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(),sleepDelay());
                OofieKhazardMinerPlugin.conditionTimeout = new TimeoutWhile(
                        ()-> playerUtils.isAnimating(),
                        5);
                OofieKhazardMinerPlugin.timeout = tickDelay();
            }
        }
    }
}