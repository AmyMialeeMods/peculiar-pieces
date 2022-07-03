package amymialee.peculiarpieces.items;

import amymialee.peculiarpieces.util.WarpInstance;
import amymialee.peculiarpieces.util.WarpManager;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class PositionPearlItem extends Item {
    public PositionPearlItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && player.isSneaking() && context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.LODESTONE) {
            writeStone(context.getStack(), context.getBlockPos().add(0, 1, 0));
            player.getItemCooldownManager().set(this, 2);
        }
        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        BlockPos pos = readStone(stack);
        if (!world.isClient && !pos.equals(BlockPos.ORIGIN)) {
            if (!user.isSneaking()) {
                WarpManager.queueTeleport(WarpInstance.of(user).position(pos).particles());
            }
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.getItemCooldownManager().set(this, 2);
        return TypedActionResult.consume(stack);
    }

    public static void writeStone(ItemStack stack, BlockPos pos) {
        stack.getOrCreateNbt().put("pp:stone", NbtHelper.fromBlockPos(pos));
    }

    public static BlockPos readStone(ItemStack stack) {
        return NbtHelper.toBlockPos(stack.getOrCreateNbt().getCompound("pp:stone"));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        BlockPos pos;
        if (stack.getNbt() != null && !(pos = NbtHelper.toBlockPos(stack.getNbt().getCompound("pp:stone"))).equals(BlockPos.ORIGIN)) {
            tooltip.add(Text.translatable("Position: x%d, y%d, z%d".formatted(pos.getX(), pos.getY(), pos.getZ())).formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("peculiarpieces.pearl.empty").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("peculiarpieces.pearl.bind_stone").formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}