package coma.game.models;

import coma.game.MainGame;
import coma.game.Resources;
import coma.game.models.contents.*;
import coma.game.utils.Mathf;
import coma.game.views.Renderer;
import org.w3c.dom.ranges.RangeException;

final public class GameBot extends Player {

    // constants
    private final short SPAWN_POSITION_X;

    public byte difficulty = 1;
    private boolean isWaking = false;
    private byte decisionDelay = 120;
    private byte state = 1;

    public static final byte DECISION_DELAY = 120;

    public GameBot() {
        this.SPAWN_POSITION_X = Player.RIGHT_STRONGHOLD_POSITION_X + 210;
        this.stronghold.image.flipHorizontal();
        this.stronghold.image.setPosition(Player.RIGHT_STRONGHOLD_POSITION_X, Player.STRONGHOLD_POSITION_Y);
    }

    public static short getUltimateDelay(final byte difficulty) {
        switch (difficulty) {
            case 1: return 6000;
            case 2: return 5000;
            case 3: return 4000;
            default: throw new RangeException((short) 0, "Wrong parameter input.");
        }
    }


    @Override
    public void spawnUnit(final Unit u) {
        if (u == null) return;

        this.units.add(u);
        u.image.flipHorizontal();
        u.spawnAt(this.SPAWN_POSITION_X - u.image.naturalWidth, this.SPAWN_POSITION_Y);
    }

    @Override
    public boolean buildTurret(final Turret t) {
        // check if can build
        if (t == null) {
            boolean canBuildTurret = this.cash >= Turret.getEra(this.era).cost && this.turrets.size() < 2;

            for (final Turret at : this.turrets) {
                if (at.era < this.era && this.cash >= Turret.getEra(this.era).cost - at.cost) {
                    canBuildTurret = true;
                    break;
                }
            }

            return canBuildTurret;
        }

        // check buying contition from existing turrets
        for (Turret at : this.turrets) {
            if (at.era < this.era && this.cash >= Turret.getEra(this.era).cost - at.cost) {
                this.cash -= t.cost - at.cost;

                // replace
                at.replaceWith(t);

                Resources.unitCallSound.play();

                return true;
            }
        }

        if (this.cash >= t.cost && this.turrets.size() < 2) {
            this.cash -= t.cost;
            this.turrets.add(t);

            t.image.flipHorizontal();
            t.image.setPosition(1935, this.turrets.size() == 1 ? 260 : 340);
            Renderer.addComponents(t.image);

            return true;
        }

        return false;
    }

    @Override
    public boolean useUltimate(final Player target) {
        if (target == null) return false;

        if (this.ultimateDelay <= 0) {
            this.ultimateCaller = new Ultimate(this, target,  this.era, true);
            this.ultimateDelay = GameBot.getUltimateDelay(this.difficulty);

            if(this.era == 4) this.time2end++;

            return true;
        }

        return false;
    }

    @Override
    public boolean useEmergencyUltimate(final Player target) {
        if (target == null) return false;

        if (this.era >= 4 && this.xp >= EmergencyUltimate.REQUIRED_XP) {
            this.xp -= EmergencyUltimate.REQUIRED_XP;
            this.emergencyUltimateCaller = new EmergencyUltimate(this, target, true);

            return true;
        }

        return false;
    }

    @Override
    public boolean upgradeStronghold() {
        if (this.era < 4 && this.xp >= Stronghold.getRequiredXp(this.era)) {
            this.era++;

            this.stronghold.upgradeTo(this.era);

            return true;
        }

        return false;
    }

    @Override
    public void updateAfter(final int rawCost) {
        switch (this.difficulty) {
            case 1:
            case 2: {
                this.cash += (int) (rawCost * (1.0f + 0.4f * (this.difficulty / (0.3 * this.era) + this.time2end)));
            } break;
            case 3: {
                this.cash += (int) (rawCost * (1.0f + 0.4f * this.difficulty));
            }
        }

        this.xp += (int) (rawCost * Math.random() * (0.2f + 0.05f * this.difficulty) + rawCost * (0.05f + 0.05 * this.difficulty));
    }

