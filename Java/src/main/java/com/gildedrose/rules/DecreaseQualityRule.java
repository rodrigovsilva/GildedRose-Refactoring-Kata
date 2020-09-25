package com.gildedrose.rules;

import com.gildedrose.Item;
import com.gildedrose.constant.ItemConfiguration;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Rules implementation for Item Sellin decreasing.
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
@Rule(name = "DecreaseQuality", description = "Decrease quality rule", priority = 1)
public class DecreaseQualityRule extends BasicItemRule {

    private int qualityValueForUpdate = 1;

    private static final int NEGATIVE_SELLIN_DECREASE_FACTOR = 2;

    @Condition
    public boolean shouldDecreaseQuantity(@Fact("updates") Item item) {

        // define items apply this rule
        List<String> itemsNeverAppliesThisRule = Arrays.asList(ItemConfiguration.AGED_BRIE.getItemName(), ItemConfiguration.SULFURAS.getItemName());

        // get item configuration
        Optional<ItemConfiguration> optCurrentConfiguration = ItemConfiguration.getItemByName(item.name);

        // check min quality condition
        if (optCurrentConfiguration.isPresent() && item.quality > MIN_ITEM_QUALITY) {

            //check if item applies this rule
            if (itemsNeverAppliesThisRule.stream().filter((itemName) -> StringUtils.equals(itemName, item.name)).count() == 0) {

                // check custom condition for backstage item
                if (ObjectUtils.equals(optCurrentConfiguration.get(), ItemConfiguration.BACKSTAGE_PASS_TFKAL80ETC)) {
                    if (item.sellIn == 0) {
                        qualityValueForUpdate = optCurrentConfiguration.get().getBasicDecreaseFactor();
                        return true;
                    }
                } else {
                    if (item.sellIn <= MIN_ITEM_SELLIN) {
                        qualityValueForUpdate = optCurrentConfiguration.get().getBasicDecreaseFactor() * NEGATIVE_SELLIN_DECREASE_FACTOR;

                    } else {
                        qualityValueForUpdate = optCurrentConfiguration.get().getBasicDecreaseFactor();
                    }
                    return true;
                }
            }
        }

        return false;
    }

    @Action
    public void decreaseQuantity(@Fact("updates") Item item) {
        item.quality = item.quality - qualityValueForUpdate;
    }

}
