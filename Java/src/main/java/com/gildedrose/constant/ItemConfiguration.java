package com.gildedrose.constant;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum with all available item names.
 */
public enum ItemConfiguration {

    AGED_BRIE("Aged Brie", 0, 1, false),
    BACKSTAGE_PASS_TFKAL80ETC("Backstage passes to a TAFKAL80ETC concert", 0, 1, false),
    SULFURAS("Sulfuras, Hand of Ragnaros", 0, 0, true),
    CONJURED_MANA_CAKE("Conjured Mana Cake", 2, 0, false),
    DEXTERITY_VEST("+5 Dexterity Vest", 1, 0, false),
    ELIXIR_MONGOOSE("Elixir of the Mongoose", 1, 0, false);

    private String itemName;
    private int basicDecreaseFactor;
    private int basicIncreaseFactor;
    private boolean legendary;

    ItemConfiguration(String itemName, int basicDecreaseFactor, int basicIncreaseFactor, boolean legendary) {
        this.itemName = itemName;
        this.basicDecreaseFactor = basicDecreaseFactor;
        this.basicIncreaseFactor = basicIncreaseFactor;
        this.legendary = legendary;
    }

    public String getItemName() {
        return itemName;
    }

    public int getBasicDecreaseFactor() {
        return basicDecreaseFactor;
    }

    public int getBasicIncreaseFactor() {
        return basicIncreaseFactor;
    }

    public boolean isLegendary() {
        return legendary;
    }

    public static Optional<ItemConfiguration> getItemByName(String name) {
        return Arrays.stream(ItemConfiguration.values()).filter(itemConfiguration -> StringUtils.equals(name, itemConfiguration.itemName)).findFirst();
    }
}
