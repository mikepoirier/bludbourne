package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.Entity.State;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public class EntityConfig {
    private Array<AnimationConfig> animationConfig;
    private State state;
    private Entity.Direction direction;
    private String entityID;

    public AnimationConfig[] getAnimationConfig() {
        return animationConfig;
    }

    public void addAnimationConfig(AnimationConfig animationConfig) {
        this.animationConfig.add(animationConfig);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Entity.Direction getDirection() {
        return direction;
    }

    public void setDirection(
            Entity.Direction direction) {
        this.direction = direction;
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }
}
