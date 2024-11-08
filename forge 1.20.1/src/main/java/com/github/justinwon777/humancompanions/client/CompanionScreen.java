package com.github.justinwon777.humancompanions.client;

import com.github.justinwon777.humancompanions.HumanCompanions;
import com.github.justinwon777.humancompanions.container.CompanionContainer;
import com.github.justinwon777.humancompanions.core.PacketHandler;
import com.github.justinwon777.humancompanions.entity.AbstractHumanCompanionEntity;
import com.github.justinwon777.humancompanions.entity.Arbalist;
import com.github.justinwon777.humancompanions.entity.Archer;
import com.github.justinwon777.humancompanions.entity.Knight;
import com.github.justinwon777.humancompanions.networking.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@OnlyIn(Dist.CLIENT)
public class CompanionScreen extends AbstractContainerScreen<CompanionContainer> implements MenuAccess<CompanionContainer> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(HumanCompanions.MOD_ID,
        "textures/inventory.png");
    private static final ResourceLocation ALERT_BUTTON = new ResourceLocation(HumanCompanions.MOD_ID, "textures/alertbutton.png");
    private static final ResourceLocation HUNTING_BUTTON = new ResourceLocation(HumanCompanions.MOD_ID, "textures" +
            "/huntingbutton.png");
    private static final ResourceLocation PATROL_BUTTON = new ResourceLocation(HumanCompanions.MOD_ID, "textures" +
            "/patrolbutton.png");
    private static final ResourceLocation CLEAR_BUTTON = new ResourceLocation(HumanCompanions.MOD_ID, "textures" +
            "/clearbutton.png");
    private static final ResourceLocation STATIONERY_BUTTON = new ResourceLocation(HumanCompanions.MOD_ID, "textures" +
            "/stationerybutton.png");
    private static final ResourceLocation RELEASE_BUTTON = new ResourceLocation(HumanCompanions.MOD_ID, "textures" +
            "/releasebutton.png");
    private final int containerRows;
    private final AbstractHumanCompanionEntity companion;
    private CompanionButton alertButton;
    private CompanionButton huntingButton;
    private CompanionButton patrolButton;
    private CompanionButton clearButton;
    private CompanionButton stationeryButton;
    private CompanionButton releaseButton;
    DecimalFormat df = new DecimalFormat("#.#");
    int sidebarx;
    int rowHeight;
    int colwidth;
    int row1;
    int row2;
    int row3;
    int col1;
    int col2;

    public CompanionScreen(CompanionContainer p_98409_, Inventory p_98410_,
                           AbstractHumanCompanionEntity companion) {
        super(p_98409_, p_98410_, companion.getName());
        this.companion = companion;
        this.passEvents = false;
        this.containerRows = p_98409_.getRowCount();
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
        this.imageWidth = 226;
        df.setRoundingMode(RoundingMode.CEILING);
        sidebarx = 174;
        rowHeight = 15;
        colwidth = 19;
    }

    @Override
    public void render(PoseStack p_98418_, int p_98419_, int p_98420_, float p_98421_) {
        this.renderBackground(p_98418_);
        super.render(p_98418_, p_98419_, p_98420_, p_98421_);
        this.renderTooltip(p_98418_, p_98419_, p_98420_);
    }

    @Override
    protected void renderBg(PoseStack p_98413_, float p_98414_, int p_98415_, int p_98416_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_98413_, i, j, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        this.blit(p_98413_, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

    @Override
    protected void init() {
        super.init();
        row1 = topPos + 66;
        row2 = row1 + rowHeight;
        row3 = row2 + rowHeight;
        col1 = leftPos + sidebarx + 3;
        col2 = col1 + colwidth;
        this.alertButton = addRenderableWidget(new CompanionButton("alert", col1, row1, 16,
                12, 0
                , 0, 13,
                ALERT_BUTTON,
                btn -> {
                    PacketHandler.INSTANCE.sendToServer(new SetAlertPacket(companion.getId()));
        }));
        this.huntingButton = addRenderableWidget(new CompanionButton("hunting", col2, row1,
                16,
                12, 0, 0,13,
                HUNTING_BUTTON,
                btn -> {
                    PacketHandler.INSTANCE.sendToServer(new SetHuntingPacket(companion.getId()));
                }));
        this.patrolButton = addRenderableWidget(new CompanionButton("patrolling", col1, row2,
                16,
                12,
                0, 0
                ,13,
                PATROL_BUTTON,
                btn -> {
                    PacketHandler.INSTANCE.sendToServer(new SetPatrolingPacket(companion.getId()));
                }));
        if (companion instanceof Archer || companion instanceof Arbalist) {
            this.stationeryButton = addRenderableWidget(new CompanionButton("stationery", col2,
                    row2,
                    16,
                    12,
                    0, 0
                    , 13,
                    STATIONERY_BUTTON,
                    btn -> {
                        PacketHandler.INSTANCE.sendToServer(new SetStationeryPacket(companion.getId()));
                    }));
        }
        this.clearButton = addRenderableWidget(new CompanionButton("clear", leftPos + sidebarx + 5, row3, 31,
                12, 0, 0
                ,13,
                CLEAR_BUTTON,
                btn -> {
                    PacketHandler.INSTANCE.sendToServer(new ClearTargetPacket(companion.getId()));
                }));
        this.releaseButton = addRenderableWidget(new CompanionButton("release", leftPos + sidebarx + 3, topPos + 148,
                34,
                12, 0, 0
                ,13,
                RELEASE_BUTTON,
                btn -> {
                    PacketHandler.INSTANCE.sendToServer(new ReleasePacket(companion.getId()));
                    this.onClose();
                }));
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
        int classHeight = this.titleLabelY + 14;
        int classLeft = sidebarx + 4;
        MutableComponent classTitle = Component.literal("Class");
        MutableComponent healthTitle = Component.literal("Health");
        MutableComponent health =
                Component.literal(df.format(companion.getHealth()) + "/" + (int) companion.getMaxHealth());

        this.font.draw(pPoseStack, classTitle.withStyle(ChatFormatting.UNDERLINE), sidebarx + 4, this.titleLabelY + 3,
                4210752);
        if (companion instanceof Arbalist) {
            this.font.draw(pPoseStack, "Arbalist", classLeft, classHeight, 4210752);
        } else if (companion instanceof Archer) {
            this.font.draw(pPoseStack, "Archer", classLeft, classHeight, 4210752);
        } else if (companion instanceof Knight) {
            this.font.draw(pPoseStack, "Knight", classLeft, classHeight, 4210752);
        } else {
            this.font.draw(pPoseStack, "Axe", classLeft, classHeight, 4210752);
        }

        this.font.draw(pPoseStack, healthTitle.withStyle(ChatFormatting.UNDERLINE), sidebarx + 4, this.titleLabelY + 26,
                4210752);
        this.font.draw(pPoseStack, health, sidebarx + 4, this.titleLabelY + 37, 4210752);

        this.font.draw(pPoseStack, "Level " + companion.getExpLvl(), sidebarx, this.titleLabelY + 49,
                4210752);
    }

    @Override
    protected void renderTooltip(PoseStack stack, int x, int y) {
        super.renderTooltip(stack, x, y);
        if (this.alertButton.isHoveredOrFocused()) {
            List<Component> tooltips = new ArrayList<>();
            if (this.companion.isAlert()) {
                tooltips.add(Component.literal("Alert mode: On"));
            } else {
                tooltips.add(Component.literal("Alert mode: Off"));
            }
            tooltips.add(Component.literal("Attacks nearby hostile mobs").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));

            this.renderTooltip(stack, tooltips, Optional.empty(), x, y);
        }
        if (this.huntingButton.isHoveredOrFocused()) {
            List<Component> tooltips = new ArrayList<>();
            if (this.companion.isHunting()) {
                tooltips.add(Component.literal("Hunting mode: On"));
            } else {
                tooltips.add(Component.literal("Hunting mode: Off"));
            }
            tooltips.add(Component.literal("Attacks nearby mobs for food").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));

            this.renderTooltip(stack, tooltips, Optional.empty(), x, y);
        }

        if (this.patrolButton.isHoveredOrFocused()) {
            List<Component> tooltips = new ArrayList<>();
            if (this.companion.isFollowing()) {
                tooltips.add(Component.literal("Follow"));
                tooltips.add(Component.literal("Follows you").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
            } else if (this.companion.isPatrolling()) {
                tooltips.add(Component.literal("Patrol"));
                tooltips.add(Component.literal("Patrols a 4 block radius").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
            } else {
                tooltips.add(Component.literal("Guard"));
                tooltips.add(Component.literal("Stands at its position ready for action").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
            }

            this.renderTooltip(stack, tooltips, Optional.empty(), x, y);
        }

        if (this.clearButton.isHoveredOrFocused()) {
            List<Component> tooltips = new ArrayList<>();
            tooltips.add(Component.literal("Clear target"));
            tooltips.add(Component.literal("Useful if it gets stuck attacking").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));

            this.renderTooltip(stack, tooltips, Optional.empty(), x, y);
        }

        if (this.releaseButton.isHoveredOrFocused()) {
            List<Component> tooltips = new ArrayList<>();
            tooltips.add(Component.literal("Release Companion"));
            tooltips.add(Component.literal("Releases companion from your command. It can be tamed again.").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));

            this.renderTooltip(stack, tooltips, Optional.empty(), x, y);
        }

        if (companion instanceof Archer || companion instanceof Arbalist) {
            if (this.stationeryButton.isHoveredOrFocused()) {
                List<Component> tooltips = new ArrayList<>();
                if (this.companion.isStationery()) {
                    tooltips.add(Component.literal("Stationery: On"));
                } else {
                    tooltips.add(Component.literal("Stationery: Off"));
                }
                tooltips.add(Component.literal("Companion will not move while attacking in guard mode").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));

                this.renderTooltip(stack, tooltips, Optional.empty(), x, y);
            }
        }
    }

    class CompanionButton extends ImageButton {

        private String name;

        public CompanionButton(String name, int p_94269_, int p_94270_, int p_94271_, int p_94272_, int p_94273_,
                               int p_94274_,
                               int p_94275_, ResourceLocation p_94276_,
                               Button.OnPress p_94277_) {
            super(p_94269_, p_94270_, p_94271_, p_94272_, p_94273_, p_94274_, p_94275_, p_94276_, p_94277_);
            this.name = name;
        }

        @Override
        public void renderButton(PoseStack p_94282_, int p_94283_, int p_94284_, float p_94285_) {
            if (this.name.equals("alert")) {
                if (CompanionScreen.this.companion.isAlert()) {
                    this.xTexStart = 0;
                } else {
                    this.xTexStart = 17;
                }
            } else if (this.name.equals("hunting")) {
                if (CompanionScreen.this.companion.isHunting()) {
                    this.xTexStart = 0;
                } else {
                    this.xTexStart = 17;
                }
            } else if (this.name.equals("patrolling")) {
                if (CompanionScreen.this.companion.isFollowing()) {
                    this.xTexStart = 0;
                } else if (CompanionScreen.this.companion.isPatrolling()){
                    this.xTexStart = 17;
                } else {
                    this.xTexStart = 34;
                }
            } else if (this.name.equals("stationery")) {
                if (CompanionScreen.this.companion.isStationery()) {
                    this.xTexStart = 0;
                } else {
                    this.xTexStart = 17;
                }
            }
            RenderSystem.enableBlend();
            super.renderButton(p_94282_, p_94283_, p_94284_, p_94285_);
            RenderSystem.disableBlend();
        }
    }
}