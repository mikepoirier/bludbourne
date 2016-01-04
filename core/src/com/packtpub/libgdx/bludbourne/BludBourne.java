package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.packtpub.libgdx.bludbourne.screens.LoadGameScreen;
import com.packtpub.libgdx.bludbourne.screens.MainGameScreen;
import com.packtpub.libgdx.bludbourne.screens.MainMenuScreen;
import com.packtpub.libgdx.bludbourne.screens.NewGameScreen;

public class BludBourne extends Game
{
    public static MainGameScreen mainGameScreen;
    public static MainMenuScreen mainMenuScreen;
    public static LoadGameScreen loadGameScreen;
    public static NewGameScreen newGameScreen;

    public Screen getScreenType(ScreenType screenType)
    {
        switch(screenType)
        {
            case MAIN_GAME:
                return mainGameScreen;
            case LOAD_GAME:
                return loadGameScreen;
            case NEW_GAME:
                return newGameScreen;
            case MAIN_MENU:
            default:
                return mainMenuScreen;
        }
    }

    @Override
    public void create()
    {
        mainGameScreen = new MainGameScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        loadGameScreen = new LoadGameScreen(this);
        newGameScreen = new NewGameScreen(this);
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose()
    {
        mainGameScreen.dispose();
        mainMenuScreen.dispose();
        loadGameScreen.dispose();
        newGameScreen.dispose();
    }

    public enum ScreenType
    {
        MAIN_MENU,
        MAIN_GAME,
        LOAD_GAME,
        NEW_GAME
    }
}
