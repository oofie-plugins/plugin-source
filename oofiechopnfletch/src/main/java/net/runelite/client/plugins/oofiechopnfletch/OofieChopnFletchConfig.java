package net.runelite.client.plugins.oofiechopnfletch;

import net.runelite.client.config.Button;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("OofieChopnFletch")
public interface OofieChopnFletchConfig extends Config
{

    @ConfigSection(
        keyName = "delayConfig",
        name = "Sleep Delay Configuration",
        description = "Configure how the bot handles sleep delays",
        closedByDefault = true,
        position = 2
    )
    String delayConfig = "delayConfig";

    @Range(
        min = 0,
        max = 550
    )
    @ConfigItem(
        keyName = "sleepMin",
        name = "Sleep Min",
        description = "",
        position = 3,
        section = "delayConfig"
    )
    default int sleepMin()
    {
        return 60;
    }

    @Range(
        min = 0,
        max = 550
    )
    @ConfigItem(
        keyName = "sleepMax",
        name = "Sleep Max",
        description = "",
        position = 4,
        section = "delayConfig"
    )
    default int sleepMax()
    {
        return 350;
    }

    @Range(
        min = 0,
        max = 550
    )
    @ConfigItem(
        keyName = "sleepTarget",
        name = "Sleep Target",
        description = "",
        position = 5,
        section = "delayConfig"
    )
    default int sleepTarget()
    {
        return 100;
    }

    @Range(
        min = 0,
        max = 550
    )
    @ConfigItem(
        keyName = "sleepDeviation",
        name = "Sleep Deviation",
        description = "",
        position = 6,
        section = "delayConfig"
    )
    default int sleepDeviation()
    {
        return 10;
    }

    @ConfigItem(
        keyName = "sleepWeightedDistribution",
        name = "Sleep Weighted Distribution",
        description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
        position = 7,
        section = "delayConfig"
    )
    default boolean sleepWeightedDistribution()
    {
        return false;
    }

    @ConfigSection(
        keyName = "delayTickConfig",
        name = "Game Tick Configuration",
        description = "Configure how the bot handles game tick delays, 1 game tick equates to roughly 600ms",
        closedByDefault = true,
        position = 10
    )
    String delayTickConfig = "delayTickConfig";

    @Range(
        min = 0,
        max = 10
    )
    @ConfigItem(
        keyName = "tickDelayMin",
        name = "Game Tick Min",
        description = "",
        position = 11,
        section = "delayTickConfig"
    )
    default int tickDelayMin()
    {
        return 1;
    }

    @Range(
        min = 0,
        max = 10
    )
    @ConfigItem(
        keyName = "tickDelayMax",
        name = "Game Tick Max",
        description = "",
        position = 12,
        section = "delayTickConfig"
    )
    default int tickDelayMax()
    {
        return 3;
    }

    @Range(
        min = 0,
        max = 10
    )
    @ConfigItem(
        keyName = "tickDelayTarget",
        name = "Game Tick Target",
        description = "",
        position = 13,
        section = "delayTickConfig"
    )
    default int tickDelayTarget()
    {
        return 2;
    }

    @Range(
        min = 0,
        max = 10
    )
    @ConfigItem(
        keyName = "tickDelayDeviation",
        name = "Game Tick Deviation",
        description = "",
        position = 14,
        section = "delayTickConfig"
    )
    default int tickDelayDeviation()
    {
        return 1;
    }

    @ConfigItem(
        keyName = "tickDelayWeightedDistribution",
        name = "Game Tick Weighted Distribution",
        description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
        position = 15,
        section = "delayTickConfig"
    )
    default boolean tickDelayWeightedDistribution()
    {
        return false;
    }

    @ConfigItem(
            keyName = "tree",
            name = "tree selection",
            description = "Choose Tree to Choppity chop",
            position = 16
    )
    default Trees tree()
    {
        return Trees.TREE;
    }

    @ConfigItem(
            keyName = "param",
            name = "Short = 5 | Long = 6",
            description = " ",
            position = 17
    )
    default int param()
    {
        return 6;
    }

    @ConfigItem(
            keyName = "drop",
            name = "Don't Drop",
            description = "946=Knife. Item ID's to NOT drop",
            position = 18
    )
    default String keep() { return "946"; }

    @ConfigItem(
            keyName = "dropMin",
            name = "Drop Speed Min",
            description = "In Ms, MIN drop speed",
            position = 19
    )
    default int dropMin()
    {
        return 300;
    }

    @ConfigItem(
            keyName = "dropMax",
            name = "Drop Speed Max",
            description = "In Ms, MAX drop speed",
            position = 20
    )
    default int dropMax()
    {
        return 500;
    }



    @ConfigSection(
        keyName = "instructionsTitle",
        name = "Instructions",
        description = "",
        position = 0
    )
    String instructionsTitle = "instructionsTitle";

    @ConfigItem(
        keyName = "instructions",
        name = "",
        description = "Instructions. Don't enter anything into this field",
        position = 1,
        section = "instructionsTitle"
    )
    default String instructions()
    {
        return "Short = 5\n" + "Long = 6";
    }

    @ConfigItem(
        keyName = "enableUI",
        name = "Enable UI",
        description = "Enable to turn on in game UI",
        position = 95
    )
    default boolean enableUI()
    {
        return true;
    }

    @ConfigItem(
        keyName = "startButton",
        name = "Start/Stop",
        description = "Test button that changes variable value",
        position = 100
    )
    default Button startButton()
    {
        return new Button();
    }
}
