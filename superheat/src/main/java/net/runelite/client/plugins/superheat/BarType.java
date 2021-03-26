package net.runelite.client.plugins.superheat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@Getter
public enum BarType {
    //    MOLTEN_GLASS("Molten Glass", 17694734, 14, ItemID.MOLTEN_GLASS ,ItemID.SODA_ASH, ItemID.BUCKET_OF_SAND),
    BRONZE("Bronze", 17694734, 14, ItemID.BRONZE_BAR, ItemID.TIN_ORE, ItemID.COPPER_ORE),
    IRON("Iron", 17694736, 16, ItemID.IRON_BAR, ItemID.IRON_ORE),
    SILVER("Silver", 17694737, 17, ItemID.SILVER_BAR, ItemID.SILVER_ORE),
    GOLD("Gold", 17694739, 19, ItemID.GOLD_BAR, ItemID.GOLD_ORE),

    STEEL("Steel", 17694738, 18, ItemID.STEEL_BAR, ItemID.IRON_ORE, ItemID.COAL, 9),
    MITHRIL("Mithril", 17694740, 20, ItemID.MITHRIL_BAR, ItemID.MITHRIL_ORE, ItemID.COAL, 5),
    ADAMANTITE("Adam", 176947341, 21, ItemID.ADAMANTITE_BAR, ItemID.ADAMANTITE_ORE, ItemID.COAL, 3),
    RUNITE("Runite", 176947342, 22, ItemID.RUNITE_BAR, ItemID.RUNITE_ORE, ItemID.COAL, 2);
    ///  oreAmt = amt of firstOre for testing purposing for now   FIRST ORE                   SECOND ORE
    private final String name;
    private final Integer param1;
    private final Integer wodget;
    private final Integer barID;
    private final Integer firstOre;
    private Integer secondOre;
    private Integer oreAmt;

    BarType(String name, Integer param1, Integer wodget, Integer barID, Integer firstOre, Integer secondOre, Integer oreAmt) {
        this.name = name;
        this.param1 = param1;
        this.wodget = wodget;
        this.barID = barID;
        this.firstOre = firstOre;
        this.secondOre = secondOre;
        this.oreAmt = oreAmt;
    }

    BarType(String name, Integer param1, Integer wodget, Integer barID, Integer firstOre, Integer secondOre) {
        this.name = name;
        this.param1 = param1;
        this.wodget = wodget;
        this.barID = barID;
        this.firstOre = firstOre;
        this.secondOre = secondOre;
    }

    BarType(String name, Integer param1, Integer wodget, Integer barID, Integer firstOre) {
        this.name = name;
        this.param1 = param1;
        this.wodget = wodget;
        this.barID = barID;
        this.firstOre = firstOre;
    }
}