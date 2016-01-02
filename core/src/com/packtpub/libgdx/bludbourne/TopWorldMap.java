package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by blindweasel on 1/1/16.
 */
public class TopWorldMap extends Map
{
    private static final String MAP_PATH = "maps/topworld.tmx";

    TopWorldMap()
    {
        super(MapFactory.MapType.TOP_WORLD, MAP_PATH);
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
