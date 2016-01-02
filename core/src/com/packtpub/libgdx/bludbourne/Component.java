package com.packtpub.libgdx.bludbourne;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public interface Component
{

    String MESSAGE_TOKEN = ":::::";

    void dispose();
    void receiveMessage(String message);

    enum MESSAGE
    {
        CURRENT_POSITION,
        INIT_START_POSITION,
        CURRENT_DIRECTION,
        CURRENT_STATE,
        COLLISION_WITH_MAP,
        COLLISION_WITH_ENTITY,
        LOAD_ANIMATIONS,
        INIT_DIRECTION,
        INIT_STATE,
        INIT_SELECTED_ENTITY,
        ENTITY_SELECTED,
        ENTITY_DESELECTED
    }
}
