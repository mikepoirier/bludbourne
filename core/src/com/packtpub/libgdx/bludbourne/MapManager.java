package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;
import com.packtpub.libgdx.bludbourne.profile.ProfileObserver;

/**
 * Created by blindweasel on 12/1/15.
 */
public class MapManager implements ProfileObserver
{
	public static final String TAG = MapManager.class.getSimpleName();
    public static final String TOP_WORLD_MAP_START_POSITION_PROPERTY = "topWorldMapStartPosition";
    public static final String CURRENT_MAP_TYPE_PROPERTY = "currentMapType";
    public static final String CASTLE_OF_DOOM_MAP_START_POSITION_PROPERTY = "castleOfDoomMapStartPosition";
    public static final String TOWN_MAP_START_POSITION_PROPERTY = "townMapStartPosition";

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

    @Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event)
    {
        switch(event)
        {
            case PROFILE_LOADED:
                loadProfile(profileManager);
                break;
            case SAVING_PROFILE:
                saveProfile(profileManager);
                break;
        }
    }

    private void loadProfile(ProfileManager profileManager)
    {
        String currentMapStr = profileManager.getProperty(CURRENT_MAP_TYPE_PROPERTY, String.class);
        MapFactory.MapType mapType;
        if(currentMapStr == null || currentMapStr.isEmpty())
        {
            mapType = MapFactory.MapType.TOWN;
        } else
        {
            mapType = MapFactory.MapType.valueOf(currentMapStr);
        }
        loadMap(mapType);

        Vector2 topWorldMapStartPosition =
                profileManager.getProperty(TOP_WORLD_MAP_START_POSITION_PROPERTY, Vector2.class);
        if(topWorldMapStartPosition != null)
        {
            MapFactory.getMap(MapFactory.MapType.TOP_WORLD).setPlayerStart(topWorldMapStartPosition);
        }

        Vector2 castleOfDoomMapStartPosition =
                profileManager.getProperty(CASTLE_OF_DOOM_MAP_START_POSITION_PROPERTY, Vector2.class);
        if(castleOfDoomMapStartPosition != null)
        {
            MapFactory.getMap(MapFactory.MapType.CASTLE_OF_DOOM).setPlayerStart(castleOfDoomMapStartPosition);
        }

        Vector2 townMapStartPosition = profileManager.getProperty(TOWN_MAP_START_POSITION_PROPERTY, Vector2.class);
        if(townMapStartPosition != null)
        {
            MapFactory.getMap(MapFactory.MapType.TOWN).setPlayerStart(townMapStartPosition);
        }
    }

    private void saveProfile(ProfileManager profileManager)
    {
        profileManager.setProperty(CURRENT_MAP_TYPE_PROPERTY, currentMap.currentMapType.toString());
        profileManager.setProperty(TOP_WORLD_MAP_START_POSITION_PROPERTY,
                                   MapFactory.getMap(MapFactory.MapType.TOP_WORLD).getPlayerStart());
        profileManager.setProperty(CASTLE_OF_DOOM_MAP_START_POSITION_PROPERTY,
                                   MapFactory.getMap(MapFactory.MapType.CASTLE_OF_DOOM).getPlayerStart());
        profileManager.setProperty(TOWN_MAP_START_POSITION_PROPERTY,
                                   MapFactory.getMap(MapFactory.MapType.TOWN).getPlayerStart());
    }
}
