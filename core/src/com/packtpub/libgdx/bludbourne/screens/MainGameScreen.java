package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Json;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.Component;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.EntityFactory;
import com.packtpub.libgdx.bludbourne.Map;
import com.packtpub.libgdx.bludbourne.MapManager;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;
import com.packtpub.libgdx.bludbourne.ui.PlayerHUD;

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

    public enum GameState
    {
        RUNNING,
        PAUSED
    }

    private static GameState gameState;
    private OrthogonalTiledMapRenderer mapRenderer = null;
    private OrthographicCamera camera = null;
    private OrthographicCamera hudCamera = null;
    private static MapManager mapMgr;
    private Json json;
    private BludBourne game;
    private InputMultiplexer multiplexer;

    private static Entity player;
    private static PlayerHUD playerHUD;

    public MainGameScreen(BludBourne game)
    {
        this.game = game;
        init();
    }

    private void init()
    {
        mapMgr = new MapManager();
        json = new Json();

        gameState = GameState.RUNNING;
        setupViewport(10, 10);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        mapRenderer = new OrthogonalTiledMapRenderer(mapMgr.getCurrentTiledMap(), Map.UNIT_SCALE);
        mapRenderer.setView(camera);
        mapMgr.setCamera(camera);

        Gdx.app.debug(TAG, String.format("UnitScale value is: %.2f", mapRenderer.getUnitScale()));

        player = EntityFactory.getEntity(EntityFactory.EntityType.PLAYER);
        mapMgr.setPlayer(player);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);

        playerHUD = new PlayerHUD(hudCamera, player);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(playerHUD.getStage());
        multiplexer.addProcessor(player.getInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);

        ProfileManager.getInstance().addObserver(playerHUD);
        ProfileManager.getInstance().addObserver(mapMgr);
    }

    @Override
    public void show()
    {
        gameState = GameState.RUNNING;
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta)
    {
        if(gameState == GameState.PAUSED)
        {
            player.updateInput(delta);
            return;
        }
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
        playerHUD.render(delta);
    }

    @Override
    public void resize(int width, int height)
    {
        setupViewport(10, 10);
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        playerHUD.resize((int) VIEWPORT.physicalWidth, (int) VIEWPORT.physicalHeight);
    }

    @Override
    public void pause()
    {
        gameState = GameState.PAUSED;
        ProfileManager.getInstance().saveProfile();
    }

    @Override
    public void resume()
    {
        gameState = GameState.RUNNING;
        ProfileManager.getInstance().loadProfile();
    }

    @Override
    public void hide()
    {
        gameState = GameState.PAUSED;
        Gdx.input.setInputProcessor(null);
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
