package net.runelite.client.plugins.oofiekhazardminer.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iutils.TimeoutUntil;
import net.runelite.client.plugins.oofiekhazardminer.Task;
import net.runelite.client.plugins.oofiekhazardminer.OofieKhazardMinerPlugin;

@Slf4j
public class OpenBankTask extends Task
{

    @Override
    public boolean validate()
    {
        return inventory.isFull() && !bank.isOpen();
    }

    @Override
    public String getTaskDescription()
    {
        return OofieKhazardMinerPlugin.status = "Opening Bank";
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Player player = client.getLocalPlayer();
        if (player != null)
        {
           GameObject BANK = object.findNearestBank();
           utils.doGameObjectActionMsTime(BANK, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), sleepDelay());
           OofieKhazardMinerPlugin.conditionTimeout = new TimeoutUntil(
                   ()-> bank.isOpen(),
                   ()-> playerUtils.isMoving(),
                   4);
        }
    }
}