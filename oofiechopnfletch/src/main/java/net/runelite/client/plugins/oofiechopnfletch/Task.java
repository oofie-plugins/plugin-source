package net.runelite.client.plugins.oofiechopnfletch;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.Menu;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.iutils.*;
import net.runelite.client.plugins.iutils.bot.Bot;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class Task
{
    public Task()
    {
    }

    @Inject
    public Client client;

    @Inject
    public OofieChopnFletchConfig config;

    @Inject
    public iUtils utils;

    @Inject
    public MenuUtils menu;

    @Inject
    public MouseUtils mouse;

    @Inject
    public CalculationUtils calc;

    @Inject
    public PlayerUtils playerUtils;

    @Inject
    public InventoryUtils inventory;

    @Inject
    public ObjectUtils object;

    public MenuEntry entry;


    public int logsCut;


    public abstract boolean validate();

    public long sleepDelay()
    {
        OofieChopnFletchPlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
        return OofieChopnFletchPlugin.sleepLength;
    }

    public int tickDelay()
    {
        OofieChopnFletchPlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
        return OofieChopnFletchPlugin.tickLength;
    }

    public String getTaskDescription()
    {
        return this.getClass().getSimpleName();
    }

    public void onGameTick(GameTick event)
    {
        return;
    }

    public Set<Integer> getCraftedItems() { //ID's of all (u) bows
        return Set.of(48, 56, 58, 62, 66, 70, 50, 54, 60, 64, 68, 72);
    }




}