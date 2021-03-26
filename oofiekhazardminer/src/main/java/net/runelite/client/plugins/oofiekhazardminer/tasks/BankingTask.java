package net.runelite.client.plugins.oofiekhazardminer.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iutils.TimeoutUntil;
import net.runelite.client.plugins.oofiekhazardminer.Task;
import net.runelite.client.plugins.oofiekhazardminer.OofieKhazardMinerPlugin;

@Slf4j
public class BankingTask extends Task
{

    @Override
    public boolean validate()
    {
        return bank.isOpen();
    }

    @Override
    public String getTaskDescription()
    {
        return OofieKhazardMinerPlugin.status = "Depositing Ore";
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Player player = client.getLocalPlayer();
        if (player != null)
        {
            if (inventory.containsItem(ItemID.IRON_ORE)) {
                bank.depositAllOfItem(ItemID.IRON_ORE);
                OofieKhazardMinerPlugin.conditionTimeout = new TimeoutUntil(
                        ()-> !inventory.isFull(),
                        3);
            } else {
                bank.close();
            }
        }
    }
}