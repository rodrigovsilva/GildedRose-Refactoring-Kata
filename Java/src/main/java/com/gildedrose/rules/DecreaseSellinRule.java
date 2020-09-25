package com.gildedrose.rules;

import com.gildedrose.Item;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

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
@Rule(name = "DecreaseSellin", description = "Decrease sellin rule", priority = 3)
public class DecreaseSellinRule extends BasicItemRule {

    private static final int DECREASE_SELLIN_STEP = 1;

    @Condition
    public boolean shouldDecreaseSellin(@Fact("updates") Item item) {
        return true;
    }

    @Action
    public void decreaseSellin(@Fact("updates") Item item) {
        item.sellIn = item.sellIn - DECREASE_SELLIN_STEP;
    }

}
