package coma.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import coma.game.components.Canvas;
import coma.game.components.Image;
import coma.game.components.ImageRegion;
import coma.game.components.TextBox;
import coma.game.utils.Asset;
import coma.game.controllers.UIController;

public class Resources {
    public static Canvas devLogo;
    public static Canvas gameLogo;
    public static Canvas creditBanner;
    public static Canvas playBtn;
    public static Canvas creditBtn;
    public static Canvas how2playBtn;
    public static Canvas how2playBanner1;
    public static Canvas how2playBanner2;
    public static Canvas musicBtn;
    public static Canvas speedBtn;
    public static Canvas mode1;
    public static Canvas mode2;
    public static Canvas mode3;
    public static Canvas modeBanner;
    public static Canvas startBtn;
    public static Canvas menuBtn;
    public static Canvas restartBtn;
    public static Canvas unit1;
    public static Canvas unit2;
    public static Canvas unit3;
    public static Canvas unit4;
    public static Canvas unit5;
    public static Canvas unitUl;
    public static Canvas cashIcon;
    public static Canvas xpIcon;
    public static final Canvas[] unitQueueIcons = new Canvas[10];
    public static Canvas healthBar;
    public static Canvas healthBarL;
    public static Canvas healthBarR;
    public static Canvas queueBar;
    public static Canvas unitQueueBarInner;
    public static Canvas ultimateBarInner;
    public static Canvas victoryBanner;
    public static Canvas defeatBanner;

    public static BitmapFont bitmapFont;
    public static BitmapFont bitmapFont2;
    public static BitmapFont bitmapFont3;
    public static TextBox cashText;
    public static TextBox xpText;
    public static TextBox unitCapText;
    public static TextBox unitDescText;
    public static final TextBox[] unitTexts = new TextBox[5];

    public static Image bg;
    public static final Image[] strongholdImages = new Image[4];
    public static final Image[] turretImages = new Image[4];
    public static final Image[][] meleeUnitImages = new Image[4][7];
    public static final Image[][] rangedUnitImages = new Image[4][7];
    public static final Image[][] cavalryUnitImages = new Image[4][7];
    public static final Image[] unitQueueImages = new Image[3];
    public static final Image[] ultimateBannerImages = new Image[4];
    public static final Image[] ultimateImages = new Image[4];
    public static final Image[] emergencyUltimateImages = new Image[6];
    public static ImageRegion explosionImageRegion;
    public static Image ulPlane;
    public static Image unitHealthBar;
    public static Image unitHealthBarInner;

    public static Image unit5UlImage;
    public static Image unit5EraImage;

    public static Music themeMusic;
    public static Sound devLogoSound;
    public static Sound startSound;
    public static Sound menuClickSound;
    public static Sound newEraSound;
    public static Sound winSound;
    public static Sound loseSound;
    public static final Sound[] meleeHitSounds = new Sound[4];
    public static final Sound[] rangedHitSounds = new Sound[4];
    public static final Sound[] cavalryHitSounds = new Sound[4];
    public static final Sound[] explosionSounds = new Sound[1];
    public static final Sound[] cavalryDieSounds = new Sound[3];
    public static Sound meleeDie1;
    public static Sound unitCallSound;
    public static Sound ulPlaneSound;
    public static Sound emergencyUlSound;

