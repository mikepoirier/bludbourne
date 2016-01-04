package com.packtpub.libgdx.bludbourne.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.packtpub.libgdx.bludbourne.Utility;

/**
 * Created by blindweasel on 1/3/16.
 */
public class StatusUI extends Window
{
    private static final String HP_BAR = "HP_Bar";
    private static final String BAR = "Bar";
    private static final String MP_BAR = "MP_Bar";
    private static final String XP_BAR = "XP_Bar";
    private static final Vector2 BAR_POSITION = new Vector2(3, 6);
    private Image hpBar;
    private Image mpBar;
    private Image xpBar;

    private ImageButton inventoryButton;

    private int levelVal = 1;
    private int goldVal = 0;
    private int hpVal = 50;
    private int mpVal = 50;
    private int xpVal = 0;

    public StatusUI()
    {
        super("stats", Utility.STATUS_UI_SKIN);

        WidgetGroup group = new WidgetGroup();
        WidgetGroup group2 = new WidgetGroup();
        WidgetGroup group3 = new WidgetGroup();

        hpBar = new Image(Utility.STATUS_UI_TEXTURE_ATLAS.findRegion(HP_BAR));
        Image bar = new Image(Utility.STATUS_UI_TEXTURE_ATLAS.findRegion(BAR));
        mpBar = new Image(Utility.STATUS_UI_TEXTURE_ATLAS.findRegion(MP_BAR));
        Image bar2 = new Image(Utility.STATUS_UI_TEXTURE_ATLAS.findRegion(BAR));
        xpBar = new Image(Utility.STATUS_UI_TEXTURE_ATLAS.findRegion(XP_BAR));
        Image bar3 = new Image(Utility.STATUS_UI_TEXTURE_ATLAS.findRegion(BAR));

        Label hpLabel = new Label(" hp:", Utility.STATUS_UI_SKIN);
        Label hp = new Label(String.valueOf(hpVal), Utility.STATUS_UI_SKIN);
        Label mpLabel = new Label(" mp:", Utility.STATUS_UI_SKIN);
        Label mp = new Label(String.valueOf(mpVal), Utility.STATUS_UI_SKIN);
        Label xpLabel = new Label(" xp:", Utility.STATUS_UI_SKIN);
        Label xp = new Label(String.valueOf(xpVal), Utility.STATUS_UI_SKIN);
        Label levelLabel = new Label(" lv:", Utility.STATUS_UI_SKIN);
        Label levelValLabel = new Label(String.valueOf(levelVal), Utility.STATUS_UI_SKIN);
        Label goldLabel = new Label(" gp:", Utility.STATUS_UI_SKIN);
        Label goldValLabel = new Label(String.valueOf(goldVal), Utility.STATUS_UI_SKIN);

        inventoryButton = new ImageButton(Utility.STATUS_UI_SKIN, "inventory-button");
        inventoryButton.getImageCell().size(32,32);

        hpBar.setPosition(BAR_POSITION.x, BAR_POSITION.y);
        mpBar.setPosition(BAR_POSITION.x, BAR_POSITION.y);
        xpBar.setPosition(BAR_POSITION.x, BAR_POSITION.y);

        group.addActor(bar);
        group.addActor(hpBar);
        group2.addActor(bar2);
        group2.addActor(mpBar);
        group3.addActor(bar3);
        group3.addActor(xpBar);

        defaults().expand().fill();

        pad(this.getPadTop() + 10, 10, 10, 10);

        add();
        add();
        add(inventoryButton).align(Align.right);
        row();

        add(group).size(bar.getWidth(), bar.getHeight());
        add(hpLabel);
        add(hp).align(Align.left);
        row();

        add(group2).size(bar2.getWidth(), bar2.getHeight());
        add(mpLabel);
        add(mp).align(Align.left);
        row();

        add(group3).size(bar3.getWidth(), bar3.getHeight());
        add(xpLabel);
        add(xp).align(Align.left);
        row();

        add(levelLabel).align(Align.left);
        add(levelValLabel).align(Align.left);
        row();
        add(goldLabel);
        add(goldValLabel).align(Align.left);

        this.pack();
    }

    public ImageButton getInventoryButton()
    {
        return inventoryButton;
    }
}
