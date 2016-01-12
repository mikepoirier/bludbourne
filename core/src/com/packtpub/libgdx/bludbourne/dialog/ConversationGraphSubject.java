package com.packtpub.libgdx.bludbourne.dialog;

import com.badlogic.gdx.utils.Array;

/**
 * Created by michael.poirier on 1/12/2016.
 */
public class ConversationGraphSubject {
    private Array<ConversationGraphObserver> observers;

    public ConversationGraphSubject() {
        observers = new Array<>();
    }

    public void addObserver(ConversationGraphObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ConversationGraphObserver observer) {
        observers.removeValue(observer, true);
    }

    public void removeAllObservers() {
        for(ConversationGraphObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }
}
