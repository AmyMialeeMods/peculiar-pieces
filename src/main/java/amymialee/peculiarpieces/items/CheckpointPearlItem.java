package amymialee.peculiarpieces.items;

import amymialee.peculiarpieces.PeculiarPieces;
import amymialee.peculiarpieces.util.ExtraPlayerDataWrapper;
import amymialee.peculiarpieces.util.WarpInstance;
import amymialee.peculiarpieces.util.WarpManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CheckpointPearlItem extends Item {
    public CheckpointPearlItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user instanceof ExtraPlayerDataWrapper checkPlayer) {
            Vec3d checkpointPos = checkPlayer.getCheckpointPos();
            if (checkpointPos != null) {
                WarpManager.queueTeleport(WarpInstance.of(user).position(checkpointPos).particles());
                user.getItemCooldownManager().set(this, 4);
                user.sendMessage(Text.translatable("%s.checkpoint_returned".formatted(PeculiarPieces.MOD_ID)).formatted(Formatting.GRAY), true);
                return TypedActionResult.success(user.getStackInHand(hand));
            }
        }
        return super.use(world, user, hand);
    }
}