package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

/**
 * Created by blindweasel on 12/1/15.
 */
public class Entity
{
	private static final String TAG = Entity.class.getSimpleName();
    private Json json;
    private EntityConfig entityConfig;

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        static public Direction getRandomNext() {
            return Direction.values()[
                    MathUtils.random(Direction.values().length - 1)];
        }

        public Direction getOpposite() {
            if(this == LEFT) return RIGHT;
            else if(this == RIGHT) return LEFT;
            else if(this == UP) return DOWN;
            else return UP;
        }
    }

    public enum State {
        IDLE,
        WALKING,
        IMMOBILE;

        static public State getRandomNext() {
            // Ignore IMMOBILE which should be last state
            return State.values()[MathUtils.random(State.values().length - 2)];
        }
    }

    public enum AnimationType {
        WALK_LEFT,
        WALK_RIGHT,
        WALK_UP,
        WALK_DOWN,
        IDLE,
        IMMOBILE
    }

    public static final int FRAME_WIDTH = 16;
    public static final int FRAME_HEIGHT = 16;

    private static final int MAX_COMPONENTS = 5;
    private Array<Component> components;

    private InputComponent inputComponent;
    private GraphicsComponent graphicsComponent;
    private PhysicsComponent physicsComponent;

    public Entity(InputComponent inputComponent,
                  PhysicsComponent physicsComponent,
                  GraphicsComponent graphicsComponent) {
        entityConfig = new EntityConfig();
        json = new Json();

        components = new Array<Component>(MAX_COMPONENTS);

        this.inputComponent = inputComponent;
        this.physicsComponent = physicsComponent;
        this.graphicsComponent = graphicsComponent;

        components.add(inputComponent);
        components.add(physicsComponent);
        components.add(graphicsComponent);
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void sendMessage(Component.MESSAGE messageType, String... args) {
        String fullMessage = messageType.toString();

        for(String string : args) {
            fullMessage += Component.MESSAGE_TOKEN + string;
        }

        for(Component component : components) {
            component.receiveMessage(fullMessage);
        }
    }

    public void update(MapManager mapMgr, Batch batch, float delta) {
        inputComponent.update(this, delta);
        physicsComponent.update(this, mapMgr, delta);
        graphicsComponent.update(this, mapMgr, batch, delta);
    }

    public void updateInput(float delta)
    {
        inputComponent.update(this, delta);
    }

    public void dispose() {
        for(Component component : components) {
            component.dispose();
        }
    }

    public Rectangle getCurrentBoundingBox() {
        return physicsComponent.boundingBox;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    public static EntityConfig getEntityConfig(String configFilePath) {
        Json json = new Json();
        return json.fromJson(EntityConfig.class,
                             Gdx.files.internal(configFilePath));
    }

    public static Array<EntityConfig> getEntityConfigs(String configFilePath) {
        Json json = new Json();
        Array<EntityConfig> configs = new Array<EntityConfig>();
        ArrayList list = json.fromJson(ArrayList.class,
                                       Gdx.files.internal(configFilePath));

        list.stream().forEach(jsonValue -> configs
                .add(json.readValue(EntityConfig.class,
                                    (JsonValue) jsonValue)));


        return configs;
    }

    public InputProcessor getInputProcessor()
    {
        return inputComponent;
    }
}
