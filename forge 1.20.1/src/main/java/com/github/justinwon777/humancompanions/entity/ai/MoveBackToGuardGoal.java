package com.github.justinwon777.humancompanions.entity.ai;

import com.github.justinwon777.humancompanions.entity.AbstractHumanCompanionEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class MoveBackToGuardGoal extends Goal {

    public AbstractHumanCompanionEntity companion;
    public Vec3 patrolVec;

    public MoveBackToGuardGoal(AbstractHumanCompanionEntity p_25990_) {
        this.companion = p_25990_;
    }

    public boolean canUse() {
        if (this.companion.getPatrolPos() == null || !companion.isGuarding()) {
            return false;
        }
        this.patrolVec = Vec3.atBottomCenterOf(this.companion.getPatrolPos());
        Vec3 currentVec = Vec3.atBottomCenterOf(this.companion.blockPosition());
        if (patrolVec.distanceTo(currentVec) <= 1) {
            return false;
        }
        return true;
    }

    public boolean canContinueToUse() {
        return this.canUse();
    }

    public void tick() {
        if (companion.getTarget() == null) {
            companion.getNavigation().moveTo(patrolVec.x, patrolVec.y, patrolVec.z, 1.0);
        }
    }

}