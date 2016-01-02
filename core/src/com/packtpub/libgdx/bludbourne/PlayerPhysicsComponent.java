package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import javafx.scene.control.cell.MapValueFactory;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public class PlayerPhysicsComponent extends PhysicsComponent
{
    private static final String TAG = PlayerPhysicsComponent.class.getSimpleName();

    private Entity.State state;
    private Vector3 mouseSelectCoordinates;
    private boolean isMouseSelectEnabled = false;
    private Ray selectionRay;
    private float selectRayMaximumDistance = 32.0f;

    public PlayerPhysicsComponent()
    {
        boundingBoxLocation = BoundingBoxLocation.CENTER;
        initBoundingBox(0.3f, 0.5f);

        mouseSelectCoordinates = new Vector3(0, 0, 0);
        selectionRay = new Ray(new Vector3(), new Vector3());
    }

    @Override
    public void update(Entity entity, MapManager mapMgr, float delta)
    {
        updateBoundingBoxPosition(nextEntityPosition);
        updatePortalLayerActivation(mapMgr);

        if(isMouseSelectEnabled)
        {
            selectMapEntityCandidate(mapMgr);
            isMouseSelectEnabled = false;
        }

        if(isWalkingAndNoCollisions(entity, mapMgr))
        {
            setNextPositionToCurrent(entity);

            Camera camera = mapMgr.getCamera();
            camera.position.set(currentEntityPosition, 0f);
            camera.update();
        } else
        {
            updateBoundingBoxPosition(currentEntityPosition);
        }

        calculateNextPosition(delta);
    }

    private void selectMapEntityCandidate(MapManager mapMgr)
    {
        Array<Entity> currentEntities = mapMgr.getCurrentMapEntities();

        mapMgr.getCamera().unproject(mouseSelectCoordinates);
        mouseSelectCoordinates.x /= Map.UNIT_SCALE;
        mouseSelectCoordinates.y /= Map.UNIT_SCALE;

        for(Entity mapEntity : currentEntities)
        {
            mapEntity.sendMessage(MESSAGE.ENTITY_DESELECTED);
            Rectangle mapEntityBoundingBox = mapEntity.getCurrentBoundingBox();
            checkSelection(mapEntity, mapEntityBoundingBox);
        }
    }

    private boolean updatePortalLayerActivation(MapManager mapMgr)
    {
        MapLayer mapPortalLayer = mapMgr.getPortalLayer();

        if(mapPortalLayer == null)
        {
            Gdx.app.debug(TAG, "Portal Layer doesn't exist!");
            return false;
        }

        Rectangle rectangle = null;

        for(MapObject object : mapPortalLayer.getObjects())
        {
            if(object instanceof RectangleMapObject)
            {
                rectangle = ((RectangleMapObject)object).getRectangle();

                if(boundingBox.overlaps(rectangle))
                {
                    String mapName = object.getName();
                    if(mapName == null)
                    {
                        return false;
                    }

                    mapMgr.setClosestStartPositionFromScaledUnits(currentEntityPosition);
                    mapMgr.loadMap(MapFactory.MapType.valueOf(mapName));

                    currentEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    currentEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;
                    nextEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    nextEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;

                    Gdx.app.debug(TAG, "Portal Activated");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isWalkingAndNoCollisions(Entity entity, MapManager mapMgr)
    {
        return !isCollisionWithMapLayer(entity, mapMgr) &&
               !isCollisionWithMapEntities(entity, mapMgr) &&
               state == Entity.State.WALKING;
    }

    private void checkSelection(Entity mapEntity, Rectangle mapEntityBoundingBox)
    {
        if(!mousePointsToEntity(mapEntity)) return;

        selectionRay.set(boundingBox.x, boundingBox.y, 0.0f, mapEntityBoundingBox.x, mapEntityBoundingBox.y, 0.0f);
        float distance = selectionRay.origin.dst(selectionRay.direction);

        selectIfWithinRange(mapEntity, distance);
    }

    private boolean mousePointsToEntity(Entity mapEntity)
    {
        return mapEntity.getCurrentBoundingBox().contains(mouseSelectCoordinates.x, mouseSelectCoordinates.y);
    }

    private void selectIfWithinRange(Entity mapEntity, float distance)
    {
        if(!isWithinRange(distance)) return;

        Gdx.app.debug(TAG, String.format("Selected Entity: %s", mapEntity.getEntityConfig().getEntityID()));
        mapEntity.sendMessage(MESSAGE.ENTITY_SELECTED);
    }

    private boolean isWithinRange(float distance)
    {
        return distance <= selectRayMaximumDistance;
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void receiveMessage(String message)
    {
        String[] string = message.split(Component.MESSAGE_TOKEN);
        if(string.length == 0) return;

        if(string.length == 2)
        {
            if(string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString()))
            {
                currentEntityPosition = json.fromJson(Vector2.class, string[1]);
                nextEntityPosition.set(currentEntityPosition.x, currentEntityPosition.y);
            } else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString()))
            {
                state = json.fromJson(Entity.State.class, string[1]);
            } else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString()))
            {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            } else if(string[0].equalsIgnoreCase(MESSAGE.INIT_SELECTED_ENTITY.toString()))
            {
                mouseSelectCoordinates = json.fromJson(Vector3.class, string[1]);
                isMouseSelectEnabled = true;
            }
        }
    }
}
