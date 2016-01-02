package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.utils.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public abstract class InputComponent implements Component
{
    protected static Map<Keys, Boolean> keys = new HashMap<>();
    protected static Map<Mouse, Boolean> mouseButtons = new HashMap<>();

    static
    {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.QUIT, false);
    }

    static
    {
        mouseButtons.put(Mouse.SELECT, false);
        mouseButtons.put(Mouse.DO_ACTION, false);
    }

    protected Entity.Direction currentDirection;
    protected Entity.State currentState;
    protected Json json;

    InputComponent()
    {
        json = new Json();
    }

    public abstract void update(Entity entity, float delta);

    protected enum Keys
    {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        QUIT
    }

    protected enum Mouse
    {
        SELECT,
        DO_ACTION
    }
}
