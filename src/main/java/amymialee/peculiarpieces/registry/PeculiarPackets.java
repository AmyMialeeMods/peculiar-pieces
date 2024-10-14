package amymialee.peculiarpieces.registry;

import amymialee.peculiarpieces.PeculiarPieces;
import amymialee.peculiarpieces.PeculiarPiecesClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PeculiarPackets {
    public static final Identifier SYNC_STRONGER_LEADS_PACKET_ID = new Identifier(PeculiarPieces.MOD_ID, "sync_stronger_leads");

    public static void registerPackets() {

        ServerPlayNetworking.registerGlobalReceiver(SYNC_STRONGER_LEADS_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            boolean strongerLeadsRule = buf.readBoolean();
                server.execute(() -> {
            });
        });
    }

    public static void registerS2CPackets() {

        ClientPlayNetworking.registerGlobalReceiver(PeculiarPackets.SYNC_STRONGER_LEADS_PACKET_ID, (client, handler, buf, responseSender) -> {
            boolean strongerLeadsRule = buf.readBoolean();
            client.execute(() -> {
                PeculiarPiecesClient.StrongerLeadsClientRule = strongerLeadsRule;
            });
        });
    }

    public static void sendGameRuleToClient(ServerPlayerEntity player, boolean gameRuleValue) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(gameRuleValue);
        ServerPlayNetworking.send(player, SYNC_STRONGER_LEADS_PACKET_ID, buf);
    }
}
