package com.packtpub.libgdx.bludbourne.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created by blindweasel on 1/2/16.
 */
public class InventorySlotTooltip extends Window
{
    private Skin skin;
    private Label description;

    public InventorySlotTooltip(final Skin skin)
    {
        super("", skin);
        this.skin = skin;

        description = new Label("", skin, "inventory-item-count");

        add(description);
        padLeft(5).padRight(5);
        pack();
        setVisible(false);
    }

    public void setVisible(InventorySlot inventorySlot, boolean visible)
    {
        super.setVisible(visible);

        if(inventorySlot == null)
        {
            return;
        }

        if(!inventorySlot.hasItem())
        {
            super.setVisible(false);
        }
    }

    public void updateDescription(InventorySlot inventorySlot)
    {
        if(inventorySlot.hasItem())
        {
            description.setText(inventorySlot.getTopInventoryItem().getItemShortDescription());
            pack();
        } else
        {
            description.setText("");
            pack();
        }
    }
}