    public static void load() {
        // set ui position and group module
        bg = new Image("game-bg.png");

        for (byte era = 0; era < 4; era++) {
            strongholdImages[era] = new Image("base-era-" + (era + 1) + ".png");
            turretImages[era] = new Image("turret-era-" + (era + 1) + ".png");
            ultimateBannerImages[era] = new Image("unit-ul-" + (era + 1) + ".png");
            ultimateImages[era] = new Image("ultimate-" + (era + 1) + ".png");
        }

        for (byte era = 0; era < 4; era++) {
            for (byte mov = 0; mov < 7; mov++) {
                meleeUnitImages[era][mov] = new Image("melee-unit-era-" + (era + 1) + "-" + (mov + 1) + ".png");
                rangedUnitImages[era][mov] = new Image("ranged-unit-era-" + (era + 1) + "-" + (mov + 1) + ".png");
                cavalryUnitImages[era][mov] = new Image("cavalry-unit-era-" + (era + 1) + "-" + (mov + 1) + ".png");
            }
        }

        for (byte t = 0; t < unitQueueImages.length; t++) {
            unitQueueImages[t] = new Image("unit-queue-" + (t + 1) + ".png");
        }

        explosionImageRegion = new ImageRegion("explosion-region.png", 128, 128, 4, 4);
        ulPlane = new Image("ul-plane.png");
        unitHealthBar = new Image("unit-health-bar.png");
        unitHealthBarInner = new Image("unit-health-bar-inner.png");
        unit5UlImage = new Image("unit-5-ul.png");
        unit5EraImage = new Image("unit-5.png");

        for(byte i = 0; i < emergencyUltimateImages.length; i++){
            emergencyUltimateImages[i] = new Image("emergency-" + (i + 1) + ".png");
        }

        devLogo = new Canvas("dev-logo.png");
        gameLogo = new Canvas("game-logo.png");
        creditBanner = new Canvas("credit.png");
        playBtn = new Canvas("play-btn.png");
        creditBtn = new Canvas("credit-btn.png");
        how2playBtn = new Canvas("how2play-btn.png");
        how2playBanner1 = new Canvas("how2play-page-1.png");
        how2playBanner2 = new Canvas("how2play-page-2.png");
        musicBtn = new Canvas("music-btn.png");
        speedBtn = new Canvas("speed-btn.png");
        mode1 = new Canvas("mode-1.png");
        mode2 = new Canvas("mode-2.png");
        mode3 = new Canvas("mode-3.png");
        modeBanner = new Canvas("mode-selection-banner.png");
        startBtn = new Canvas("start-btn.png");
        menuBtn = new Canvas("menu-btn.png");
        restartBtn = new Canvas("restart-btn.png");
        unit1 = new Canvas("unit-1.png");
        unit2 = new Canvas("unit-2.png");
        unit3 = new Canvas("unit-3.png");
        unit4 = new Canvas("unit-4.png");
        unit5 = new Canvas(unit5EraImage);
        unitUl = new Canvas(ultimateBannerImages[0]);
        cashIcon = new Canvas("cash-icon.png");
        xpIcon = new Canvas("xp-icon.png");

        for (byte i = 0; i < unitQueueIcons.length; i++) {
            unitQueueIcons[i] = new Canvas(unitQueueImages[0].clone());
        }

        healthBar = new Canvas("health-bar.png");
        healthBarL = new Canvas("health-bar-inner.png");
        healthBarR = new Canvas(healthBarL);
        queueBar = new Canvas("queue-bar.png");
        unitQueueBarInner = new Canvas("unit-queue-bar-inner.png");
        ultimateBarInner = new Canvas("ultimate-bar-inner.png");
        victoryBanner = new Canvas("victory.png");
        defeatBanner = new Canvas("defeat.png");

        bitmapFont = Asset.loadBitmapFont("fonts/kefa.fnt", false);
        bitmapFont2 = Asset.loadBitmapFont("fonts/kefa.fnt", false);
        bitmapFont3 = Asset.loadBitmapFont("fonts/kefa.fnt", false);
        cashText = new TextBox(bitmapFont);
        xpText = new TextBox(bitmapFont);
        unitCapText = new TextBox(bitmapFont);
        unitDescText = new TextBox(bitmapFont2);
        for (int i = 0; i < unitTexts.length; i++) {
            unitTexts[i] = new TextBox(bitmapFont3);
        }

        themeMusic = Asset.loadMusic("audio/theme.mp3");

        devLogoSound = Asset.loadSound("audio/dev-logo.mp3");
        startSound = Asset.loadSound("audio/start-game.mp3");
        menuClickSound = Asset.loadSound("audio/menu-click.mp3");
        newEraSound = Asset.loadSound("audio/new-era.mp3");
        winSound = Asset.loadSound("audio/win.mp3");
        loseSound = Asset.loadSound("audio/lose.mp3");
        ulPlaneSound = Asset.loadSound("audio/ul-plane.mp3");
        emergencyUlSound = Asset.loadSound("audio/emergency-ultimate.mp3");

        for (byte era = 0; era < meleeHitSounds.length; era++) {
            meleeHitSounds[era] = Asset.loadSound("audio/melee-hit-" + (era + 1) + ".mp3");
        }

        for (byte era = 0; era < rangedHitSounds.length; era++) {
            rangedHitSounds[era] = Asset.loadSound("audio/ranged-hit-" + (era + 1) + ".mp3");
        }

        for (byte era = 0; era < cavalryHitSounds.length; era++) {
            cavalryHitSounds[era] = Asset.loadSound("audio/cavalry-hit-" + (era + 1) + ".mp3");
        }

        explosionSounds[0] = Asset.loadSound("audio/explosion-1.mp3");
        meleeDie1 = Asset.loadSound("audio/melee-die-1.mp3");
        cavalryDieSounds[0] = Asset.loadSound("audio/cavalry-die-1.mp3");
        cavalryDieSounds[1] = Asset.loadSound("audio/cavalry-die-2.mp3");
        cavalryDieSounds[2] = Asset.loadSound("audio/cavalry-die-3.mp3");
        unitCallSound = Asset.loadSound("audio/call-unit.mp3");
    }

