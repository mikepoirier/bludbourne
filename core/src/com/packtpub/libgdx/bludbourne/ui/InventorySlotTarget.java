package com.packtpub.libgdx.bludbourne.ui;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.packtpub.libgdx.bludbourne.InventoryItem;

/**
 * Created by blindweasel on 1/3/16.
 */
public class InventorySlotTarget extends Target
{

    InventorySlot targetSlot;

    public InventorySlotTarget(InventorySlot actor)
    {
        super(actor);
        targetSlot = actor;
    }

    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer)
    {
        return true;
    }

    @Override
    public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload)
    {
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer)
    {
        InventoryItem sourceActor = (InventoryItem) payload.getDragActor();
        InventoryItem targetActor = targetSlot.getTopInventoryItem();
        InventorySlot sourceSlot = ((InventorySlotSource) source).getSourceSlot();

        if(sourceActor == null)
        {
            return;
        }

        if(!targetSlot.doesAcceptItemUseType(sourceActor.getItemUseType()))
        {
            sourceSlot.add(sourceActor);
            return;
        }

        addItem(sourceActor, targetActor, sourceSlot);
    }

    private void addItem(InventoryItem sourceActor, InventoryItem targetActor, InventorySlot sourceSlot)
    {
        if(targetSlotEmpty())
        {
            targetSlot.add(sourceActor);
        } else
        {
            addToNonEmptySlot(sourceActor, targetActor, sourceSlot);
        }
    }

    private void addToNonEmptySlot(InventoryItem sourceActor, InventoryItem targetActor, InventorySlot sourceSlot)
    {
        if(canStack(sourceActor, targetActor))
        {
            targetSlot.add(sourceActor);
        } else
        {
            InventorySlot.swapSlots(sourceSlot, targetSlot, sourceActor);
        }
    }

    private boolean targetSlotEmpty()
    {
        return !targetSlot.hasItem();
    }

    private boolean canStack(InventoryItem sourceActor, InventoryItem targetActor)
    {
        return sourceActor.isSameItemType(targetActor) && sourceActor.isStackable();
    }
}
