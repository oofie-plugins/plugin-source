package net.runelite.client.plugins.oofiechopnfletch;

import com.google.inject.Injector;
import com.google.inject.Provides;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;

import com.sun.source.doctree.SerialTree;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import com.owain.chinbreakhandler.ChinBreakHandler;
import net.runelite.client.plugins.iutils.ConditionTimeout;
import net.runelite.client.plugins.iutils.InventoryUtils;
import net.runelite.client.plugins.iutils.PlayerUtils;
import net.runelite.client.plugins.oofiechopnfletch.tasks.*;
import net.runelite.client.plugins.iutils.iUtils;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;


import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.util.*;

@Extension
@PluginDependency(iUtils.class)
@PluginDescriptor(
    name = "Oofie's Chop n Fletch",
    enabledByDefault = false,
    description = "Chops n Fletch n Drop",
    tags = {"chop", "n", "fletch", "oofie"}
)
@Slf4j
public class OofieChopnFletchPlugin extends Plugin
{
    @Inject
    private Injector injector;

    @Inject
    private Client client;

    @Inject
    private OofieChopnFletchConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private OofieChopnFletchOverlay overlay;

    @Inject
    private iUtils utils;

    @Inject
    private InventoryUtils inventory;

    @Inject
    private PlayerUtils playa;

    @Inject
    public ChinBreakHandler chinBreakHandler;

    @Inject
    private ConfigManager configManager;

    private TaskSet tasks = new TaskSet();
    public static LocalPoint beforeLoc = new LocalPoint(0, 0);

    MenuEntry targetMenu;
    Instant botTimer;
    Player player;

    public static boolean startBot;
    public static long sleepLength;
    public static int tickLength;
    public static int timeout;
    public static String status = "starting...";
    public static ConditionTimeout conditionTimeout;
    public static boolean timeoutFinished;


    @Provides
    OofieChopnFletchConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(OofieChopnFletchConfig.class);
    }

    @Override
    protected void startUp()
    {
        chinBreakHandler.registerPlugin(this);
    }

    @Override
    protected void shutDown()
    {
        resetVals();
        chinBreakHandler.unregisterPlugin(this);
    }


    private void loadTasks()
    {
        tasks.clear();
        tasks.addAll(
            injector.getInstance(TimeoutTask.class),
            injector.getInstance(MovingTask.class),
            injector.getInstance(CutTask.class),
            injector.getInstance(FletchTask.class),
            injector.getInstance(DropTask.class)
        );
    }

    public void resetVals()
    {
        log.debug("stopping chop n fletch");
        overlayManager.remove(overlay);
        chinBreakHandler.stopPlugin(this);
        startBot = false;
        botTimer = null;
        tasks.clear();
    }
    @Subscribe
    private void onConfigButtonPressed(ConfigButtonClicked configButtonClicked)
    {
        if (!configButtonClicked.getGroup().equalsIgnoreCase("OofieChopnFletch"))
        {
            return;
        }
        log.debug("button {} pressed!", configButtonClicked.getKey());
        if (configButtonClicked.getKey().equals("startButton"))
        {
            if (!startBot)
            {
                Player player = client.getLocalPlayer();
                if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN)
                {
                    log.info("starting chop n fletch");
                    loadTasks();
                    startBot = true;
                    chinBreakHandler.startPlugin(this);
                    timeout = 0;
                    targetMenu = null;
                    botTimer = Instant.now();
                    overlayManager.add(overlay);
                    beforeLoc = client.getLocalPlayer().getLocalLocation();
                }
                else
                {
                    log.info("Start logged in");
                }
            }
            else
            {
                resetVals();
            }
        }
        if (configButtonClicked.getKey().equals("setHWID"))
        {
//            final ItemContainer inventory = client.getItemContainer(InventoryID.EQUIPMENT);
//
//            if (inventory == null)
//            {
//                log.error("CopyCS: Can't find equipment container.");
//                return;
//            }
//
//            final StringBuilder sb = new StringBuilder();
//
//            for (Item item : inventory.getItems())
//            {
//                if (item.getId() == -1 || item.getId() == 0)
//                {
//                    continue;
//                }
//
//                sb.append(item.getId());
//                sb.append(":");
//                sb.append("Equip");
//                sb.append("\n");
//            }
//
//            Toolkit.getDefaultToolkit()
//                    .getSystemClipboard()
//                    .setContents(
//                            new StringSelection(String.valueOf(sb)),
//                            null
//                    );

//            Toolkit.getDefaultToolkit()
//                    .getSystemClipboard()
//                    .setContents(
//                            new StringSelection(String.valueOf(client.getLocalPlayer().getWorldLocation())),
//                            null
//                    );

            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(
                            new StringSelection(generateHWID()),
                            null
                    );
        }
    }
//    String hwid;


    public static String generateHWID() {
        try {
            MessageDigest hash = MessageDigest.getInstance("MD5");
            String var10000 = System.getProperty("os.name");
            String s = var10000 + System.getProperty("os.arch") + Runtime.getRuntime().availableProcessors() + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS");
            byte[] md5sum = hash.digest(s.getBytes());
            return String.format("%032X", new BigInteger(1, md5sum));
        } catch (NoSuchAlgorithmException var4) {
            throw new Error("Algorithm wasn't found.", var4);
        }
    }



    @Subscribe
    private void onGameTick(GameTick event)
    {
        if (!startBot || chinBreakHandler.isBreakActive(this))
        {
            return;
        }
        player = client.getLocalPlayer();
        if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN)
        {

            if (chinBreakHandler.shouldBreak(this))
            {
                status = "Taking a break";
                chinBreakHandler.startBreak(this);
                timeout = 5;
            }

            Task task = tasks.getValidTask();
            if (task != null)
            {
                status = task.getTaskDescription();
                task.onGameTick(event);

                if (timeoutFinished)
                {
                    if (timeout > 0)
                    {
                        return;
                    }

                    Task newTask = tasks.getValidTask();
                    if (newTask != null)
                    {
                        newTask.onGameTick(event);
                        status = task.getTaskDescription();
                    } else
                    {
                        status = "Idle";
                    }

                    timeoutFinished = false;
                }
            }
            else
            {
                status = "Task not found";
                log.debug(status);
            }
            beforeLoc = player.getLocalLocation();
        }
    }
}