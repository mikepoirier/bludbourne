package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

/**
 * Created by blindweasel on 12/1/15.
 */
public class MapManager
{
	public static final String TAG = MapManager.class.getSimpleName();
	public final static float UNIT_SCALE = 1 / 16f;
	private final static String TOP_WORLD = "TOP_WORLD";
	private final static String TOWN = "TOWN";
	private final static String CASTLE_OF_DOOM = "CASTLE_OF_DOOM";
	private final static String MAP_COLLISION_LAYER = "MAP_COLLISION_LAYER";
	private final static String MAP_SPAWNS_LAYER = "MAP_SPAWNS_LAYER";
	private final static String MAP_PORTAL_LAYER = "MAP_PORTAL_LAYER";
	private final static String PLAYER_START = "PLAYER_START";
	private HashMap<String, String> mapTable;
	private HashMap<String, Vector2> playerStartLocationTable;
	private Vector2 playerStartPositionRect;
	private Vector2 closestPlayerStartPosition;
	private Vector2 convertedUnits;
	private Vector2 playerStart;
	private TiledMap currentMap = null;
	private String currentMapName;
	private MapLayer collisionLayer = null;
	private MapLayer portalLayer = null;
	private MapLayer spawnsLayer = null;

	public MapManager()
	{
		playerStart = new Vector2(0, 0);
		mapTable = new HashMap<>();

		mapTable.put(TOP_WORLD, "maps/topworld.tmx");
		mapTable.put(TOWN, "maps/town.tmx");
		mapTable.put(CASTLE_OF_DOOM, "maps/castle_of_doom.tmx");

		playerStartLocationTable = new HashMap<>();
		playerStartLocationTable.put(TOP_WORLD, playerStart.cpy());
		playerStartLocationTable.put(TOWN, playerStart.cpy());
		playerStartLocationTable.put(CASTLE_OF_DOOM, playerStart.cpy());

		playerStartPositionRect = new Vector2(0, 0);
		closestPlayerStartPosition = new Vector2(0, 0);
		convertedUnits = new Vector2(0, 0);
	}

	public TiledMap getCurrentMap()
	{
		if(currentMap == null)
		{
			currentMapName = TOWN;
			loadMap(currentMapName);
		}
		Gdx.app.debug(TAG, "Current Map: " + currentMapName);
		return currentMap;
	}

	public void loadMap(String mapName)
	{
		Gdx.app.debug(TAG, "Loading Map: " + mapName);
		playerStart.set(0, 0);
		String mapFullPath = mapTable.get(mapName);

		if(mapFullPath == null || mapFullPath.isEmpty())
		{
			Gdx.app.debug(TAG, "Map is invalid: " + mapName);
			return;
		}

		if(currentMap != null)
		{
			Gdx.app.debug(TAG, "Disposing Map: " + currentMapName);
			currentMap.dispose();
		}

		Utility.loadMapAsset(mapFullPath);
		if(Utility.isAssetLoaded(mapFullPath))
		{
			currentMap = Utility.getMapAsset(mapFullPath);
			currentMapName = mapName;
			Gdx.app.debug(TAG, "Setting current map: " + currentMapName);
		} else
		{
			Gdx.app.debug(TAG, "Map not loaded: " + mapName);
			return;
		}

		collisionLayer = currentMap.getLayers().get(MAP_COLLISION_LAYER);
		if(collisionLayer == null)
		{
			Gdx.app.debug(TAG, "No collision layer: " + mapName);
		}

		portalLayer = currentMap.getLayers().get(MAP_PORTAL_LAYER);
		if(portalLayer == null)
		{
			Gdx.app.debug(TAG, "No portal layer: " + mapName);
		}

		spawnsLayer = currentMap.getLayers().get(MAP_SPAWNS_LAYER);
		if(spawnsLayer == null)
		{
			Gdx.app.debug(TAG, "No spawn layer: " + mapName);
		} else
		{
			Vector2 start = playerStartLocationTable.get(currentMapName);
			if(start.isZero())
			{
				setClosestStartPosition(playerStart);
				start = playerStartLocationTable.get(currentMapName);
			}
			playerStart.set(start.x, start.y);
		}

		Gdx.app.debug(TAG, String.format("Player Start: (%.2f,%.2f)", playerStart.x, playerStart.y));
		Gdx.app.debug(TAG, "Finished Loading Map: " + mapName);
	}

	private void setClosestStartPosition(final Vector2 position)
	{
		Gdx.app.debug(TAG, String.format("setClosestStartPosition INPUT: (%.2f,%.2f) %s", position.x, position.y,
		                                 currentMapName));
		playerStartPositionRect.set(0, 0);
		closestPlayerStartPosition.set(0, 0);
		float shortestDistance = 0f;
		for(MapObject object : spawnsLayer.getObjects())
		{
			if(object.getName().equalsIgnoreCase(PLAYER_START))
			{
				((RectangleMapObject) object).getRectangle().getPosition(playerStartPositionRect);
				float distance = position.dst2(playerStartPositionRect);
				Gdx.app.debug(TAG, String.format("distance %.2f for %s", distance, currentMapName));

				if(distance < shortestDistance || shortestDistance == 0)
				{
					closestPlayerStartPosition.set(playerStartPositionRect);
					shortestDistance = distance;
					Gdx.app.debug(TAG, String.format("closest START is: (%.2f,%.2f) %s", closestPlayerStartPosition.x,
					                                 closestPlayerStartPosition.y, currentMapName));
				}
			}
		}
		playerStartLocationTable.put(currentMapName, closestPlayerStartPosition.cpy());
	}

	public void setClosestStartPositionFromScaledUnits(Vector2 position)
	{
		if(UNIT_SCALE <= 0)
		{
			return;
		}

		convertedUnits.set(position.x / UNIT_SCALE, position.y / UNIT_SCALE);
		setClosestStartPosition(convertedUnits);
	}

	public MapLayer getCollisionLayer()
	{
		return collisionLayer;
	}

	public MapLayer getPortalLayer()
	{
		return portalLayer;
	}

	public Vector2 getPlayerStartUnitScaled()
	{
		Vector2 playerStartCopy = playerStart.cpy();
		playerStartCopy.set(playerStart.x * UNIT_SCALE, playerStart.y * UNIT_SCALE);
		return playerStartCopy;
	}
}