    public static void setup() {

        gameLogo.setPosition("center", 400);
        playBtn.setPosition("center", 300);
        creditBtn.setPosition("center", 180);
        how2playBtn.setPosition("center", 60);
        how2playBanner1.setPosition("center", 0);
        how2playBanner1.isVisible = false;
        how2playBanner2.setPosition("center", 0);
        how2playBanner2.isVisible = false;
        creditBanner.setPosition("center", 5);
        creditBanner.isVisible = false;
        musicBtn.setPosition(886, 14);
        musicBtn.isVisible = false;
        speedBtn.setPosition(14, 14);
        speedBtn.setActive(false);
        modeBanner.setPosition("center", 440);
        mode1.setPosition(70, 200);
        mode2.setPosition("center", 200);
        mode3.setPosition(640, 200);
        startBtn.setPosition("center", 80);
        startBtn.x += -120;
        menuBtn.setPosition("center", 80);
        menuBtn.x += 120;
        restartBtn.setPosition("center", 80);
        restartBtn.x += -120;
        unit1.setPosition(566, 524);
        unit2.setPosition(646, 524);
        unit3.setPosition(726, 524);
        unit4.setPosition(806, 524);
        unit5.setPosition(886, 524);
        unitUl.setPosition(770, 420);
        cashIcon.setPosition(-28, 504);
        cashIcon.setScale(0.25f);
        xpIcon.setPosition(-28, 466);
        xpIcon.setScale(0.25f);
        for (byte i = 0; i < unitQueueIcons.length; i++) {
            unitQueueIcons[i].setPosition(566 + 20 * i, 452);
            unitQueueIcons[i].isVisible = false;
        }
        healthBar.setPosition("center", 14);
        healthBarL.setPosition(318, 18);
        healthBarR.setPosition(485, 18);
        healthBarR.setRotation(180);
        queueBar.setPosition(566, 482);
        unitQueueBarInner.setPosition(567, 490);
        ultimateBarInner.setPosition(567,483);
        victoryBanner.setPosition("center", 180);
        defeatBanner.setPosition("center", 180);

        cashText.setPosition(64, 580);
        cashText.textContent = "0";
        xpText.setPosition(64, 540);
        xpText.textContent = "0";
        unitCapText.setPosition(22, 500);
        unitCapText.textContent = "0/NaN";
        unitDescText.setPosition(248, 580);
        unitDescText.textContent = "";

        for (int i = 0; i < unitTexts.length; i++) {
            unitTexts[i].setPosition(576 + (i * 80), 516);
            unitTexts[i].textContent = "0";
        }

        bitmapFont2.getData().setScale(0.8f);
        bitmapFont3.getData().setScale(0.4f);

        UIController.addBoxModule("start-menu", gameLogo, playBtn, creditBtn, how2playBtn);
        UIController.getBoxModule("start-menu").setVisibility(false);
        UIController.addBoxModule("mode-selection-menu", modeBanner, mode1, mode2, mode3, startBtn, menuBtn);
        UIController.getBoxModule("mode-selection-menu").setVisibility(false);
        UIController.addBoxModule("in-game-menu", speedBtn, unit1, unit2, unit3, unit4, unit5, unitUl,
                cashIcon, xpIcon, cashText, xpText, unitCapText, unitDescText, unitTexts[0],
                unitTexts[1], unitTexts[2], unitTexts[3], unitTexts[4], healthBar, healthBarL, healthBarR, queueBar,
                unitQueueBarInner, ultimateBarInner, unitQueueIcons[0], unitQueueIcons[1], unitQueueIcons[2],
                unitQueueIcons[3], unitQueueIcons[4], unitQueueIcons[5], unitQueueIcons[6], unitQueueIcons[7],
                unitQueueIcons[8], unitQueueIcons[9]);
        UIController.getBoxModule("in-game-menu").setVisibility(false);
        UIController.addBoxModule("game-over-menu", restartBtn, menuBtn, victoryBanner, defeatBanner);
        UIController.getBoxModule("game-over-menu").setVisibility(false);

        // set sounds and music
        themeMusic.setLooping(true);
        themeMusic.setVolume(MainGame.THEME_VOLUME);
    }
}
