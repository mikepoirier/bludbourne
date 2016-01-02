package com.packtpub.libgdx.bludbourne;

import java.util.Hashtable;

/**
 * Created by blindweasel on 1/1/16.
 */
public class MapFactory
{
    private static Hashtable<MapType, Map> mapTable = new Hashtable<>();

    public enum MapType
    {
        TOP_WORLD,
        TOWN,
        CASTLE_OF_DOOM
    }

    public static Map getMap(MapType mapType)
    {
        Map map = null;
        switch(mapType)
        {
            case TOP_WORLD:
                map = getTopWorldMap();
                break;
            case TOWN:
                map = getTownMap();
                break;
            case CASTLE_OF_DOOM:
                map = getCastleOfDoomMap();
                break;
        }
        return map;
    }

    private static Map getCastleOfDoomMap()
    {
        if(!mapTable.contains(MapType.CASTLE_OF_DOOM))
        {
            mapTable.put(MapType.CASTLE_OF_DOOM, new CastleDoomMap());
        }
        return mapTable.get(MapType.CASTLE_OF_DOOM);
    }

    private static Map getTownMap()
    {
        if(!mapTable.contains(MapType.TOWN))
        {
            mapTable.put(MapType.TOWN, new TownMap());
        }
        return mapTable.get(MapType.TOWN);
    }

    private static Map getTopWorldMap()
    {
        if(!mapTable.contains(MapType.TOP_WORLD))
        {
            mapTable.put(MapType.TOP_WORLD, new TopWorldMap());
        }
        return mapTable.get(MapType.TOP_WORLD);
    }
}