    @Override
    public void setup() {
        this.cash = 400;
        this.xp = 0;
        this.deploymentDelay = 0;
        this.ultimateDelay = GameBot.getUltimateDelay(this.difficulty);
        this.stronghold.setEra(this.era = 1);
    }

    public void awake() {
        // init
        if (!this.isWaking) {
            this.ultimateDelay = GameBot.getUltimateDelay(this.difficulty);
            this.isWaking = true;
        }

        if (this.decisionDelay < 0) {

            this.counterPlayerTactic();
            this.processTurretSetting();
            this.upgradeStronghold();

            if (this.difficulty == 3) this.useEmergencyUltimate(MainGame.user);

            if (MainGame.user.ultimateDelay <= 1000) {
                if (this.units.size() + this.deploymentQueue.size <= MainGame.user.units.size() + (3 - this.difficulty)) // prevent ultimate clear field
                    this.botDecision();
            }
            else {
                this.botDecision();
            }

            if ((MainGame.user.units.size() >= 3 || this.isBaseHit()) && this.difficulty > 1) this.useUltimate(MainGame.user);

            this.decisionDelay = GameBot.DECISION_DELAY;
        }
        else {
            this.decisionDelay -= MainGame.deltaTime;
        }
    }

    private boolean isBaseHit(){
        if(MainGame.user.units.size() > 1) {
            final Unit spy = MainGame.user.units.get(0);
            return spy.isReachedMax();
        }
        else return false;
    }

    private void botDecision() {
        switch (this.getCalculatedDecisionState()) { // << old: this.diffulty
            case 1:
                this.level1Automation();
                break;
            case 2:
                this.level2Automation();
                break;
            case 3:
                this.level3Automation();
                break;
        }
    }

    public byte getCalculatedDecisionState() {

        if (this.cash >= CavalryUnit.stats[this.era - 1][3] * 5) state = 3;
        else if (this.cash >= CavalryUnit.stats[this.era - 1][3] * 3) state = 2;
        else state = 1;

        return this.state;
    }

    private void level1Automation() { //less money
        if (!this.isWaking) return;

        // game bot decision fired >> write decision commands here
        if (this.units.size() < Player.MAX_UNIT) {

            int idx = Mathf.calRange(0,100);      // random idx for choosing unit

            if (this.hasMeleeInFront()) {
                this.processStrategy(idx);
            }
            else{
                if (idx >= 0 && idx < 45) {
                    this.deployUnit(new MeleeUnit(this.era, MeleeUnit.stats[this.era - 1]));
                }
                else if (idx >= 45 && idx < 90) {
                    this.deployUnit(new RangedUnit(this.era, RangedUnit.stats[this.era - 1]));
                }
                else {
                    this.deployUnit(new CavalryUnit(this.era, CavalryUnit.stats[this.era - 1]));
                }
            }
        }
    }

    private void level2Automation() { // fair money
        if (!this.isWaking) return;

        // game bot decision fired >> write decision commands here
        if (this.units.size() < Player.MAX_UNIT) {
            int idx = (int)(Math.random() * 100);      // random idx for choosing unit

            if (this.hasMeleeInFront()) {
                this.processStrategy(idx);
            }
            else{
                if (idx >= 0 && idx < 35) {
                    this.deployUnit(new MeleeUnit(this.era, MeleeUnit.stats[this.era - 1]));
                }
                else if (idx >= 35 && idx < 70) {
                    this.deployUnit(new RangedUnit(this.era, RangedUnit.stats[this.era - 1]));
                }
                else {
                    this.deployUnit(new CavalryUnit(this.era, CavalryUnit.stats[this.era - 1]));
                }
            }
        }
    }

    private void level3Automation() { //more money
        if (!this.isWaking) return;

        // game bot decision fired >> write decision commands here
        int idx = (int)(Math.random() * 100);      // random idx for choosing unit

        if (this.hasMeleeInFront()) {
            this.processStrategy(idx);
        }
        else {
            if (idx >= 0 && idx < 30) {
                this.deployUnit(new MeleeUnit(this.era, MeleeUnit.stats[this.era - 1]));
            }
            else if (idx >= 30 && idx < 60) {
                this.deployUnit(new RangedUnit(this.era, RangedUnit.stats[this.era - 1]));
            }
            else {
                this.deployUnit(new CavalryUnit(this.era, CavalryUnit.stats[this.era - 1]));
            }
        }
    }

