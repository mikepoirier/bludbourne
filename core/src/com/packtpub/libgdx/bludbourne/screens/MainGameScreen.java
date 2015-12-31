package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.MapManager;
import com.packtpub.libgdx.bludbourne.PlayerController;

/**
 * Created by blindweasel on 12/1/15.
 */
public class MainGameScreen implements Screen
{
	private static final String TAG = MainGameScreen.class.getSimpleName();
	private static MapManager mapMgr;
	private static Entity player;
	private PlayerController controller;
	private TextureRegion currentPlayerFrame;
	private Sprite currentPlayerSprite;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;

	public MainGameScreen()
	{
		mapMgr = new MapManager();
	}

	@Override
	public void show()
	{
		setupViewport(10, 10);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Viewport.viewportWidth, Viewport.viewportHeight);

		mapRenderer = new OrthogonalTiledMapRenderer(mapMgr.getCurrentMap(), MapManager.UNIT_SCALE);
		mapRenderer.setView(camera);

		Gdx.app.debug(TAG, "UnitScale value: " + mapRenderer.getUnitScale());

		player = new Entity();
		player.init(mapMgr.getPlayerStartUnitScaled().x, mapMgr.getPlayerStartUnitScaled().y);

		currentPlayerSprite = player.getFrameSprite();

		controller = new PlayerController(player);
		Gdx.input.setInputProcessor(controller);
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.position.set(currentPlayerSprite.getX(), currentPlayerSprite.getY(), 0f);
		camera.update();

		player.update(delta);
		currentPlayerFrame = player.getFrame();

		updatePortalLayerActivation(player.boundingBox);

		if(!isCollisionWithMapLayer(player.boundingBox))
		{
			player.setNextPositionToCurrent();
		}

		controller.update(delta);

		mapRenderer.setView(camera);
		mapRenderer.render();

		mapRenderer.getBatch().begin();
		mapRenderer.getBatch().draw(currentPlayerFrame, currentPlayerSprite.getX(), currentPlayerSprite.getY(), 1, 1);
		mapRenderer.getBatch().end();
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
		controller.dispose();
		Gdx.input.setInputProcessor(null);
		mapRenderer.dispose();
	}

	private boolean updatePortalLayerActivation(Rectangle boundingBox)
	{
		MapLayer mapPortalLayer = mapMgr.getPortalLayer();

		if(mapPortalLayer == null)
		{
			return false;
		}

		Rectangle rectangle;

		for(MapObject object : mapPortalLayer.getObjects())
		{
			if(object instanceof RectangleMapObject)
			{
				rectangle = ((RectangleMapObject) object).getRectangle();
				if(boundingBox.overlaps(rectangle))
				{
					String mapName = object.getName();
					if(mapName == null)
					{
						return false;
					}

					mapMgr.setClosestStartPositionFromScaledUnits(player.getCurrentPosition());
					mapMgr.loadMap(mapName);
					player.init(mapMgr.getPlayerStartUnitScaled().x, mapMgr.getPlayerStartUnitScaled().y);
					mapRenderer.setMap(mapMgr.getCurrentMap());
					Gdx.app.debug(TAG, "Portal Activated");
					return true;
				}
			}
		}
		return false;
	}

	private boolean isCollisionWithMapLayer(Rectangle boundingBox)
	{
		MapLayer mapCollisionLayer = mapMgr.getCollisionLayer();

		if(mapCollisionLayer == null)
		{
			return false;
		}

		Rectangle rectangle;

		for(MapObject object : mapCollisionLayer.getObjects())
		{
			if(object instanceof RectangleMapObject)
			{
				rectangle = ((RectangleMapObject) object).getRectangle();
				if(boundingBox.overlaps(rectangle))
				{
					return true;
				}
			}
		}

		return false;
	}

	private void setupViewport(int width, int height)
	{
		Viewport.virtualWidth = width;
		Viewport.virtualHeight = height;

		Viewport.viewportWidth = Viewport.virtualWidth;
		Viewport.viewportHeight = Viewport.virtualHeight;

		Viewport.physicalWidth = Gdx.graphics.getWidth();
		Viewport.physicalHeight = Gdx.graphics.getHeight();

		Viewport.aspectRatio = Viewport.virtualWidth / Viewport.virtualHeight;

		if(Viewport.physicalWidth / Viewport.physicalHeight >= Viewport.aspectRatio)
		{
			Viewport.viewportWidth = Viewport.viewportHeight * (Viewport.physicalWidth / Viewport.physicalHeight);
			Viewport.viewportHeight = Viewport.virtualHeight;
		} else
		{
			Viewport.viewportWidth = Viewport.virtualWidth;
			Viewport.viewportHeight = Viewport.viewportWidth * (Viewport.physicalHeight / Viewport.physicalWidth);
		}

		Gdx.app.debug(TAG, String.format("WorldRenderer: Virtual: (%.2f,%.2f)", Viewport.virtualWidth,
		                                 Viewport.virtualHeight));
		Gdx.app.debug(TAG, String.format("WorldRenderer: Viewport: (%.2f,%.2f)", Viewport.viewportWidth,
		                                 Viewport.viewportHeight));
		Gdx.app.debug(TAG, String.format("WorldRenderer: Physical: (%.2f,%.2f)", Viewport.physicalWidth,
		                                 Viewport.physicalHeight));
	}

	private static class Viewport
	{
		static float viewportWidth;
		static float viewportHeight;
		static float virtualWidth;
		static float virtualHeight;
		static float physicalWidth;
		static float physicalHeight;
		static float aspectRatio;
	}
}