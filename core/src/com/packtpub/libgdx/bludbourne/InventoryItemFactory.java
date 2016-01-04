package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by blindweasel on 1/3/16.
 */
public class InventoryItemFactory
{
    private Json json = new Json();
    private final String INVENTORY_ITEM = "scripts/inventory_items.json";
    private static InventoryItemFactory instance = null;
    private Hashtable<InventoryItem.ItemTypeID, InventoryItem> inventoryItemList;

    public static InventoryItemFactory getInstance()
    {
        if(instance == null) instance = new InventoryItemFactory();

        return instance;
    }

    private InventoryItemFactory()
    {
        List<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal(INVENTORY_ITEM));
        inventoryItemList = new Hashtable<>();

        for(JsonValue value : list)
        {
            InventoryItem inventoryItem = json.readValue(InventoryItem.class, value);
            inventoryItemList.put(inventoryItem.getItemTypeID(), inventoryItem);
        }
    }

    public InventoryItem getInventoryItem(InventoryItem.ItemTypeID inventoryItemType)
    {
        InventoryItem item = new InventoryItem(inventoryItemList.get(inventoryItemType));
        item.setDrawable(
                new TextureRegionDrawable(Utility.ITEMS_TEXTURE_ATLAS.findRegion(item.getItemTypeID().toString())));
        item.setScaling(Scaling.none);
        return item;
    }
}
