package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public abstract class PhysicsComponent implements Component
{
    private static final String TAG = PhysicsComponent.class.getSimpleName();
    public Rectangle boundingBox;
    protected Vector2 nextEntityPosition;
    protected Vector2 currentEntityPosition;
    protected Entity.Direction currentDirection;
    protected Json json;
    protected Vector2 velocity;
    protected BoundingBoxLocation boundingBoxLocation;
    PhysicsComponent()
    {
        this.nextEntityPosition = new Vector2(0, 0);
        this.currentEntityPosition = new Vector2(0, 0);
        this.velocity = new Vector2(2f, 2f);
        this.boundingBox = new Rectangle();
        this.json = new Json();
        boundingBoxLocation = BoundingBoxLocation.BOTTOM_LEFT;
    }

    public abstract void update(Entity entity, MapManager mapMgr, float delta);

    protected boolean isCollisionWithMapEntities(Entity entity, MapManager mapMgr)
    {
        Array<Entity> entities = mapMgr.getCurrentMapEntities();
        boolean isCollisionWithMapEntities = false;

        for(Entity mapEntity : entities)
        {
            if(mapEntity.equals(entity))
            {
                continue;
            }

            Rectangle targetRect = mapEntity.getCurrentBoundingBox();
            if(boundingBox.overlaps(targetRect))
            {
                entity.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
                isCollisionWithMapEntities = true;
                break;
            }
        }
        return isCollisionWithMapEntities;
    }

    protected boolean isCollision(Entity entitySource, Entity entityTarget)
    {
        boolean isCollisionWithMapEntities = false;
        if(entitySource.equals(entityTarget))
        {
            return false;
        }

        if(entitySource.getCurrentBoundingBox().overlaps(entityTarget.getCurrentBoundingBox()))
        {
            entitySource.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
            isCollisionWithMapEntities = true;
        }

        return isCollisionWithMapEntities;
    }

    protected boolean isCollisionWithMapLayer(Entity entity, MapManager mapMgr)
    {
        MapLayer mapCollisionLayer = mapMgr.getCollisionLayer();

        if(mapCollisionLayer == null) return false;

        Rectangle rectangle = null;

        for(MapObject object : mapCollisionLayer.getObjects())
        {
            if(object instanceof RectangleMapObject)
            {
                rectangle = ((RectangleMapObject) object).getRectangle();
                if(boundingBox.overlaps(rectangle))
                {
                    entity.sendMessage(MESSAGE.COLLISION_WITH_MAP);
                    return true;
                }
            }
        }
        return false;
    }

    protected void setNextPositionToCurrent(Entity entity)
    {
        this.currentEntityPosition.x = nextEntityPosition.x;
        this.currentEntityPosition.y = nextEntityPosition.y;
        entity.sendMessage(MESSAGE.CURRENT_POSITION, json.toJson(currentEntityPosition));
    }

    protected void calculateNextPosition(float delta)
    {
        if(currentDirection == null) return;

        float testX = currentEntityPosition.x;
        float testY = currentEntityPosition.y;

        velocity.scl(delta);

        switch(currentDirection)
        {
            case UP:
                testY += velocity.y;
                break;
            case RIGHT:
                testX += velocity.x;
                break;
            case DOWN:
                testY -= velocity.y;
                break;
            case LEFT:
                testX -= velocity.x;
                break;
        }

        nextEntityPosition.x = testX;
        nextEntityPosition.y = testY;

        velocity.scl(1 / delta);
    }

    protected void initBoundingBox(float percentageWidthReduced, float percentageHeightReduced)
    {
        float width;
        float height;

        float origWidth = Entity.FRAME_WIDTH;
        float origHeight = Entity.FRAME_HEIGHT;

        float widthReductionAmount = 1.0f - percentageWidthReduced;
        float heightReductionAmount = 1.0f - percentageHeightReduced;

        if(widthReductionAmount > 0 && widthReductionAmount < 1)
        {
            width = Entity.FRAME_WIDTH * widthReductionAmount;
        } else
        {
            width = Entity.FRAME_WIDTH;
        }

        if(heightReductionAmount > 0 && heightReductionAmount < 1)
        {
            height = Entity.FRAME_HEIGHT * heightReductionAmount;
        } else
        {
            height = Entity.FRAME_HEIGHT;
        }

        if(width == 0 || height == 0)
        {
            Gdx.app.debug(TAG, String.format("Width or height is 0!! %.2f:%.2f", width, height));
        }

        float minX, minY;

        if(Map.UNIT_SCALE > 0)
        {
            minX = nextEntityPosition.x / Map.UNIT_SCALE;
            minY = nextEntityPosition.y / Map.UNIT_SCALE;
        } else
        {
            minX = nextEntityPosition.x;
            minY = nextEntityPosition.y;
        }

        boundingBox.setWidth(width);
        boundingBox.setHeight(height);

        switch(boundingBoxLocation)
        {
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, width, height);
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(getBottomCenter(minX, minY));
                break;
            case CENTER:
                boundingBox.setCenter(getCenter(minX, minY));
                break;
        }
    }

    private Vector2 getBottomCenter(float x, float y)
    {
        Vector2 bottomCenter = new Vector2();
        bottomCenter.x = x + Entity.FRAME_WIDTH / 2;
        bottomCenter.y = y + Entity.FRAME_HEIGHT / 4;
        return bottomCenter;
    }

    private Vector2 getCenter(float x, float y)
    {
        Vector2 center = new Vector2();
        center.x = x + Entity.FRAME_WIDTH / 2;
        center.y = y + Entity.FRAME_HEIGHT / 2;
        return center;
    }

    protected void updateBoundingBoxPosition(Vector2 position)
    {
        float minX, minY;

        if(Map.UNIT_SCALE > 0)
        {
            minX = position.x / Map.UNIT_SCALE;
            minY = position.y / Map.UNIT_SCALE;
        } else
        {
            minX = position.x;
            minY = position.y;
        }

        switch(boundingBoxLocation)
        {
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, boundingBox.getWidth(), boundingBox.getHeight());
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(getBottomCenter(minX, minY));
                break;
            case CENTER:
                boundingBox.setCenter(getCenter(minX, minY));
                break;
        }
    }

    public enum BoundingBoxLocation
    {
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        CENTER
    }
}
