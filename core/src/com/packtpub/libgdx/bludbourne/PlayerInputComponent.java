package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public class PlayerInputComponent extends InputComponent implements InputProcessor
{
    private static final String TAG = PlayerInputComponent.class.getSimpleName();
    private Vector3 lastMouseCoordinates;

    public PlayerInputComponent()
    {
        lastMouseCoordinates = new Vector3();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update(Entity entity, float delta)
    {
        if(keys.get(Keys.LEFT))
        {
            entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
            entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.LEFT));
        } else if(keys.get(Keys.RIGHT))
        {
            entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
            entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.RIGHT));
        } else if(keys.get(Keys.UP))
        {
            entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
            entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.UP));
        } else if(keys.get(Keys.DOWN))
        {
            entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
            entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.DOWN));
        } else if(keys.get(Keys.QUIT))
        {
            Gdx.app.exit();
        } else
        {
            entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));
            if(currentDirection == null)
            {
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.DOWN));
            }
        }

        if(mouseButtons.get(Mouse.SELECT))
        {
            entity.sendMessage(MESSAGE.INIT_SELECTED_ENTITY, json.toJson(lastMouseCoordinates));
            mouseButtons.put(Mouse.SELECT, false);
        }
    }

    @Override
    public void dispose()
    {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void receiveMessage(String message)
    {
        String[] string = message.split(MESSAGE_TOKEN);
        if(string.length == 0) return;

        if(string.length == 2)
        {
            if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString()))
            {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            }
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if(isLeftKey(keycode))
        {
            leftPressed();
        }
        if(isRightKey(keycode))
        {
            rightPressed();
        }
        if(isUpKey(keycode))
        {
            upPressed();
        }
        if(isDownKey(keycode))
        {
            downPressed();
        }
        if(isQuitKey(keycode))
        {
            quitPressed();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if(isLeftKey(keycode))
        {
            this.leftReleased();
        }
        if(isRightKey(keycode))
        {
            this.rightReleased();
        }
        if(isUpKey(keycode))
        {
            upReleased();
        }
        if(isDownKey(keycode))
        {
            downReleased();
        }
        if(isQuitKey(keycode))
        {
            quitReleased();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if(isLeftButton(button) || isRightButton(button))
        {
            setClickedMouseCoordinates(screenX, screenY);
        }
        if(isLeftButton(button))
        {
            selectMouseButtonPressed(screenX, screenY);
        }
        if(isRightButton(button))
        {
            doActionMouseButtonPressed(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if(isLeftButton(button))
        {
            selectMouseButtonReleased(screenX, screenY);
        }
        if(isRightButton(button))
        {
            doActionMouseButtonReleased(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }

    public void leftPressed()
    {
        keyPressed(Keys.LEFT);
    }

    public void rightPressed()
    {
        keyPressed(Keys.RIGHT);
    }

    public void upPressed()
    {
        keyPressed(Keys.UP);
    }

    public void downPressed()
    {
        keyPressed(Keys.DOWN);
    }

    public void quitPressed()
    {
        keyPressed(Keys.QUIT);
    }

    public void setClickedMouseCoordinates(int x, int y)
    {
        lastMouseCoordinates.set(x, y, 0);
    }

    public void selectMouseButtonPressed(int x, int y)
    {
        mouseButtonPressed(Mouse.SELECT);
    }

    public void doActionMouseButtonPressed(int x, int y)
    {
        mouseButtonPressed(Mouse.DO_ACTION);
    }

    public void leftReleased()
    {
        keyReleased(Keys.LEFT);
    }

    public void rightReleased()
    {
        keyReleased(Keys.RIGHT);
    }

    public void upReleased()
    {
        keyReleased(Keys.UP);
    }

    public void downReleased()
    {
        keyReleased(Keys.DOWN);
    }

    public void quitReleased()
    {
        keyReleased(Keys.QUIT);
    }

    public void selectMouseButtonReleased(int x, int y)
    {
        mouseButtonReleased(Mouse.SELECT);
    }

    public void doActionMouseButtonReleased(int x, int y)
    {
        mouseButtonReleased(Mouse.DO_ACTION);
    }

    public static void hide()
    {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.QUIT, false);
    }

    private void mouseButtonPressed(Mouse mouseButton)
    {
        mouseButtons.put(mouseButton, true);
    }

    private void mouseButtonReleased(Mouse mouseButton)
    {
        mouseButtons.put(mouseButton, false);
    }

    private void keyPressed(Keys key)
    {
        keys.put(key, true);
    }

    private void keyReleased(Keys key)
    {
        keys.put(key, false);
    }

    private boolean isLeftButton(int button)
    {
        return button == Input.Buttons.LEFT;
    }

    private boolean isRightButton(int button)
    {
        return button == Input.Buttons.RIGHT;
    }

    private boolean isLeftKey(int keycode)
    {
        return keycode == Input.Keys.LEFT || keycode == Input.Keys.A;
    }

    private boolean isRightKey(int keycode)
    {
        return keycode == Input.Keys.RIGHT || keycode == Input.Keys.D;
    }

    private boolean isUpKey(int keycode)
    {
        return keycode == Input.Keys.UP || keycode == Input.Keys.W;
    }

    private boolean isDownKey(int keycode)
    {
        return keycode == Input.Keys.DOWN || keycode == Input.Keys.S;
    }

    private boolean isQuitKey(int keycode)
    {
        return keycode == Input.Keys.Q;
    }
}
