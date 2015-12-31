package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.UUID;

/**
 * Created by blindweasel on 12/1/15.
 */
public class Entity
{
	public static final String TAG = Entity.class.getSimpleName();
	private static final String DEFAULT_SPRITE_PATH = "sprites/characters/Warrior.png";
	public static Rectangle boundingBox;
	public final int FRAME_WIDTH = 16;
	public final int FRAME_HEIGHT = 16;
	protected Vector2 nextPlayerPosition;
	protected Vector2 currentPlayerPosition;
	protected State state = State.IDLE;
	protected float frameTime = 0f;
	protected Sprite frameSprite;
	protected TextureRegion currentFrame;
	private Vector2 velocity;
	private String entityID;
	private Direction currentDirection = Direction.LEFT;
	private Direction previousDirection = Direction.UP;
	private Animation walkLeftAnimation;
	private Animation walkRightAnimation;
	private Animation walkUpAnimation;
	private Animation walkDownAnimation;
	private Array<TextureRegion> walkLeftFrames;
	private Array<TextureRegion> walkRightFrames;
	private Array<TextureRegion> walkUpFrames;
	private Array<TextureRegion> walkDownFrames;

	public Entity()
	{
		initEntity();
	}

	private void initEntity()
	{
		entityID = UUID.randomUUID().toString();
		nextPlayerPosition = new Vector2();
		currentPlayerPosition = new Vector2();
		boundingBox = new Rectangle();
		velocity = new Vector2(2f, 2f);

		Utility.loadTextureAsset(DEFAULT_SPRITE_PATH);
		loadDefaultSprite();
		loadAllAnimations();
	}

	private void loadDefaultSprite()
	{
		Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
		TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
		frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		currentFrame = textureFrames[0][0];
	}

	private void loadAllAnimations()
	{
		Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
		TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
		walkDownFrames = new Array<>(4);
		walkLeftFrames = new Array<>(4);
		walkRightFrames = new Array<>(4);
		walkUpFrames = new Array<>(4);

		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				TextureRegion region = textureFrames[i][j];
				if(region == null)
				{
					Gdx.app.debug(TAG, String.format("Got null animation frame (%d,%d)", i, j));
				}

				switch(i)
				{
					case 0:
						walkDownFrames.insert(j, region);
						break;
					case 1:
						walkLeftFrames.insert(j, region);
						break;
					case 2:
						walkRightFrames.insert(j, region);
						break;
					case 3:
						walkUpFrames.insert(j, region);
						break;
				}
			}
		}

		walkDownAnimation = new Animation(0.25f, walkDownFrames, Animation.PlayMode.LOOP);
		walkLeftAnimation = new Animation(0.25f, walkLeftFrames, Animation.PlayMode.LOOP);
		walkRightAnimation = new Animation(0.25f, walkRightFrames, Animation.PlayMode.LOOP);
		walkUpAnimation = new Animation(0.25f, walkUpFrames, Animation.PlayMode.LOOP);
	}

	public void init(float x, float y)
	{
		currentPlayerPosition.x = x;
		currentPlayerPosition.y = y;

		nextPlayerPosition.x = x;
		nextPlayerPosition.y = y;
	}

	public void update(float delta)
	{
		frameTime = (frameTime + delta) % 5;
		setBoundingBoxSize(0f, 0.5f);
	}

	public void setBoundingBoxSize(float percentageWidthReduced, float percentageHeightReduced)
	{
		float width;
		float height;

		float widthReductionAmount = 1f - percentageWidthReduced;
		float heightReductionAmount = 1f - percentageHeightReduced;

		if(widthReductionAmount > 0 && widthReductionAmount < 1)
		{
			width = FRAME_WIDTH * heightReductionAmount;
		} else
		{
			width = FRAME_WIDTH;
		}

		if(heightReductionAmount > 0 && heightReductionAmount < 1)
		{
			height = FRAME_HEIGHT * heightReductionAmount;
		} else
		{
			height = FRAME_HEIGHT;
		}

		if(width == 0 || height == 0)
		{
			Gdx.app.debug(TAG, "Width and Height are 0!");
		}

		float minX;
		float minY;
		if(MapManager.UNIT_SCALE > 0)
		{
			minX = nextPlayerPosition.x / MapManager.UNIT_SCALE;
			minY = nextPlayerPosition.y / MapManager.UNIT_SCALE;
		} else
		{
			minX = nextPlayerPosition.x;
			minY = nextPlayerPosition.y;
		}

		boundingBox.set(minX, minY, width, height);
	}

	public void dispose()
	{
		Utility.unloadAsset(DEFAULT_SPRITE_PATH);
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public Sprite getFrameSprite()
	{
		return frameSprite;
	}

	public TextureRegion getFrame()
	{
		return currentFrame;
	}

	public Vector2 getCurrentPosition()
	{
		return currentPlayerPosition;
	}

	public void setDirection(Direction direction, float deltaTime)
	{
		previousDirection = currentDirection;
		currentDirection = direction;

		switch(currentDirection)
		{
			case DOWN:
				currentFrame = walkDownAnimation.getKeyFrame(frameTime);
				break;
			case LEFT:
				currentFrame = walkLeftAnimation.getKeyFrame(frameTime);
				break;
			case UP:
				currentFrame = walkUpAnimation.getKeyFrame(frameTime);
				break;
			case RIGHT:
				currentFrame = walkRightAnimation.getKeyFrame(frameTime);
				break;
		}
	}

	public void setNextPositionToCurrent()
	{
		setCurrentPosition(nextPlayerPosition.x, nextPlayerPosition.y);
	}

	public void setCurrentPosition(float x, float y)
	{
		frameSprite.setX(x);
		frameSprite.setY(y);
		currentPlayerPosition.x = x;
		currentPlayerPosition.y = y;
	}

	public void calculateNextPosition(Direction currentDirection, float deltaTime)
	{
		float x = currentPlayerPosition.x;
		float y = currentPlayerPosition.y;

		velocity.scl(deltaTime);

		switch(currentDirection)
		{
			case LEFT:
				x -= velocity.x;
				break;
			case RIGHT:
				x += velocity.x;
				break;
			case UP:
				y += velocity.y;
				break;
			case DOWN:
				y -= velocity.y;
				break;
			default:
				break;
		}

		nextPlayerPosition.x = x;
		nextPlayerPosition.y = y;

		velocity.scl(1 / deltaTime);
	}

	public enum State
	{
		IDLE,
		WALKING
	}

	public enum Direction
	{
		UP,
		RIGHT,
		DOWN,
		LEFT
	}
}
