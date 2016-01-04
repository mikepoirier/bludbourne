package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;

/**
 * Created by blindweasel on 1/3/16.
 */
public class NewGameScreen implements Screen
{
    private Stage stage;
    private BludBourne game;

    public NewGameScreen(BludBourne game)
    {
        this.game = game;

        init();
    }

    private void init()
    {
        stage = new Stage();
        Label profileName = new Label("Enter Profile Name: ", Utility.STATUS_UI_SKIN);
        final TextField profileText = new TextField("", Utility.STATUS_UI_SKIN, "inventory");
        profileText.setMaxLength(20);

        final Dialog overwriteDialog = new Dialog("Overwrite?", Utility.STATUS_UI_SKIN, "solidbackground");
        Label overwriteLabel = new Label("Overwrite existing prifile name?", Utility.STATUS_UI_SKIN);
        TextButton cancelButton = new TextButton("Cancel", Utility.STATUS_UI_SKIN);

        TextButton overwriteButton = new TextButton("Overwrite", Utility.STATUS_UI_SKIN, "inventory");
        overwriteDialog.setKeepWithinStage(true);
        overwriteDialog.setModal(true);
        overwriteDialog.setMovable(false);
        overwriteDialog.text(overwriteLabel);

        TextButton startButton = new TextButton("Start", Utility.STATUS_UI_SKIN);
        TextButton backButton = new TextButton("Back", Utility.STATUS_UI_SKIN);

        overwriteDialog.row();
        overwriteDialog.button(overwriteButton).bottom().left();
        overwriteDialog.button(cancelButton).bottom().right();

        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.add(profileName).center();
        topTable.add(profileText).center();

        Table bottomTable = new Table();
        bottomTable.setHeight(startButton.getHeight());
        bottomTable.setWidth(Gdx.graphics.getWidth());
        bottomTable.center();
        bottomTable.add(startButton).padRight(50);
        bottomTable.add(backButton);

        stage.addActor(topTable);
        stage.addActor(bottomTable);

        cancelButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                overwriteDialog.hide();
                return true;
            }
        });

        overwriteButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                String messageText = profileText.getText();
                ProfileManager.getInstance().writeProfileToStorage(messageText, "", true);
                ProfileManager.getInstance().setCurrentProfile(messageText);
                ProfileManager.getInstance().saveProfile();
                ProfileManager.getInstance().loadProfile();
                game.setScreen(game.getScreenType(BludBourne.ScreenType.MAIN_GAME));
                return true;
            }
        });

        startButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                String messageText = profileText.getText();
                boolean exists = ProfileManager.getInstance().profileExists(messageText);

                if(exists)
                {
                    overwriteDialog.show(stage);
                } else
                {
                    ProfileManager.getInstance().writeProfileToStorage(messageText, "", false);
                    ProfileManager.getInstance().setCurrentProfile(messageText);
                    ProfileManager.getInstance().saveProfile();
                    ProfileManager.getInstance().loadProfile();
                    game.setScreen(game.getScreenType(BludBourne.ScreenType.MAIN_GAME));
                }
                return true;
            }
        });

        backButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                game.setScreen(game.getScreenType(BludBourne.ScreenType.MAIN_MENU));
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
        if(delta == 0) return;

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
        stage.clear();
        stage.dispose();
    }
}
