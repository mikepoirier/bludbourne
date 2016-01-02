package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by blindweasel on 12/1/15.
 */
public class MapManager
{
	public static final String TAG = MapManager.class.getSimpleName();

	private Camera camera;
	private boolean mapChanged = false;
	private Map currentMap;
	private Entity player;

	public MapManager()
	{

	}

	public void loadMap(MapFactory.MapType mapType)
	{
		Map map = MapFactory.getMap(mapType);

		if(map == null)
		{
			Gdx.app.debug(TAG, String.format("Map does not exist: %s", mapType.toString()));
			return;
		}

		currentMap = map;
		mapChanged = true;
		Gdx.app.debug(TAG, String.format("Player Start: (%.2f,%.2f)", currentMap.getPlayerStart().x,
		                                 currentMap.getPlayerStart().y));
	}

    public void setClosestStartPositionFromScaledUnits(Vector2 position)
    {
        currentMap.setClosestStartPositionFromScaledUnits(position);
    }

    public MapLayer getCollisionLayer()
    {
        return currentMap.getCollisionLayer();
    }

    public MapLayer getPortalLayer()
    {
        return currentMap.getPortalLayer();
    }

    public Vector2 getPlayerStartUnitScaled()
    {
        return currentMap.getPlayerStartUnitScaled();
    }

    public TiledMap getCurrentTiledMap()
    {
        if(currentMap == null)
        {
            loadMap(MapFactory.MapType.TOWN);
        }
        return currentMap.getCurrentTiledMap();
    }

    public void updateCurrentMapEntites(MapManager mapMgr, Batch batch, float delta)
    {
        currentMap.updateMapEntities(mapMgr, batch, delta);
    }

    public final Array<Entity> getCurrentMapEntities()
    {
        return currentMap.getMapEntites();
    }

    public void setPlayer(Entity entity)
    {
        player = entity;
    }

    public Entity getPlayer()
    {
        return player;
    }

    public Camera getCamera()
    {
        return camera;
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    public boolean hasMapChanged()
    {
        return mapChanged;
    }

    public void setMapChanged(boolean mapChanged)
    {
        this.mapChanged = mapChanged;
    }
}
