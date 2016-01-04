package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;

/**
 * Created by blindweasel on 1/3/16.
 */
public class LoadGameScreen implements Screen
{
    private static final String TAG = LoadGameScreen.class.getSimpleName();
    private Stage stage;
    private BludBourne game;

    public LoadGameScreen(BludBourne game)
    {
        this.game = game;
        this.stage = new Stage();

        TextButton loadButton = new TextButton("Load", Utility.STATUS_UI_SKIN);
        TextButton backButton = new TextButton("Back", Utility.STATUS_UI_SKIN);

        ProfileManager.getInstance().storeAllProfiles();
        Array<String> list = ProfileManager.getInstance().getProfileList();
        final List listItems = new List(Utility.STATUS_UI_SKIN, "inventory");
        listItems.setItems(list);
        ScrollPane scrollPane = new ScrollPane(listItems);

        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);

        Table table = new Table();
        Table bottomTable = new Table();

        table.center();
        table.setFillParent(true);
        table.padBottom(loadButton.getHeight());
        table.add(scrollPane).center();

        bottomTable.setHeight(loadButton.getHeight());
        bottomTable.setWidth(Gdx.graphics.getWidth());
        bottomTable.center();
        bottomTable.add(loadButton).padRight(50);
        bottomTable.add(backButton);

        stage.addActor(table);
        stage.addActor(bottomTable);

        backButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                game.setScreen(game.getScreenType(BludBourne.ScreenType.MAIN_MENU));
                return true;
            }
        });

        loadButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                String fileName = listItems.getSelected().toString();
                if(fileName != null)
                {
                    FileHandle file = ProfileManager.getInstance().getProfileFile(fileName);
                    if(file != null)
                    {
                        ProfileManager.getInstance().setCurrentProfile(fileName);
                        ProfileManager.getInstance().loadProfile();
                        game.setScreen(game.getScreenType(BludBourne.ScreenType.MAIN_GAME));
                    }
                }

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
        if(delta == 0)
        {
            return;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
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
        stage.clear();
        stage.dispose();
    }
}
