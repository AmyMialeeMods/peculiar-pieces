package amymialee.peculiarpieces.mixin;

import amymialee.peculiarpieces.PeculiarPieces;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules.Rule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Rule.class)
public class GameRuleRuleMixin {
    @Inject(
            method = "Lnet/minecraft/world/GameRules$Rule;set(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)V",
            at = @At("TAIL")
    )
    public void set(CommandContext<ServerCommandSource> context, String name, CallbackInfo ci) {
        MinecraftServer server = context.getSource().getServer();
        server.getPlayerManager().getPlayerList().forEach(PeculiarPieces::syncGameRuleToClient);
    }
}
