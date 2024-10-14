package amymialee.peculiarpieces.mixin;

import amymialee.peculiarpieces.PeculiarPieces;
import amymialee.peculiarpieces.PeculiarPiecesClient;
import amymialee.peculiarpieces.registry.PeculiarItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.GameRules;
import net.minecraft.server.MinecraftServer;

@SuppressWarnings("ConstantConditions")
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract boolean isLeashed();

    @Shadow protected abstract void updateLeash();

    @Shadow public abstract void detachLeash(boolean sendPacket, boolean dropItem);

    @Inject(method = "canBeLeashedBy", at = @At("HEAD"), cancellable = true)
    public void PeculiarPieces$CreativeLeashes(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!this.isLeashed()) {
            boolean isNormallyLeashable = !this.isLeashed() && !(this instanceof Monster);
            boolean re = isNormallyLeashable || (this.getWorld().isClient() ? PeculiarPiecesClient.StrongerLeadsClientRule : getWorld().getGameRules().get(PeculiarPieces.StrongerLeadsGamerule).get());
            cir.setReturnValue(re);
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void PeculiarPieces$InteractMobHead(CallbackInfoReturnable<ActionResult> cir) {
        if ((Object) this instanceof SlimeEntity slime) {
            if (slime.getSize() == 1) {
                if (!this.getWorld().isClient) {
                    if (this.dropStack(new ItemStack(PeculiarItems.SLIME)) != null) {
                        this.discard();
                        cir.setReturnValue(ActionResult.SUCCESS);
                    }
                }
            }
        }
    }
}