package com.packtpub.libgdx.bludbourne.ui;


import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.InventoryItem.ItemTypeID;
import com.packtpub.libgdx.bludbourne.InventoryItem.ItemUseType;
import com.packtpub.libgdx.bludbourne.InventoryItemFactory;
import com.packtpub.libgdx.bludbourne.Utility;

/**
 * Created by blindweasel on 1/2/16.
 */
public class InventoryUI extends Window
{
    public static final String INV_HELMET = "inv_helmet";
    public static final String INV_WEAPON = "inv_weapon";
    public static final String INV_SHIELD = "inv_shield";
    public static final String INV_CHEST = "inv_chest";
    public static final String INV_BOOT = "inv_boot";
    public static final String DIALOG_PATCH = "dialog";
    private final int slotWidth = 52;
    private final int slotHeight = 52;
    private int numSlots = 50;
    private int lengthSlotRow = 10;
    private Table inventorySlotTable;
    private Table playerSlotsTable;
    private Table equipSlots;
    private DragAndDrop dragAndDrop;
    private Array<Actor> inventoryActors;
    private InventorySlotTooltip inventorySlotTooltip;

    public InventoryUI()
    {
        super("Inventory", Utility.STATUS_UI_SKIN, "solidbackground");

        dragAndDrop = new DragAndDrop();
        inventoryActors = new Array<>();

        inventorySlotTable = new Table();
        inventorySlotTable.setName("Inventory_Slot_Table");

        playerSlotsTable = new Table();
        equipSlots = new Table();
        equipSlots.setName("Equipment_Slot_Table");

        equipSlots.defaults().space(10);
        inventorySlotTooltip = new InventorySlotTooltip(Utility.STATUS_UI_SKIN);

        InventorySlot headSlot = new InventorySlot(
                ItemUseType.ARMOR_HELMET.getValue(),
                new Image(Utility.ITEMS_TEXTURE_ATLAS.findRegion(INV_HELMET)));

        InventorySlot leftArmSlot = new InventorySlot(
                ItemUseType.WEAPON_ONEHAND.getValue() |
                ItemUseType.WEAPON_TWOHAND.getValue() |
                ItemUseType.ARMOR_SHIELD.getValue() |
                ItemUseType.WAND_ONEHAND.getValue() |
                ItemUseType.WAND_TWOHAND.getValue(),
                new Image(Utility.ITEMS_TEXTURE_ATLAS.findRegion(INV_WEAPON)));

        InventorySlot rightArmSlot = new InventorySlot(
                ItemUseType.WEAPON_ONEHAND.getValue() |
                ItemUseType.WEAPON_TWOHAND.getValue() |
                ItemUseType.ARMOR_SHIELD.getValue() |
                ItemUseType.WAND_ONEHAND.getValue() |
                ItemUseType.WAND_TWOHAND.getValue(),
                new Image(Utility.ITEMS_TEXTURE_ATLAS.findRegion(INV_SHIELD)));

        InventorySlot chestSlot = new InventorySlot(
                ItemUseType.ARMOR_CHEST.getValue(),
                new Image(Utility.ITEMS_TEXTURE_ATLAS.findRegion(INV_CHEST)));

        InventorySlot legsSlot = new InventorySlot(
                ItemUseType.ARMOR_FEET.getValue(),
                new Image(Utility.ITEMS_TEXTURE_ATLAS.findRegion(INV_BOOT)));

        headSlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
        leftArmSlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
        rightArmSlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
        chestSlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
        legsSlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));

        dragAndDrop.addTarget(new InventorySlotTarget(headSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(leftArmSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(rightArmSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(chestSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(legsSlot));

        playerSlotsTable.setBackground(
                new Image(new NinePatch(Utility.STATUS_UI_TEXTURE_ATLAS.createPatch(DIALOG_PATCH))).getDrawable());

        for(int i = 1; i <= numSlots; i++)
        {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            inventorySlotTable.add(inventorySlot).size(slotWidth, slotHeight);

            if(i % lengthSlotRow == 0)
            {
                inventorySlotTable.row();
            }
        }

        equipSlots.add();
        equipSlots.add(headSlot).size(slotWidth, slotHeight);
        equipSlots.row();

        equipSlots.add(leftArmSlot).size(slotWidth, slotHeight);
        equipSlots.add(chestSlot).size(slotWidth, slotHeight);
        equipSlots.add(rightArmSlot).size(slotWidth, slotHeight);
        equipSlots.row();

        equipSlots.add();
        equipSlots.right().add(legsSlot).size(slotWidth, slotHeight);

        playerSlotsTable.add(equipSlots);
        inventoryActors.add(inventorySlotTooltip);

        this.add(playerSlotsTable).padBottom(20).row();
        this.add(inventorySlotTable).row();
        this.pack();
    }

    public Table getInventorySlotTable()
    {
        return inventorySlotTable;
    }

    public Table getEquipSlotsTable()
    {
        return equipSlots;
    }

    public void populateInventory(Table targetTable, Array<InventoryItemLocation> inventoryItems)
    {
        Array<Cell> cells = targetTable.getCells();
        for(int i = 0; i < inventoryItems.size; i++)
        {
            InventoryItemLocation itemLocation = inventoryItems.get(i);
            ItemTypeID itemTypeID = ItemTypeID.valueOf(itemLocation.getItemTypeAtLocation());
            InventorySlot inventorySlot = ((InventorySlot) cells.get(itemLocation.getLocationIndex()).getActor());
            inventorySlot.clearAllInventoryItems();

            for(int index = 0; index < itemLocation.getNumberItemsAtLocation(); index++)
            {
                inventorySlot.add(InventoryItemFactory.getInstance().getInventoryItem(itemTypeID));
                dragAndDrop.addSource(new InventorySlotSource(inventorySlot, dragAndDrop));
            }
        }
    }

    public Array<InventoryItemLocation> getInventory(Table targetTable)
    {
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<>();
        for(int i = 0; i < cells.size; i++)
        {
            InventorySlot inventorySlot = ((InventorySlot) cells.get(i).getActor());
            if(inventorySlot == null) continue;
            int numItems = inventorySlot.getNumItems();
            if(numItems > 0)
            {
                items.add(new InventoryItemLocation(i, inventorySlot.getTopInventoryItem().getItemTypeID().toString(),
                                                    numItems));
            }
        }
        return items;
    }

    public Array<Actor> getInventoryActors()
    {
        return inventoryActors;
    }
}
