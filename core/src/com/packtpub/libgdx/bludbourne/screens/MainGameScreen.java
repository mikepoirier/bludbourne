package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Json;
import com.packtpub.libgdx.bludbourne.Component;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityFactory;
import com.packtpub.libgdx.bludbourne.Map;
import com.packtpub.libgdx.bludbourne.MapManager;

/**
 * Created by blindweasel on 12/1/15.
 */
public class MainGameScreen implements Screen
{
    private static final String TAG = MainGameScreen.class.getSimpleName();

    private static class VIEWPORT
    {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    private OrthogonalTiledMapRenderer mapRenderer = null;
    private OrthographicCamera camera = null;
    private static MapManager mapMgr;
    private Json json;

    public MainGameScreen()
    {
        mapMgr = new MapManager();
        json = new Json();
    }

    private static Entity player;

    @Override
    public void show()
    {
        setupViewport(10, 10);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        mapRenderer = new OrthogonalTiledMapRenderer(mapMgr.getCurrentTiledMap(), Map.UNIT_SCALE);
        mapRenderer.setView(camera);

        mapMgr.setCamera(camera);

        Gdx.app.debug(TAG, String.format("UNIT_SCALE value is: %.2f", mapRenderer.getUnitScale()));

        player = EntityFactory.getEntity(EntityFactory.EntityType.PLAYER);
        mapMgr.setPlayer(player);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);

        if(mapMgr.hasMapChanged())
        {
            mapRenderer.setMap(mapMgr.getCurrentTiledMap());
            player.sendMessage(Component.MESSAGE.INIT_START_POSITION, json.toJson(mapMgr.getPlayerStartUnitScaled()));

            camera.position.set(mapMgr.getPlayerStartUnitScaled(), 0.0f);
            camera.update();

            mapMgr.setMapChanged(false);
        }

        mapRenderer.render();

        mapMgr.updateCurrentMapEntites(mapMgr, mapRenderer.getBatch(), delta);

        player.update(mapMgr, mapRenderer.getBatch(), delta);
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        player.dispose();
        mapRenderer.dispose();
    }

    private void setupViewport(int width, int height)
    {
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;

        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;

        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

        VIEWPORT.aspectRatio = VIEWPORT.viewportWidth / VIEWPORT.virtualHeight;

        if(VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio)
        {
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth/VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        } else
        {
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
        }

        Gdx.app.debug(TAG, String.format("WorldRenderer: virtual: (%.2f,%.2f)", VIEWPORT.virtualWidth, VIEWPORT.virtualHeight));
        Gdx.app.debug(TAG, String.format("WorldRenderer: viewport: (%.2f,%.2f)", VIEWPORT.viewportWidth, VIEWPORT.viewportHeight));
        Gdx.app.debug(TAG, String.format("WorldRenderer: physical: (%.2f,%.2f)", VIEWPORT.physicalWidth, VIEWPORT.physicalHeight));
    }
}