    private boolean hasMeleeInFront() {
        if (this.units.size() > 1) {
            final Unit lastU = this.units.get(this.units.size() - 1);
            final Unit secondLastU = this.units.get(this.units.size() - 2);

            return !((lastU instanceof RangedUnit) || (secondLastU instanceof RangedUnit));
        }

        return false;
    }

    private void counterPlayerTactic() { // '322 Fn Code'
        for(int i = 0; i < MainGame.user.units.size() - 2 ; i++){
            Unit tank = MainGame.user.units.get(i);
            Unit ranged1 = MainGame.user.units.get(i + 1);
            Unit ranged2 = MainGame.user.units.get(i + 2);

            if ((tank instanceof CavalryUnit || tank instanceof MeleeUnit)
                    && ranged1 instanceof RangedUnit
                    && ranged2 instanceof RangedUnit){

                if(this.hasMeleeInFront()) {   // not wasting resources for melee infront
                    this.deployUnit(new RangedUnit(this.era, RangedUnit.stats[this.era - 1]));
                    this.deployUnit(new RangedUnit(this.era, RangedUnit.stats[this.era - 1]));
                }
                else{
                    int idx = Mathf.calRange(0,20);

                    if(idx < 10)
                        this.deployUnit(new MeleeUnit(this.era, MeleeUnit.stats[this.era -1]));
                    else
                        this.deployUnit(new CavalryUnit(this.era, CavalryUnit.stats[this.era -1]));

                    this.deployUnit(new RangedUnit(this.era, RangedUnit.stats[this.era - 1]));
                    this.deployUnit(new RangedUnit(this.era, RangedUnit.stats[this.era - 1]));
                }
            }

            else return;
        }
    }

    private void processStrategy(final int rand) {
        switch (this.getCalculatedDecisionState()){
            case 1 :{
                if (rand >= 0 && rand < 50) {
                    this.deployUnit(new MeleeUnit(this.era, MeleeUnit.stats[this.era - 1]));
                }
                else if (rand >= 50 && rand < 80) {
                    this.deployUnit(new CavalryUnit(this.era, CavalryUnit.stats[this.era - 1]));
                }
                else {
                    this.deployUnit(new RangedUnit(this.era, RangedUnit.stats[this.era - 1]));
                }
            }
            case 2 :{
                if (rand >= 0 && rand < 40) {
                    this.deployUnit(new MeleeUnit(this.era, MeleeUnit.stats[this.era - 1]));
                }
                else if (rand >= 40 && rand < 80) {
                    this.deployUnit(new CavalryUnit(this.era, CavalryUnit.stats[this.era - 1]));
                }
                else {
                    this.deployUnit(new RangedUnit(this.era, RangedUnit.stats[this.era - 1]));
                }
            }
            case 3 :{
                if (rand >= 0 && rand < 50) {
                    this.deployUnit(new CavalryUnit(this.era, CavalryUnit.stats[this.era - 1]));
                }
                else if (rand >= 50 && rand < 80) {
                    this.deployUnit(new MeleeUnit(this.era, MeleeUnit.stats[this.era - 1]));
                }
                else {
                    this.deployUnit(new RangedUnit(this.era, RangedUnit.stats[this.era - 1]));
                }
            }
        }
    }

    private void processTurretSetting() {
        if (!this.isWaking) return;

        if (this.units.size() > 3) {
            // unit must be > 4 and cash is enough for build turret and spawn troops in the future
            if (this.cash >= Turret.getEra(this.era).cost + CavalryUnit.stats[this.era - 1][3] * (3 - this.difficulty)) {
                this.buildTurret(Turret.getEra(this.era));
            }
        }
    }

    public void halt() {
        this.isWaking = false;
    }
}
