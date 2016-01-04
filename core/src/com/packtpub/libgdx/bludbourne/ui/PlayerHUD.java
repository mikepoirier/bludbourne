package com.packtpub.libgdx.bludbourne.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packtpub.libgdx.bludbourne.Entity;
import com.packtpub.libgdx.bludbourne.InventoryItem;
import com.packtpub.libgdx.bludbourne.InventoryItem.ItemTypeID;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;
import com.packtpub.libgdx.bludbourne.profile.ProfileObserver;

/**
 * Created by blindweasel on 1/2/16.
 */
public class PlayerHUD implements Screen, ProfileObserver
{
    public static final String PLAYER_EQUIP_INVENTORY = "playerEquipInventory";
    public static final String PLAYER_INVENTORY = "playerInventory";
    private Stage stage;
    private Viewport viewport;
    private StatusUI statusUI;
    private InventoryUI inventoryUI;
    private Camera camera;
    private Entity player;

    public PlayerHUD(Camera camera, Entity player)
    {
        this.camera = camera;
        this.player = player;
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);

        statusUI = new StatusUI();
        statusUI.setVisible(true);
        statusUI.setPosition(0,0);

        inventoryUI = new InventoryUI();
        inventoryUI.setMovable(false);
        inventoryUI.setVisible(false);
        inventoryUI.setPosition(stage.getWidth() / 2, 0);

        stage.addActor(statusUI);
        stage.addActor(inventoryUI);

        Array<Actor> actors = inventoryUI.getInventoryActors();
        for(Actor actor : actors)
        {
            stage.addActor(actor);
        }

        ImageButton inventoryButton = statusUI.getInventoryButton();
        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                inventoryUI.setVisible(!inventoryUI.isVisible());
            }
        });
    }

    public Stage getStage()
    {
        return stage;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }

    @Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event)
    {
        switch(event)
        {
            case PROFILE_LOADED:
                Array<InventoryItemLocation> inventory = profileManager.getProperty(PLAYER_INVENTORY, Array.class);
                if(inventory != null && inventory.size > 0)
                {
                    inventoryUI.populateInventory(inventoryUI.getInventorySlotTable(), inventory);
                } else
                {
                    Array<ItemTypeID> items = player.getEntityConfig().getInventory();
                    Array<InventoryItemLocation> itemLocations = new Array<>();
                    for(int i = 0; i < items.size; i++)
                    {
                        itemLocations.add(new InventoryItemLocation(i, items.get(i).toString(), 1));
                    }
                    inventoryUI.populateInventory(inventoryUI.getInventorySlotTable(), itemLocations);
                }

                Array<InventoryItemLocation> equipInventory = profileManager.getProperty(PLAYER_EQUIP_INVENTORY, Array.class);
                if(equipInventory != null && equipInventory.size > 0)
                {
                    inventoryUI.populateInventory(inventoryUI.getEquipSlotsTable(), equipInventory);
                }
                break;
            case SAVING_PROFILE:
                profileManager.setProperty(PLAYER_INVENTORY, inventoryUI.getInventory(inventoryUI.getInventorySlotTable()));
                profileManager.setProperty(PLAYER_EQUIP_INVENTORY, inventoryUI.getInventory(inventoryUI.getEquipSlotsTable()));
                break;
        }
    }
}
