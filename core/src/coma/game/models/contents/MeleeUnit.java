package coma.game.models.contents;

import coma.game.Resources;
import coma.game.components.Animator;

/**
 * Melee unit class.
 */
final public class MeleeUnit extends Unit {

    public static final int[][] stats = {
            { 120, 40, 40, 100 },
            { 190, 60, 40, 180 },
            { 280, 80, 40, 360 },
            { 420, 115, 40, 820 }
    };

    public MeleeUnit(final int era, final int[] s) {
        super(new Animator(Resources.meleeUnitImages[era - 1]), s[0], s[1], s[2], s[3], era);
        this.attackSound = Resources.meleeHitSounds[era - 1];
    }

    public short GetDeploymentDelay() {
        return 100;
    }
}