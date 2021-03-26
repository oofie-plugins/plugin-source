package net.runelite.client.plugins.oofiekittengrower;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.iutils.*;
import net.runelite.client.plugins.iutils.TimeoutWhile;
import net.runelite.client.plugins.iutils.TimeoutUntil;
import net.runelite.client.plugins.iutils.ConditionTimeout;

@Slf4j
public abstract class Task
{
    public Task()
    {
    }

    @Inject
    public Client client;

    @Inject
    public OofieKittenGrowerConfig config;

    @Inject
    public iUtils utils;

    @Inject
    public MenuUtils menu;

    @Inject
    public NPCUtils npc;

    @Inject
    public InventoryUtils inventory;

    @Inject
    public MouseUtils mouse;

    @Inject
    public CalculationUtils calc;

    @Inject
    public PlayerUtils playerUtils;

    @Inject
    public ObjectUtils object;

    public MenuEntry targetMenu;

    public abstract boolean validate();

    public long sleepDelay()
    {
        OofieKittenGrowerPlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
        return OofieKittenGrowerPlugin.sleepLength;
    }

    public int tickDelay()
    {
        OofieKittenGrowerPlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
        return OofieKittenGrowerPlugin.tickLength;
    }

    public String getTaskDescription()
    {
        return this.getClass().getSimpleName();
    }

    public void onGameTick(GameTick event)
    {
        return;
    }

    public void pssPssPss()
    {
        NPC kitten = npc.findNearestNpc("Kitten");
        Widget whistle = client.getWidget(387, 8);
        Widget warning = client.getWidget(217, 3);

        if (warning != null)
        {
            continuePlayerDialog(sleepDelay());
            OofieKittenGrowerPlugin.timeout = 1;
        } else {

            targetMenu = new MenuEntry("", "", 1, MenuAction.CC_OP.getId(), -1, 25362439, false);
            utils.doActionMsTime(targetMenu, whistle.getBounds(), sleepDelay());

            OofieKittenGrowerPlugin.conditionTimeout = new TimeoutUntil(
                    () -> client.getLocalPlayer().getWorldLocation().distanceTo(kitten.getWorldLocation()) < 2,
                    3);
        }
    }

    public void continuePlayerDialog(long delay)
    {
        MenuEntry entry = new MenuEntry("Continue", "", 0, MenuAction.WIDGET_TYPE_6.getId(), -1, 14221315, false);
        utils.doActionMsTime(entry, new Point(0, 0), delay);
    }

}
