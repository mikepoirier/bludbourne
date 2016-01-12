package com.packtpub.libgdx.bludbourne.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by michael.poirier on 1/12/2016.
 */
public class ConversationGraph extends ConversationGraphSubject {
    private static final String TAG = ConversationGraph.class.getSimpleName();
    private Hashtable<String, Conversation> conversations;
    private Hashtable<String, ArrayList<ConversationChoice>> associatedChoices;
    private String currentConversationID = null;

    public ConversationGraph() {
    }

    public ConversationGraph(Hashtable<String, Conversation> conversations, String rootID)
    {
        setConversations(conversations);
        setCurrentConversation(rootID);
    }

    private void setConversations(
            Hashtable<String, Conversation> conversations) {
        if(conversations.size() < 0) {
            throw new IllegalArgumentException("Can't have a negative amount of conversations");
        }

        this.conversations = conversations;
        this.associatedChoices = new Hashtable<>(conversations.size());
        for(Conversation conversation : conversations.values())
        {
            associatedChoices.put(conversation.getId(), new ArrayList<>());
        }
        this.conversations = conversations;
    }

    public ArrayList<ConversationChoice> getCurrentChoices() {
        return associatedChoices.get(currentConversationID);
    }

    public String getCurrentConversationID() {
        return this.currentConversationID;
    }

    private void setCurrentConversation(String id) {
        Conversation conversation = getConversationByID(id);

        if(currentConversationID == null ||
                currentConversationID.equalsIgnoreCase(id) ||
                isReachable(currentConversationID, id))
        {
            currentConversationID = id;
        } else {
            System.out.println(String.format(
                    "New conversation node[%s] is not reachable from current node [%s]",
                    id, currentConversationID));
        }
    }

    public boolean isValid(String conversationID) {
        Conversation conversation = conversations.get(conversationID);
        if(conversation == null) return false;
        return true;
    }

    public boolean isReachable(String sourceID, String sinkID) {
        if(!isValid(sourceID) || !isValid(sinkID)) return false;
        if(conversations.get(sourceID) == null) return false;

        ArrayList<ConversationChoice> list = associatedChoices.get(sourceID);
        if(list == null) return false;
        for(ConversationChoice choice : list) {
            if(choice.getSourceId().equalsIgnoreCase(sourceID) &&
                    choice.getDestinationId().equalsIgnoreCase(sinkID)) {
                return true;
            }
        }
        return false;
    }

    public Conversation getConversationByID(String id) {
        if(!isValid(id))
        {
            Gdx.app.debug(TAG, String.format("ID %s is not valid!", id));
            return null;
        }

        return conversations.get(id);
    }

    public String displayCurrentConversation() {
        return conversations.get(currentConversationID).getDialog();
    }

    public void addChoice(ConversationChoice conversationChoice) {
        ArrayList<ConversationChoice> list =
                associatedChoices.get(conversationChoice.getSourceId());
        if(list == null) return;

        list.add(conversationChoice);
    }

    public String toString() {
        StringBuilder outputString = new StringBuilder();
        int numberTotalChoices = 0;

        Set<String> keys = associatedChoices.keySet();
        for(String id : keys) {
            outputString.append(String.format("[%s]: ", id));

            for(ConversationChoice choice : associatedChoices.get(id)) {
                numberTotalChoices++;
                outputString.append(String.format("%s ", choice.getDestinationId()));
            }

            outputString.append(System.getProperty("line.separator"));
        }

        outputString.append(String.format("Number conversations: %d",
                                          conversations.size()))
                    .append(String.format(", Number of choices: %d",
                                          numberTotalChoices))
                    .append(System.getProperty("line.separator"));

        return outputString.toString();
    }

    public String toJson() {
        Json json = new Json();
        return json.prettyPrint(this);
    }
}
