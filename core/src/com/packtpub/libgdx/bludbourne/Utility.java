package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Created by blindweasel on 12/1/15.
 */
public final class Utility
{
	public static final AssetManager ASSET_MANAGER = new AssetManager();

	private static final String TAG = Utility.class.getSimpleName();

	private static final InternalFileHandleResolver FILE_PATH_RESOLVER = new InternalFileHandleResolver();

	public static void unloadAsset(String assetFilenamePath)
	{
		// Once the asset manager is done loading
		if(ASSET_MANAGER.isLoaded(assetFilenamePath))
		{
			ASSET_MANAGER.unload(assetFilenamePath);
		} else
		{
			Gdx.app.debug(TAG, "Asset is not loaded, Nothing to unload: " + assetFilenamePath);
		}
	}

	public static float loadCompleted()
	{
		return ASSET_MANAGER.getProgress();
	}

	public static int numberAssetsQueued()
	{
		return ASSET_MANAGER.getQueuedAssets();
	}

	public static boolean updateAssetLoading()
	{
		return ASSET_MANAGER.update();
	}

	public static boolean isAssetLoaded(String filename)
	{
		return ASSET_MANAGER.isLoaded(filename);
	}

	public static void loadMapAsset(String mapFilenamePath)
	{
		if(mapFilenamePath == null || mapFilenamePath.isEmpty())
		{
			return;
		}

		if(FILE_PATH_RESOLVER.resolve(mapFilenamePath).exists())
		{
			ASSET_MANAGER.setLoader(TiledMap.class, new TmxMapLoader(FILE_PATH_RESOLVER));
			ASSET_MANAGER.load(mapFilenamePath, TiledMap.class);

			ASSET_MANAGER.finishLoadingAsset(mapFilenamePath);
			Gdx.app.debug(TAG, "Map loaded: " + mapFilenamePath);
		} else
		{
			Gdx.app.debug(TAG, "Map doesn't exist: " + mapFilenamePath);
		}
	}

	public static TiledMap getMapAsset(String mapFilenamePath)
	{
		TiledMap map = null;

		if(ASSET_MANAGER.isLoaded(mapFilenamePath))
		{
			map = ASSET_MANAGER.get(mapFilenamePath, TiledMap.class);
		} else
		{
			Gdx.app.debug(TAG, "Map is not loaded: " + mapFilenamePath);
		}

		return map;
	}

	public static void loadTextureAsset(String textureFilenamePath)
	{
		if(textureFilenamePath == null || textureFilenamePath.isEmpty())
		{
			return;
		}

		if(FILE_PATH_RESOLVER.resolve(textureFilenamePath).exists())
		{
			ASSET_MANAGER.setLoader(Texture.class, new TextureLoader(FILE_PATH_RESOLVER));
			ASSET_MANAGER.load(textureFilenamePath, Texture.class);
			ASSET_MANAGER.finishLoadingAsset(textureFilenamePath);
		} else
		{
			Gdx.app.debug(TAG, "Texture doesn't exist: " + textureFilenamePath);
		}
	}

	public static Texture getTextureAsset(String textureFilenamePath)
	{
		Texture texture = null;

		if(ASSET_MANAGER.isLoaded(textureFilenamePath))
		{
			texture = ASSET_MANAGER.get(textureFilenamePath, Texture.class);
		} else
		{
			Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilenamePath);
		}

		return texture;
	}
}
