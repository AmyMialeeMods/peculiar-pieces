package amymialee.peculiarpieces;

import amymialee.peculiarpieces.blockentities.FlagBlockEntity;
import amymialee.peculiarpieces.blocks.RedstoneStaticBlock;
import amymialee.peculiarpieces.client.CreativeBarrelBlockEntityRenderer;
import amymialee.peculiarpieces.client.EquipmentStandBlockEntityRenderer;
import amymialee.peculiarpieces.client.EquipmentStandEntityRenderer;
import amymialee.peculiarpieces.client.FishTankBlockEntityRenderer;
import amymialee.peculiarpieces.client.FlagBlockEntityRenderer;
import amymialee.peculiarpieces.client.HangGliderEntityModel;
import amymialee.peculiarpieces.client.PedestalBlockEntityRenderer;
import amymialee.peculiarpieces.client.RedstoneTriggerBlockEntityRenderer;
import amymialee.peculiarpieces.client.TeleportItemEntityRenderer;
import amymialee.peculiarpieces.client.VisibleBarriersCompat;
import amymialee.peculiarpieces.items.GliderItem;
import amymialee.peculiarpieces.items.PlayerCompassItem;
import amymialee.peculiarpieces.items.TransportPearlItem;
import amymialee.peculiarpieces.particles.WardingParticle;
import amymialee.peculiarpieces.registry.PeculiarBlocks;
import amymialee.peculiarpieces.registry.PeculiarEntities;
import amymialee.peculiarpieces.registry.PeculiarItems;
import amymialee.peculiarpieces.registry.PeculiarPackets;
import amymialee.peculiarpieces.screens.CouriporterScreen;
import amymialee.peculiarpieces.screens.CreativeBarrelScreen;
import amymialee.peculiarpieces.screens.EquipmentStandScreen;
import amymialee.peculiarpieces.screens.FishTankScreen;
import amymialee.peculiarpieces.screens.PackedPouchScreen;
import amymialee.peculiarpieces.screens.PedestalScreen;
import amymialee.peculiarpieces.screens.PotionPadScreen;
import amymialee.peculiarpieces.screens.RedstoneTriggerScreen;
import amymialee.peculiarpieces.screens.WarpScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class PeculiarPiecesClient implements ClientModInitializer {
    public static final EntityModelLayer HANG_GLIDER = new EntityModelLayer(PeculiarPieces.id("hang_glider"), "main");
    public static final EntityModelLayer FLAG = new EntityModelLayer(PeculiarPieces.id("flag"), "main");
    public static final EntityModelLayer EMPTY = new EntityModelLayer(PeculiarPieces.id("empty"), "main");
    public static final EntityModelLayer EQUIPMENT_STAND = new EntityModelLayer(PeculiarPieces.id("equipment_stand"), "main");
    private final FlagBlockEntity renderFlag = new FlagBlockEntity(BlockPos.ORIGIN, PeculiarBlocks.FLAG.getDefaultState());

    public static boolean StrongerLeadsClientRule = false;

    @Override
    public void onInitializeClient() {
        PeculiarPackets.registerS2CPackets();
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.CHECKPOINT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.CHECKPOINT_REMOVER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.CHECKPOINT_RETURNER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.CHECKPOINT_DAMAGER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.INVISIBLE_PLATE_HEAVY, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.INVISIBLE_PLATE_LIGHT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.INVISIBLE_PLAYER_PLATE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.REDSTONE_CLAMP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.REDSTONE_FILTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.REDSTONE_HURDLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.REDSTONE_STATIC, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.REDSTONE_MONO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.REDSTONE_RANDOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.REDSTONE_FLIP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.REDSTONE_CLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.REDSTONE_TRIGGER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.ADVENTURE_BARRIER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.SURVIVOR_BARRIER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.ADVENTURE_BLOCKER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.SURVIVOR_BLOCKER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.ADVENTURE_SETTER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.SURVIVOR_SETTER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.TORCH_LEVER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.SOUL_TORCH_LEVER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.ENTITY_GLASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.TINTED_ENTITY_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.PLAYER_GLASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.TINTED_PLAYER_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.ENTITY_BARRIER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.PLAYER_BARRIER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.TOUGHENED_SCAFFOLDING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.ENTANGLED_SCAFFOLDING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.POTION_PAD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.FISH_TANK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.HEAVY_GLASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.HEAVY_STONE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.LIVING_LADDER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.COURIPORTER_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.STONE_PHASING_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.DEEPSLATE_PHASING_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.BLACKSTONE_PHASING_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.QUARTZ_PHASING_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.PRISMARINE_PHASING_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.CREATIVE_BARREL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PeculiarBlocks.HEAVY_GLASS_TILES, RenderLayer.getCutout());

        HandledScreens.register(PeculiarPieces.WARP_SCREEN_HANDLER, WarpScreen::new);
        HandledScreens.register(PeculiarPieces.COURIPORTER_SCREEN_HANDLER, CouriporterScreen::new);
        HandledScreens.register(PeculiarPieces.POTION_PAD_SCREEN_HANDLER, PotionPadScreen::new);
        HandledScreens.register(PeculiarPieces.BUSTLING_SCREEN_HANDLER, PackedPouchScreen::new);
        HandledScreens.register(PeculiarPieces.PEDESTAL_SCREEN_HANDLER, PedestalScreen::new);
        HandledScreens.register(PeculiarPieces.FISH_TANK_SCREEN_HANDLER, FishTankScreen::new);
        HandledScreens.register(PeculiarPieces.REDSTONE_TRIGGER_SCREEN_HANDLER, RedstoneTriggerScreen::new);
        HandledScreens.register(PeculiarPieces.CREATIVE_BARREL_SCREEN_HANDLER, CreativeBarrelScreen::new);
        HandledScreens.register(PeculiarPieces.EQUIPMENT_STAND_SCREEN_HANDLER, EquipmentStandScreen::new);

        EntityModelLayerRegistry.registerModelLayer(HANG_GLIDER, HangGliderEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(FLAG, FlagBlockEntityRenderer::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(EMPTY, () -> TexturedModelData.of(new ModelData(), 16, 16));
        EntityModelLayerRegistry.registerModelLayer(EQUIPMENT_STAND, () -> TexturedModelData.of(PlayerEntityModel.getTexturedModelData(Dilation.NONE, true), 64, 64));

        EntityRendererRegistry.register(PeculiarEntities.TELEPORT_ITEM_ENTITY, TeleportItemEntityRenderer::new);
        EntityRendererRegistry.register(PeculiarEntities.EQUIPMENT_STAND_ENTITY, EquipmentStandEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(PeculiarPieces.WARDING_AURA, WardingParticle.Factory::new);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> tintIndex == 1 ? (state.get(RedstoneStaticBlock.POWERED) ? 16711680 : 2621440) : -1, PeculiarBlocks.REDSTONE_STATIC);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (tintIndex != 1) {
                return -1;
            }
            if (world == null || pos == null) {
                return FoliageColors.getDefaultColor();
            }
            return BiomeColors.getFoliageColor(world, pos);
        }, PeculiarBlocks.LIVING_LADDER);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex != 1) {
                return -1;
            }
            var blockState = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
            return MinecraftClient.getInstance().getBlockColors().getColor(blockState, null, null, tintIndex);
        }, PeculiarBlocks.LIVING_LADDER);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex != 1 ? -1 : 16711680, PeculiarBlocks.REDSTONE_STATIC);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> MathHelper.hsvToRgb(((float)(TransportPearlItem.getSlot(stack) + 1) / 8), 1.0F, 1.0F), PeculiarItems.TRANS_PEARL);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : 0xF800F8, PeculiarBlocks.POTION_PAD);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : PotionUtil.getColor(stack), PeculiarItems.HIDDEN_POTION);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : this.getColorOr(stack, 10511680), PeculiarItems.PACKED_POUCH);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : this.getColorOr(stack, 3655735), PeculiarItems.REACHING_REMOTE);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : this.getColorOr(stack, 13121335), PeculiarItems.REDSTONE_REMOTE);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : 16560501, PeculiarItems.PLAYER_COMPASS);

        BlockEntityRendererRegistry.register(PeculiarBlocks.PEDESTAL_BLOCK_ENTITY, ctx -> new PedestalBlockEntityRenderer());
        BlockEntityRendererRegistry.register(PeculiarBlocks.FISH_TANK_BLOCK_ENTITY, ctx -> new FishTankBlockEntityRenderer());
        BlockEntityRendererRegistry.register(PeculiarBlocks.FLAG_BLOCK_ENTITY, FlagBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(PeculiarBlocks.REDSTONE_TRIGGER_BLOCK_ENTITY, ctx -> new RedstoneTriggerBlockEntityRenderer());
        BlockEntityRendererRegistry.register(PeculiarBlocks.CREATIVE_BARREL_BLOCK_ENTITY, ctx -> new CreativeBarrelBlockEntityRenderer());
        BlockEntityRendererRegistry.register(PeculiarBlocks.EQUIPMENT_STAND_BLOCK_ENTITY, ctx -> new EquipmentStandBlockEntityRenderer());

        BuiltinItemRendererRegistry.INSTANCE.register(PeculiarBlocks.FLAG, (stack, mode, matrixStack, vertexConsumerProvider, light, overlay) -> {
            this.renderFlag.readFrom(stack);
            MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(this.renderFlag, matrixStack, vertexConsumerProvider, light, overlay);
        });
        
        if (FabricLoader.getInstance().isModLoaded("visiblebarriers")) {
            VisibleBarriersCompat.init();
        }
    }

    public int getColorOr(ItemStack stack, int base) {
        var nbtCompound = stack.getSubNbt(DyeableItem.DISPLAY_KEY);
        if (nbtCompound != null && nbtCompound.contains(DyeableItem.COLOR_KEY, NbtElement.NUMBER_TYPE)) {
            return nbtCompound.getInt(DyeableItem.COLOR_KEY);
        }
        return base;
    }

    static {
        ModelPredicateProviderRegistry.register(PeculiarBlocks.JUMP_PAD.asItem(), new Identifier("variant"), (stack, world, entity, number) -> stack.getNbt() == null ? 0 : (float) stack.getNbt().getInt("pp:variant") / 3);
        ModelPredicateProviderRegistry.register(PeculiarBlocks.PUSH_PAD.asItem(), new Identifier("variant"), (stack, world, entity, number) -> stack.getNbt() == null ? 0 : (float) stack.getNbt().getInt("pp:variant") / 3);
        ModelPredicateProviderRegistry.register(PeculiarItems.HANG_GLIDER, new Identifier("active"), (stack, world, entity, number) -> stack.getNbt() != null && stack.getNbt().getBoolean("pp:gliding") ? 1 : 0);
        ModelPredicateProviderRegistry.register(PeculiarItems.HANG_GLIDER, new Identifier("gliding"), (stack, world, entity, number) -> entity != null && GliderItem.isGliding(entity) ? 1 : 0);
        ModelPredicateProviderRegistry.register(PeculiarItems.PLAYER_COMPASS, new Identifier("angle"), new CompassAnglePredicateProvider((world, stack, entity) -> PlayerCompassItem.createPlayerPos(world, stack.getOrCreateNbt())));
        ModelPredicateProviderRegistry.register(PeculiarItems.TORCH_QUIVER, new Identifier("setting"), (stack, world, entity, number) -> stack.getOrCreateNbt().getInt("pp:setting"));
        ModelPredicateProviderRegistry.register(PeculiarItems.SLIME, new Identifier("chunked"), (stack, world, entity, number) -> stack.getNbt() == null || !stack.getNbt().getBoolean("pp:chunked") ? 0 : 1);
    }
}