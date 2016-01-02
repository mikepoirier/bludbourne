package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.EntityConfig.AnimationConfig;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public class PlayerGraphicsComponent extends GraphicsComponent {
    private static final String TAG = PlayerGraphicsComponent.class.getSimpleName();

    public PlayerGraphicsComponent()
    {

    }

    @Override
    public void update(Entity entity, MapManager mapManager, Batch batch, float delta)
    {
        updateAnimations(delta);

        Camera camera = mapManager.getCamera();
        camera.position.set(currentPosition.x, currentPosition.y, 0.0f);
        camera.update();

        batch.begin();
        batch.draw(currentFrame, currentPosition.x, currentPosition.y, 1, 1);
        batch.end();
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void receiveMessage(String message)
    {
        String[] string = message.split(MESSAGE_TOKEN);

        if(string.length == 0) return;

        if(string.length == 2)
        {
            if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_POSITION.toString()))
            {
                currentPosition = json.fromJson(Vector2.class, string[1]);
            } else if(string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString()))
            {
                currentPosition = json.fromJson(Vector2.class, string[1]);
            } else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString()))
            {
                currentState = json.fromJson(Entity.State.class, string[1]);
            } else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString()))
            {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            } else if(string[0].equalsIgnoreCase(MESSAGE.LOAD_ANIMATIONS.toString()))
            {
                EntityConfig entityConfig = json.fromJson(EntityConfig.class, string[1]);
                Array<AnimationConfig> animationConfigs = entityConfig.getAnimationConfig();

                for(AnimationConfig animationConfig : animationConfigs)
                {
                    Array<String> textureNames = animationConfig.getTexturePaths();
                    Array<GridPoint2> points = animationConfig.getGridPoints();
                    Entity.AnimationType animationType = animationConfig.getAnimationType();
                    float frameDuration = animationConfig.getFrameDuration();
                    Animation animation = null;

                    if(textureNames.size == 1)
                    {
                        animation = loadAnimation(textureNames.get(0), points, frameDuration);
                    } else if(textureNames.size == 2)
                    {
                        animation = loadAnimation(textureNames.get(0), textureNames.get(1), points, frameDuration);
                    }

                    animations.put(animationType, animation);
                }
            }
        }
    }
}
