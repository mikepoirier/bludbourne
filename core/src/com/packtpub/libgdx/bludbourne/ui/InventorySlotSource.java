package com.packtpub.libgdx.bludbourne.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;

/**
 * Created by blindweasel on 1/3/16.
 */
public class InventorySlotSource extends Source
{

    private DragAndDrop dragAndDrop;
    private InventorySlot sourceSlot;

    public InventorySlotSource(InventorySlot sourceSlot, DragAndDrop dragAndDrop)
    {
        super(sourceSlot.getTopInventoryItem());
        this.sourceSlot = sourceSlot;
        this.dragAndDrop = dragAndDrop;
    }

    @Override
    public Payload dragStart(InputEvent event, float x, float y, int pointer)
    {
        Payload payload = new Payload();

        sourceSlot = (InventorySlot) getActor().getParent();
        sourceSlot.decrementItemCount();

        payload.setDragActor(getActor());
        dragAndDrop.setDragActorPosition(-x, -y + getActor().getHeight());

        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload,
                         DragAndDrop.Target target)
    {
        if(target == null)
        {
            sourceSlot.add(payload.getDragActor());
        }
    }

    public InventorySlot getSourceSlot()
    {
        return sourceSlot;
    }
}
