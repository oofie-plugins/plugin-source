package net.runelite.client.plugins.oofiechopnfletch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;

@Getter
@AllArgsConstructor

public enum Trees
{
    TREE("Tree,", ItemID.LOGS),
    OAK("Oak", ItemID.OAK_LOGS),
    TEAK("Teak", ItemID.TEAK_LOGS),
    MAPLE("Maple tree", ItemID.MAPLE_LOGS),
    MAHOGANY("Mahogany tree", ItemID.MAHOGANY_LOGS),
    YEW("Yew", ItemID.YEW_LOGS),
    MAGIC("Magic tree", ItemID.MAGIC_TREE),
    REDWOOD("Redwood tree", ItemID.REDWOOD_LOGS);

    private final String name;
    private final Integer logID;

    @Override
    public String toString()
    {
        return getName();
    }
}

