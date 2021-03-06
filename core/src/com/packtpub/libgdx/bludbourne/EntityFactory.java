package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.utils.Json;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public class EntityFactory {
    private static Json json = new Json();

    private enum EntityType
    {
        PLAYER,
        DEMO_PLAYER,
        NPC
    }

    private static String player_config = "scripts/player.json";

    public static Entity getentity(EntityType entityType)
    {
        switch (entityType)
        {
            case PLAYER:
                Entity entity = new Entity(
                        new PlayerInputComponent(),
                        new PlayerPhysicsComponent(),
                        new PlayerGraphicsComponent());
                entity.setEntityConfig(Entity.getEntityConfig(
                        EntityFactory.PLAYER_CONFIG));
                entity.sendMessage(
                        Component.MESSAGE.LOAD_ANIMATIONS,
                        json.toJson(entity.getEntityConfig()));
                return entity;
            case DEMO_PLAYER:
                return new Entity(
                        new NPCInputComponent(),
                        new PlayerPhysicsComponent(),
                        new PlayerGraphicsComponent());
            case NPC:
                return new Entity(
                        new NPCInputComponent(),
                        new NPCPhysicsComponent(),
                        new NPCGraphicsComponent());
            default:
                return null;
        }
    }
}
