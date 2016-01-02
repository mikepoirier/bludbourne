package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public class NPCGraphicsComponent extends GraphicsComponent {
    private static final String TAG = NPCGraphicsComponent.class.getSimpleName();

    private boolean isSelected = false;

    public NPCGraphicsComponent(){
    }

    @Override
    public void receiveMessage(String message) {
        //Gdx.app.debug(TAG, "Got message " + message);
        String[] string = message.split(MESSAGE_TOKEN);

        if( string.length == 0 ) return;

        if( string.length == 1 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.ENTITY_SELECTED.toString())) {
                isSelected = true;
            }else if (string[0].equalsIgnoreCase(MESSAGE.ENTITY_DESELECTED.toString())) {
                isSelected = false;
            }
        }

        //Specifically for messages with 1 object payload
        if( string.length == 2 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_POSITION.toString())) {
                currentPosition = json.fromJson(Vector2.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString())) {
                currentPosition = json.fromJson(Vector2.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString())) {
                currentState = json.fromJson(Entity.State.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString())) {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            }else if (string[0].equalsIgnoreCase(MESSAGE.LOAD_ANIMATIONS.toString())) {
                EntityConfig entityConfig = json.fromJson(EntityConfig.class, string[1]);
                Array<EntityConfig.AnimationConfig> animationConfigs = entityConfig.getAnimationConfig();

                for( EntityConfig.AnimationConfig animationConfig : animationConfigs ){
                    loadAnimationConfig(animationConfig);
                }
            }
        }
    }

    @Override
    public void update(Entity entity, MapManager mapMgr, Batch batch, float delta){
        updateAnimations(delta);

        if(isSelected){
            drawSelected(entity, mapMgr);
        }

        batch.begin();
        batch.draw(currentFrame, currentPosition.x, currentPosition.y, 1, 1);
        batch.end();
    }

    private void drawSelected(Entity entity, MapManager mapMgr){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Camera camera = mapMgr.getCamera();
        Rectangle rect = entity.getCurrentBoundingBox();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.0f, 1.0f, 1.0f, 0.5f);

        float width =  rect.getWidth() * Map.UNIT_SCALE*2f;
        float height = rect.getHeight() * Map.UNIT_SCALE/2f;
        float x = rect.x * Map.UNIT_SCALE - width/4;
        float y = rect.y * Map.UNIT_SCALE - height/2;

        shapeRenderer.ellipse(x,y,width,height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }


    @Override
    public void dispose(){
    }
}
