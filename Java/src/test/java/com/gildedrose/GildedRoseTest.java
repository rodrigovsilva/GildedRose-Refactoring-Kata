package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

/*    @Test
    void foo() {
        Item[] items = new Item[]{
                new Item("+5 Dexterity Vest", 10, 20), //
                new Item("Aged Brie", 2, 0), //
                new Item("Aged Brie", 2, 50), //
                new Item("Elixir of the Mongoose", 5, 7), //
                new Item("Sulfuras, Hand of Ragnaros", 0, 80), //
                new Item("Sulfuras, Hand of Ragnaros", -1, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
                new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
                new Item("Backstage passes to a TAFKAL80ETC concert", 0, 100),
                // this conjured item does not work properly yet
                new Item("Conjured Mana Cake", 3, 6)};

        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("fixme", app.items[0].name);
    }*/

    @Test
    void shouldDecreaseQualityItemsWithoutSpecialRules() {
        Item[] items = new Item[]{
                new Item("+5 Dexterity Vest", 10, 20), //
                new Item("Elixir of the Mongoose", 5, 7), //
                new Item("Conjured Mana Cake", 3, 6)};

        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(19, app.items[0].quality);
        assertEquals(9, app.items[0].sellIn);
        assertEquals(6, app.items[1].quality);
        assertEquals(4, app.items[1].sellIn);
        assertEquals(4, app.items[2].quality);
        assertEquals(2, app.items[2].sellIn);
    }

    @Test
    void shouldDecreaseQualityBackstage() {
        Item[] items = new Item[]{
                new Item("Backstage passes to a TAFKAL80ETC concert", 0, 100)};

        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(0, app.items[0].quality);
        assertEquals(-1, app.items[0].sellIn);
    }

    @Test
    void onceSellinHasPassedDegradesTwiceFaster() {
        Item[] items = new Item[]{
                new Item("Elixir of the Mongoose", 0, 7), //
                new Item("Conjured Mana Cake", 0, 8),
                new Item("Conjured Mana Cake", -1, 3)};

        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(5, app.items[0].quality);
        assertEquals(-1, app.items[0].sellIn);
        assertEquals(4, app.items[1].quality);
        assertEquals(-1, app.items[1].sellIn);
        assertEquals(0, app.items[2].quality);
        assertEquals(-2, app.items[2].sellIn);
    }

    @Test
    void shouldNotUpdateLegendaryItems() {
        Item[] items = new Item[]{
                new Item("Sulfuras, Hand of Ragnaros", 0, 80)};

        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(80, app.items[0].quality);
        assertEquals(-1, app.items[0].sellIn);

    }

    @Test
    void shouldIncreaseItems() {
        Item[] items = new Item[]{
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
                new Item("Backstage passes to a TAFKAL80ETC concert", 8, 20),
                new Item("Backstage passes to a TAFKAL80ETC concert", 4, 30),
                new Item("Backstage passes to a TAFKAL80ETC concert", 6, 49),
                new Item("Aged Brie", 2, 0), //
                new Item("Aged Brie", 2, 50)};

        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(21, app.items[0].quality);
        assertEquals(14, app.items[0].sellIn);
        assertEquals(50, app.items[1].quality);
        assertEquals(9, app.items[1].sellIn);
        assertEquals(22, app.items[2].quality);
        assertEquals(7, app.items[2].sellIn);
        assertEquals(33, app.items[3].quality);
        assertEquals(3, app.items[3].sellIn);
        assertEquals(50, app.items[4].quality);
        assertEquals(5, app.items[4].sellIn);
        assertEquals(1, app.items[5].quality);
        assertEquals(1, app.items[5].sellIn);
        assertEquals(50, app.items[6].quality);
        assertEquals(1, app.items[6].sellIn);
    }
}
