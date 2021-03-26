package net.runelite.client.plugins.oofiekhazardminer;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iutils.*;

@Slf4j
public abstract class Task
{
    public Task()
    {
    }

    @Inject
    public Client client;

    @Inject
    public OofieKhazardMinerConfig config;

    @Inject
    public iUtils utils;

    @Inject
    public MenuUtils menu;

    @Inject
    public MouseUtils mouse;

    @Inject
    public BankUtils bank;

    @Inject
    public InventoryUtils inventory;

    @Inject
    public WalkUtils walk;

    @Inject
    public CalculationUtils calc;

    @Inject
    public PlayerUtils playerUtils;

    @Inject
    public ObjectUtils object;

    public MenuEntry entry;

    public abstract boolean validate();

    public long sleepDelay()
    {
        OofieKhazardMinerPlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
        return OofieKhazardMinerPlugin.sleepLength;
    }

    public int tickDelay()
    {
        OofieKhazardMinerPlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
        return OofieKhazardMinerPlugin.tickLength;
    }

    public String getTaskDescription()
    {
        return this.getClass().getSimpleName();
    }

    public void onGameTick(GameTick event)
    {
        return;
    }

}
