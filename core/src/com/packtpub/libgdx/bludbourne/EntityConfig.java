package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.Entity.Direction;
import com.packtpub.libgdx.bludbourne.Entity.State;
import com.packtpub.libgdx.bludbourne.InventoryItem.ItemTypeID;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public class EntityConfig
{
    private Array<AnimationConfig> animationConfig;
    private Array<ItemTypeID> inventory;
    private State state;
    private Direction direction;
    private String entityID;

    EntityConfig()
    {
        animationConfig = new Array<>();
    }

    public Array<AnimationConfig> getAnimationConfig()
    {
        return animationConfig;
    }

    public void addAnimationConfig(AnimationConfig animationConfig)
    {
        this.animationConfig.add(animationConfig);
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(
            Direction direction)
    {
        this.direction = direction;
    }

    public String getEntityID()
    {
        return entityID;
    }

    public void setEntityID(String entityID)
    {
        this.entityID = entityID;
    }

    public Array<ItemTypeID> getInventory()
    {
        return inventory;
    }

    public void setInventory(
            Array<ItemTypeID> inventory)
    {
        this.inventory = inventory;
    }

    public static class AnimationConfig
    {
        private float frameDuration = 1.0f;
        private Entity.AnimationType animationType;
        private Array<String> texturePaths;
        private Array<GridPoint2> gridPoints;

        public AnimationConfig()
        {
            animationType = Entity.AnimationType.IDLE;
            texturePaths = new Array<>();
            gridPoints = new Array<>();
        }

        public float getFrameDuration()
        {
            return frameDuration;
        }

        public void setFrameDuration(float frameDuration)
        {
            this.frameDuration = frameDuration;
        }

        public Entity.AnimationType getAnimationType()
        {
            return animationType;
        }

        public void setAnimationType(Entity.AnimationType animationType)
        {
            this.animationType = animationType;
        }

        public Array<String> getTexturePaths()
        {
            return texturePaths;
        }

        public void setTexturePaths(Array<String> texturePaths)
        {
            this.texturePaths = texturePaths;
        }

        public Array<GridPoint2> getGridPoints()
        {
            return gridPoints;
        }

        public void setGridPoints(Array<GridPoint2> gridPoints)
        {
            this.gridPoints = gridPoints;
        }
    }
}
