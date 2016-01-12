package com.packtpub.libgdx.bludbourne.dialog;

/**
 * Created by michael.poirier on 1/12/2016.
 */
public interface ConversationGraphObserver {
    enum ConversationCommandEvent {
        LOAD_STORE_INVENTORY,
        EXIT_CONVERSATION,
        NONE
    }

    void onNotify(final ConversationGraph graph, ConversationCommandEvent event);
}
