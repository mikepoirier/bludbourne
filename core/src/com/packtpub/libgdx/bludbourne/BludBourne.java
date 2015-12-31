package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Game;
import com.packtpub.libgdx.bludbourne.screens.MainGameScreen;

public class BludBourne extends Game
{
	public static final MainGameScreen MAIN_GAME_SCREEN = new MainGameScreen();

	@Override
	public void create()
	{
		setScreen(MAIN_GAME_SCREEN);
	}

	@Override
	public void dispose()
	{
		MAIN_GAME_SCREEN.dispose();
	}
}
