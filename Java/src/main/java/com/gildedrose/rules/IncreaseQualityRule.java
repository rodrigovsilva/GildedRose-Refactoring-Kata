package com.gildedrose.rules;

import com.gildedrose.Item;
import com.gildedrose.constant.AppConstant;
import com.gildedrose.constant.ItemConfiguration;
import org.apache.commons.lang.StringUtils;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Rules implementation for Item quality increasing.
 * <p>
 * - Once the sell by date has passed, Quality degrades twice as fast ok
 * - The Quality of an item is never negative ok
 * - "Aged Brie" actually increases in Quality the older it gets
 * - The Quality of an item is never more than 50
 * - "Sulfuras", being a legendary item, never has to be sold or decreases in Quality
 * - "Backstage passes", like aged brie, increases in Quality as its SellIn value approaches;
 * Quality increases by 2 when there are 10 days or less
 * and by 3 when there are 5 days or less
 * but Quality drops to 0 after the concert
 * <p>
 * We have recently signed a supplier of conjured items. This requires an update to our system:
 * - "Conjured" items degrade in Quality twice as fast as normal items
 */
@Rule(name = "IncreaseQuality", description = "Increase quality rule", priority = 2)
public class IncreaseQualityRule extends BasicItemRule {

    private int qualityValueForUpdate = 1;

    @Condition
    public boolean shouldIncreaseQuality(@Fact(AppConstant.FACT_ITEM_UPDATES) Item item) {
        List<String> itemsAppliesThisRule = Arrays.asList(ItemConfiguration.AGED_BRIE.getItemName(), ItemConfiguration.BACKSTAGE_PASS_TFKAL80ETC.getItemName());

        // get item configuration
        Optional<ItemConfiguration> optCurrentConfiguration = ItemConfiguration.getItemByName(item.name);

        // check min quality condition
        if (optCurrentConfiguration.isPresent() && item.quality < MAX_ITEM_QUALITY && itemsAppliesThisRule.stream().filter((itemName) -> StringUtils.equals(itemName, item.name)).count() > 0) {

            if (optCurrentConfiguration.get().isLegendary()) {
                return false;
            }
            if (StringUtils.equals(item.name, ItemConfiguration.BACKSTAGE_PASS_TFKAL80ETC.getItemName())) {

                if (item.sellIn <= 0) {
                    return false;
                }
                if (item.sellIn <= 10 && item.sellIn > 5) {
                    qualityValueForUpdate = item.quality + 2;
                    return true;

                } else if (item.sellIn <= 5 && item.sellIn > MIN_ITEM_SELLIN) {
                    qualityValueForUpdate = item.quality + 3;
                    return true;

                } else {
                    qualityValueForUpdate = item.quality + optCurrentConfiguration.get().getBasicIncreaseFactor();
                    return true;
                }
            } else {
                qualityValueForUpdate = item.quality + optCurrentConfiguration.get().getBasicIncreaseFactor();
                return true;

            }
        }

        return false;
    }

    @Action
    public void increaseQuantity(@Fact(AppConstant.FACT_ITEM_UPDATES) Item item) {
        item.quality = qualityValueForUpdate;
        if (item.quality > MAX_ITEM_QUALITY) {
            item.quality = MAX_ITEM_QUALITY;
        }
    }

}
