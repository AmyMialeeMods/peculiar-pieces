package amymialee.peculiarpieces.blocks;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public class BlockBreakerBlock extends Block {
    public static final DirectionProperty FACING = FacingBlock.FACING;
    public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;
    private final boolean silk;
    private static final ItemStack pick = new ItemStack(Items.NETHERITE_PICKAXE);
    private static final ItemStack pick_silk = new ItemStack(Items.NETHERITE_PICKAXE);

    public BlockBreakerBlock(boolean silk, Settings settings) {
        super(settings);
        this.silk = silk;
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(TRIGGERED, false));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        boolean bl2 = state.get(TRIGGERED);
        if (bl && !bl2) {
            this.destroy(world, pos.add(state.get(FACING).getVector()));
            world.setBlockState(pos, state.with(TRIGGERED, true), Block.NO_REDRAW);
        } else if (!bl && bl2) {
            world.setBlockState(pos, state.with(TRIGGERED, false), Block.NO_REDRAW);
        }
    }

    protected void destroy(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getHardness(world, pos) != -1 && !blockState.isAir()) {
            world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.2f, (1.0f + world.random.nextFloat()) * 2f);
            this.breakBlock(world, pos);
            if (world instanceof ServerWorld serverWorld) {
                Vec3d vec3d = Vec3d.ofCenter(pos);
                serverWorld.spawnParticles(ParticleTypes.EXPLOSION, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 1, 0.0, 0.0, 0.0, 1.0);
            }
        }
    }

    public void breakBlock(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.isAir()) {
            return;
        }
        FluidState fluidState = world.getFluidState(pos);
        if (!(blockState.getBlock() instanceof AbstractFireBlock)) {
            world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(blockState));
        }
        BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(blockState, world, pos, blockEntity, null, this.silk ? pick_silk : pick);
        if (world.setBlockState(pos, fluidState.getBlockState(), Block.NOTIFY_ALL, 512)) {
            world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(null, blockState));
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }

    static {
        pick_silk.addEnchantment(Enchantments.SILK_TOUCH, 1);
    }
}