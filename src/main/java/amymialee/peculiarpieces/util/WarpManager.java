package amymialee.peculiarpieces.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;

public class WarpManager {
    private static final ArrayList<WarpInstance> dueTeleports = new ArrayList<>();

    public static void tick() {
        for (int i = 0; i < dueTeleports.size();) {
            WarpInstance instance = dueTeleports.get(i);
            Entity entity = instance.getEntity();
            RegistryKey<World> world = instance.getWorld();
            if (world != null) {
                MinecraftServer server = instance.getEntity().getServer();
                if (server != null) {
                    entity.moveToWorld(server.getWorld(world));
                }
            }
            Vec3d pos = instance.getPosition();
            if (pos != null) {
                entity.dismountVehicle();
                if (entity instanceof LivingEntity livingEntity) {
                    teleport(livingEntity, pos.x, pos.y, pos.z, instance.hasParticles());
                } else {
                    entity.teleport(pos.x, pos.y, pos.z);
                }
            }
            if (instance.hasPitch() || instance.hasYaw()) {
                if (instance.hasYaw()) entity.setHeadYaw(instance.getYaw());
                if (instance.hasPitch()) entity.setHeadYaw(instance.getPitch());
                if (entity instanceof ServerPlayerEntity playerEntity) {
                    Vec3d playerPos = playerEntity.getPos();
                    playerEntity.networkHandler.sendPacket(
                            new PlayerPositionLookS2CPacket(playerPos.x, playerPos.y, playerPos.z,
                                    instance.hasYaw() ? instance.getYaw() : playerEntity.getYaw(),
                                    instance.hasPitch() ? instance.getPitch() : playerEntity.getPitch(),
                                    Collections.emptySet(), 0, true)
                    );
                }
            }
            dueTeleports.remove(instance);
        }
    }

    public static void queueTeleport(WarpInstance instance) {
        dueTeleports.add(instance);
    }

    public static void teleport(Entity entity, double x, double y, double z, boolean particleEffects) {
        if (particleEffects) {
            entity.world.sendEntityStatus(entity, (byte)46);
        }
        entity.requestTeleport(x, y, z);
        if (particleEffects) {
            entity.world.sendEntityStatus(entity, (byte)46);
        }
        if (entity instanceof PathAwareEntity) {
            ((PathAwareEntity)entity).getNavigation().stop();
        }
    }
}