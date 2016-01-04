package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by blindweasel on 1/2/16.
 */
public class InventoryItem extends Image
{
    public enum ItemAttribute
    {
        CONSUMABLE(1),
        EQUIPPABLE(2),
        STACKABLE(4),;

        private int attribute;

        ItemAttribute(int attribute)
        {
            this.attribute = attribute;
        }

        public int getValue()
        {
            return attribute;
        }
    }

    public enum ItemUseType
    {
        ITEM_RESTORE_HEALTH(1),
        ITEM_RESTORE_MP(2),
        ITEM_DAMAGE(4),
        WEAPON_ONEHAND(8),
        WEAPON_TWOHAND(16),
        WAND_ONEHAND(32),
        WAND_TWOHAND(64),
        ARMOR_SHIELD(128),
        ARMOR_HELMET(256),
        ARMOR_CHEST(512),
        ARMOR_FEET(1024);

        private int value;

        ItemUseType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    public enum ItemTypeID
    {
        ARMOR01,
        ARMOR02,
        ARMOR03,
        ARMOR04,
        ARMOR05,
        BOOTS01,
        BOOTS02,
        BOOTS03,
        BOOTS04,
        BOOTS05,
        HELMET01,
        HELMET02,
        HELMET03,
        HELMET04,
        HELMET05,
        SHIELD01,
        SHIELD02,
        SHIELD03,
        SHIELD04,
        SHIELD05,
        WANDS01,
        WANDS02,
        WANDS03,
        WANDS04,
        WANDS05,
        WEAPON01,
        WEAPON02,
        WEAPON03,
        WEAPON04,
        WEAPON05,
        POTIONS01,
        POTIONS02,
        POTIONS03,
        SCROLL01,
        SCROLL02,
        SCROLL03
    }

    private int itemAttributes;
    private int itemUseType;
    private ItemTypeID itemTypeID;
    private String itemShortDescription;

    public InventoryItem(TextureRegion textureRegion, int itemAttributes, ItemTypeID itemTypeID, int itemUseType)
    {
        super(textureRegion);

        this.itemTypeID = itemTypeID;
        this.itemAttributes = itemAttributes;
        this.itemUseType = itemUseType;
    }

    public InventoryItem()
    {
        super();
    }

    public InventoryItem(InventoryItem inventoryItem)
    {
        super();
        this.itemTypeID = inventoryItem.getItemTypeID();
        this.itemAttributes = inventoryItem.getItemAttributes();
        this.itemUseType = inventoryItem.getItemUseType();
        this.itemShortDescription = inventoryItem.getItemShortDescription();
    }

    public int getItemAttributes()
    {
        return itemAttributes;
    }

    public void setItemAttributes(int itemAttributes)
    {
        this.itemAttributes = itemAttributes;
    }

    public int getItemUseType()
    {
        return itemUseType;
    }

    public void setItemUseType(int itemUseType)
    {
        this.itemUseType = itemUseType;
    }

    public ItemTypeID getItemTypeID()
    {
        return itemTypeID;
    }

    public void setItemTypeID(ItemTypeID itemTypeID)
    {
        this.itemTypeID = itemTypeID;
    }

    public String getItemShortDescription()
    {
        return itemShortDescription;
    }

    public void setItemShortDescription(String itemShortDescription)
    {
        this.itemShortDescription = itemShortDescription;
    }

    public boolean isStackable()
    {
        return((itemAttributes & ItemAttribute.STACKABLE.getValue()) == ItemAttribute.STACKABLE.getValue());
    }

    public boolean isSameItemType(InventoryItem candidateInventoryItem)
    {
        return itemTypeID == candidateInventoryItem.getItemTypeID();
    }
}
