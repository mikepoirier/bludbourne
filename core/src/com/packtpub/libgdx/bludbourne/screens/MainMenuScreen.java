package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.Utility;

/**
 * Created by blindweasel on 1/3/16.
 */
public class MainMenuScreen implements Screen
{
    public static final String BLUDBOURNE_TITLE_REGION = "bludbourne_title";
    private Stage stage;
    private BludBourne game;

    public MainMenuScreen(BludBourne game)
    {
        this.game = game;

        this.stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);

        Image title = new Image(Utility.STATUS_UI_TEXTURE_ATLAS.findRegion(BLUDBOURNE_TITLE_REGION));
        TextButton newGameButton = new TextButton("New Game", Utility.STATUS_UI_SKIN);
        TextButton loadGameButton = new TextButton("Load Game", Utility.STATUS_UI_SKIN);
        TextButton exitButton = new TextButton("Exit", Utility.STATUS_UI_SKIN);

        table.add(title).spaceBottom(75).row();
        table.add(newGameButton).spaceBottom(10).row();
        table.add(loadGameButton).spaceBottom(10).row();
        table.add(exitButton).spaceBottom(10).row();

        stage.addActor(table);

        newGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                game.setScreen(game.getScreenType(BludBourne.ScreenType.NEW_GAME));
                return true;
            }
        });

        loadGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                game.setScreen(game.getScreenType(BludBourne.ScreenType.LOAD_GAME));
                return true;
            }
        });

        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.exit();
                return true;
            }
        });
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().setScreenSize(width, height);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}
