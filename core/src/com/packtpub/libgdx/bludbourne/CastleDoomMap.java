package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by blindweasel on 1/1/16.
 */
public class CastleDoomMap extends Map
{
    private static final String MAP_PATH = "maps/castle_of_doom.tmx";

    CastleDoomMap()
    {
        super(MapFactory.MapType.CASTLE_OF_DOOM, MAP_PATH);
    }

    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta)
    {
        for(Entity entity : mapEntites)
        {
            entity.update(mapMgr, batch, delta);
        }
    }
}
