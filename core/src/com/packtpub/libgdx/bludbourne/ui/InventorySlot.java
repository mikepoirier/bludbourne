package com.packtpub.libgdx.bludbourne.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.packtpub.libgdx.bludbourne.InventoryItem;
import com.packtpub.libgdx.bludbourne.Utility;

/**
 * Created by blindweasel on 1/2/16.
 */
public class InventorySlot extends Stack
{
    private Stack defaultBackground;
    private Image customBackgroundDecal;
    private Label numItemsLabel;
    private int numItemsVal = 0;
    private int filterItemType;

    public InventorySlot()
    {
        filterItemType = 0;
        defaultBackground = new Stack();
        customBackgroundDecal = new Image();
        Image image = new Image(new NinePatch(Utility.STATUS_UI_TEXTURE_ATLAS.createPatch("dialog")));

        numItemsLabel = new Label(String.valueOf(numItemsVal), Utility.STATUS_UI_SKIN, "inventory-item-count");
        numItemsLabel.setAlignment(Align.bottomRight);
        numItemsLabel.setVisible(false);

        defaultBackground.add(image);

        add(defaultBackground);
        add(numItemsLabel);
    }

    public InventorySlot(int filterItemType, Image customBackgroundDecal)
    {
        this();
        this.filterItemType = filterItemType;
        this.customBackgroundDecal = customBackgroundDecal;
        defaultBackground.add(customBackgroundDecal);
    }

    public void decrementItemCount()
    {
        numItemsVal--;
        numItemsLabel.setText(String.valueOf(numItemsVal));
        if(defaultBackground.getChildren().size == 1)
        {
            defaultBackground.add(customBackgroundDecal);
        }
        checkVisibilityOfItemCount();
    }

    public void incrementItemCount()
    {
        numItemsVal++;
        numItemsLabel.setText(String.valueOf(numItemsVal));
        if(defaultBackground.getChildren().size > 1)
        {
            defaultBackground.getChildren().pop();
        }
        checkVisibilityOfItemCount();
    }

    @Override
    public void add(Actor actor)
    {
        super.add(actor);

        if(numItemsLabel == null)
        {
            return;
        }

        if(!actor.equals(defaultBackground) && !actor.equals(numItemsLabel))
        {
            incrementItemCount();
        }
    }

    public void add(Array<Actor> array)
    {
        for(Actor actor : array)
        {
            super.add(actor);
            if(numItemsLabel == null)
            {
                return;
            }

            if(!actor.equals(defaultBackground) && !actor.equals(numItemsLabel))
            {
                incrementItemCount();
            }
        }
    }

    public Array<Actor> getAllInventoryItems()
    {
        Array<Actor> items = new Array<>();
        if(hasItem())
        {
            SnapshotArray<Actor> arrayChildren = getChildren();
            int numInventoryItems = arrayChildren.size = 2;
            for(int i = 0; i < numInventoryItems; i++)
            {
                items.add(arrayChildren.pop());
                decrementItemCount();
            }
        }
        return items;
    }

    public void clearAllInventoryItems()
    {
        if(hasItem())
        {
            SnapshotArray<Actor> arrayChildren = getChildren();
            int numInventoryItems = getNumItems();
            for(int i = 0; i < numInventoryItems; i++)
            {
                arrayChildren.pop();
                decrementItemCount();
            }
        }
    }

    private void checkVisibilityOfItemCount()
    {
        if(numItemsVal < 2)
        {
            numItemsLabel.setVisible(false);
        } else
        {
            numItemsLabel.setVisible(true);
        }
    }

    public boolean hasItem()
    {
        if(hasChildren())
        {
            SnapshotArray<Actor> items = getChildren();
            if(items.size > 2)
            {
                return true;
            }
        }
        return false;
    }

    public int getNumItems()
    {
        if(hasChildren())
        {
            SnapshotArray<Actor> items = getChildren();
            return items.size - 2;
        }

        return 0;
    }

    public boolean doesAcceptItemUseType(int itemUseType)
    {
        return filterItemType == 0 || ((filterItemType & itemUseType) == itemUseType);
    }

    public InventoryItem getTopInventoryItem()
    {
        InventoryItem actor = null;
        if(hasChildren())
        {
            SnapshotArray<Actor> items = this.getChildren();
            if(items.size > 2)
            {
                actor = (InventoryItem) items.peek();
            }
        }
        return actor;
    }

    public static void swapSlots(InventorySlot inventorySlotSource, InventorySlot inventorySlotTarget, InventoryItem dragActor)
    {
        if(inventorySlotTarget.doesAcceptItemUseType(dragActor.getItemUseType()) ||
                inventorySlotSource.doesAcceptItemUseType(inventorySlotTarget.getTopInventoryItem().getItemUseType()))
        {
            inventorySlotSource.add(dragActor);
            return;
        }

        Array<Actor> tempArray = inventorySlotSource.getAllInventoryItems();
        tempArray.add(dragActor);
        inventorySlotSource.add(inventorySlotTarget.getAllInventoryItems());
        inventorySlotTarget.add(tempArray);
    }
}
