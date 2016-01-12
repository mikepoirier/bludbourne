package com.packtpub.libgdx.bludbourne.dialog;

import com.packtpub.libgdx.bludbourne.dialog.ConversationGraphObserver.ConversationCommandEvent;

/**
 * Created by michael.poirier on 1/12/2016.
 */
public class ConversationChoice {
    private String sourceId;
    private String destinationId;
    private String choicePhrase;
    private ConversationCommandEvent conversationCommandEvent;

    public ConversationChoice() {
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getChoicePhrase() {
        return choicePhrase;
    }

    public void setChoicePhrase(String choicePhrase) {
        this.choicePhrase = choicePhrase;
    }

    public ConversationCommandEvent getConversationCommandEvent() {
        return conversationCommandEvent;
    }

    public void setConversationCommandEvent(
            ConversationCommandEvent conversationCommandEvent) {
        this.conversationCommandEvent = conversationCommandEvent;
    }

    public String toString() {
        return choicePhrase;
    }
}
