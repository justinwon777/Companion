package com.github.justinwon777.humancompanions.entity.ai;

import com.github.justinwon777.humancompanions.entity.AbstractHumanCompanionEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class EatGoal extends Goal {
    protected final AbstractHumanCompanionEntity companion;
    ItemStack food = ItemStack.EMPTY;

    public EatGoal(AbstractHumanCompanionEntity entity) {
        companion = entity;
    }

    public boolean canUse() {
        if (companion.getHealth() < companion.getMaxHealth()) {
            food = companion.checkFood();
            return !food.isEmpty();
        }
        return false;
    }

    public void start() {
        companion.setItemSlot(EquipmentSlot.OFFHAND, food);
        companion.startUsingItem(InteractionHand.OFF_HAND);
        companion.setEating(true);
    }

    public void stop() {
        companion.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        companion.setEating(false);
    }

    public void tick () {
        if (companion.getHealth() < companion.getMaxHealth()) {
            food = companion.checkFood();
            if (!food.isEmpty()) {
                start();
            }
        }
    }
}
