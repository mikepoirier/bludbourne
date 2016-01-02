package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by blindweasel on 1/1/16.
 */
public class TownMap extends Map
{
    private static final String TAG = TownMap.class.getSimpleName();

    private static String mapPath = "maps/town.tmx";
    private static String townGuardWalking = "scripts/town_guard_walking.json";
    private static String townBlacksmith = "scripts/town_blacksmith.json";
    private static String townMage = "scripts/town_mage.json";
    private static String townInnKeeper = "scripts/town_innkeeper.json";
    private static String townFolk = "scripts/town_folk.json";

    TownMap()
    {
        super(MapFactory.MapType.TOWN, mapPath);
        for(Vector2 position : npcStartPositions)
        {
            mapEntites.add(initEntity(Entity.getEntityConfig(townGuardWalking), position));
        }

        mapEntites.add(initSpecialEntity(Entity.getEntityConfig(townBlacksmith)));
        mapEntites.add(initSpecialEntity(Entity.getEntityConfig(townMage)));
        mapEntites.add(initSpecialEntity(Entity.getEntityConfig(townInnKeeper)));

        Array<EntityConfig> configs = Entity.getEntityConfigs(townFolk);
        for(EntityConfig config : configs)
        {
            mapEntites.add(initSpecialEntity(config));
        }
    }

    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta)
    {
        for(Entity entity : mapEntites)
        {
            entity.update(mapMgr, batch, delta);
        }
    }

    private Entity initEntity(EntityConfig entityConfig, Vector2 position)
    {
        Entity entity = EntityFactory.getEntity(EntityFactory.EntityType.NPC);
        entity.setEntityConfig(entityConfig);

        entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, json.toJson(entity.getEntityConfig()));
        entity.sendMessage(Component.MESSAGE.INIT_START_POSITION, json.toJson(position));
        entity.sendMessage(Component.MESSAGE.INIT_STATE, json.toJson(entity.getEntityConfig().getState()));
        entity.sendMessage(Component.MESSAGE.INIT_DIRECTION, json.toJson(entity.getEntityConfig().getDirection()));

        return entity;
    }

    private Entity initSpecialEntity(EntityConfig entityConfig)
    {
        Vector2 position = new Vector2();
        if(specialNPCStartPositions.containsKey(entityConfig.getEntityID()))
        {
            position = specialNPCStartPositions.get(entityConfig.getEntityID());
        }
        return initEntity(entityConfig, position);
    }
}
