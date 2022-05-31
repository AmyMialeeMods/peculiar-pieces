package amymialee.peculiarpieces.items;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class BlazingGlidersItem extends TrinketItem implements Wearable {
    public static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior(){
        @Override
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            return dispenseTrinket(pointer, stack) ? stack : super.dispenseSilently(pointer, stack);
        }
    };

    public BlazingGlidersItem(FabricItemSettings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        entity.setNoDrag(true);
        super.tick(stack, slot, entity);
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        entity.setNoDrag(false);
        super.onUnequip(stack, slot, entity);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (equipItem(user, stack)) {
            return TypedActionResult.success(stack, world.isClient());
        }
        return super.use(world, user, hand);
    }

    public static boolean dispenseTrinket(BlockPointer pointer, ItemStack stack) {
        BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
        List<PlayerEntity> list = pointer.getWorld().getEntitiesByClass(PlayerEntity.class, new Box(blockPos), EntityPredicates.EXCEPT_SPECTATOR.and(new EntityPredicates.Equipable(stack)));
        if (list.isEmpty()) {
            return false;
        }
        PlayerEntity user = list.get(0);
        return equipItem(user, stack);
    }
}