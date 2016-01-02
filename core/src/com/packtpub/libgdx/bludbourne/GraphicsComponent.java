package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.Hashtable;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public abstract class GraphicsComponent implements Component
{
    protected TextureRegion currentFrame = null;
    protected float frameTime = 0f;
    protected Entity.State currentState;
    protected Entity.Direction currentDirection;
    protected Json json;
    protected Vector2 currentPosition;
    protected Hashtable<Entity.AnimationType, Animation> animations;
    protected ShapeRenderer shapeRenderer;

    protected GraphicsComponent()
    {
        currentPosition = new Vector2(0, 0);
        currentState = Entity.State.WALKING;
        currentDirection = Entity.Direction.DOWN;
        json = new Json();
        animations = new Hashtable<>();
        shapeRenderer = new ShapeRenderer();
    }

    public abstract void update(Entity entity, MapManager mapManager, Batch batch, float delta);

    protected void updateAnimations(float delta)
    {
        safelyUpdateFrameTime(delta);
        setAnimationBasedOnDirection();
    }

    private void safelyUpdateFrameTime(float delta)
    {
        frameTime = (frameTime + delta) % 5;
    }

    private void setAnimationBasedOnDirection()
    {
        switch(currentState)
        {
            case WALKING:
                setToWalkingAnimation();
                break;
            case IMMOBILE:
                setToImmobileAnimation();
                break;
            case IDLE:
            default:
                setToIdleAnimation();
                break;
        }
    }

    private void setToWalkingAnimation()
    {
        Animation animation = animations.get(currentAnimationType());
        if(animation == null) return;
        currentFrame = animation.getKeyFrame(frameTime);
    }

    private void setToImmobileAnimation()
    {
        Animation animation = animations.get(Entity.AnimationType.IMMOBILE);
        if(animation == null) return;
        currentFrame = animation.getKeyFrame(frameTime);
    }

    private void setToIdleAnimation()
    {
        Animation animation = animations.get(currentAnimationType());
        if(animation == null) return;
        currentFrame = animation.getKeyFrames()[0];
    }

    private Entity.AnimationType currentAnimationType()
    {
        switch(currentDirection)
        {
            case UP:
                return Entity.AnimationType.WALK_UP;
            case RIGHT:
                return Entity.AnimationType.WALK_RIGHT;
            case LEFT:
                return Entity.AnimationType.WALK_LEFT;
            case DOWN:
            default:
                return Entity.AnimationType.WALK_DOWN;
        }
    }

    protected Animation loadAnimation(String firstTexture, String secondTexture, Array<GridPoint2> points,
                                      float frameDuration)
    {
        Utility.loadTextureAsset(firstTexture);
        Texture texture1 = Utility.getTextureAsset(firstTexture);

        Utility.loadTextureAsset(secondTexture);
        Texture texture2 = Utility.getTextureAsset(secondTexture);

        TextureRegion[][] texture1Frames = TextureRegion.split(texture1, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);
        TextureRegion[][] texture2Frames = TextureRegion.split(texture2, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        Array<TextureRegion> animationKeyFrames = new Array<>(2);

        GridPoint2 point = points.first();

        animationKeyFrames.add(texture1Frames[point.x][point.y]);
        animationKeyFrames.add(texture2Frames[point.x][point.y]);

        return new Animation(frameDuration, animationKeyFrames, Animation.PlayMode.LOOP);
    }

    protected Animation loadAnimation(String textureName, Array<GridPoint2> points, float frameDuration)
    {
        Utility.loadTextureAsset(textureName);
        Texture texture = Utility.getTextureAsset(textureName);

        TextureRegion[][] textureFrames = TextureRegion.split(texture, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        Array<TextureRegion> animationKeyFrames = new Array<>(points.size);

        for(GridPoint2 point : points)
        {
            animationKeyFrames.add(textureFrames[point.x][point.y]);
        }

        return new Animation(frameDuration, animationKeyFrames, Animation.PlayMode.LOOP);
    }
}
