package net.deechael.fabric.isekaiclient.mixin;

import net.deechael.fabric.isekaiclient.IsekaiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract boolean isInvisible();

    private static <T extends Entity> T getBestEntity(T entity) {
        Level world = entity.getCommandSenderWorld();
        Minecraft client = Minecraft.getInstance();
        T ret = entity;
        if (world == client.level) {
            world = getBestWorld(client);
            if (world != null && world != client.level) {
                Entity bestEntity = world.getEntity(entity.getId());
                if (entity.getClass().isInstance(bestEntity)) {
                    ret = (T) bestEntity;
                }
            }
        }
        return ret;
    }

    @Inject(method = "isCurrentlyGlowing", at = @At(value = "RETURN"), cancellable = true)
    private void checkWanderingTraderEntity(CallbackInfoReturnable<Boolean> cir) {
        if (IsekaiClient.getConfig().highlightWanderingTrader && !cir.getReturnValue()) {
            Entity entity = getBestEntity((Entity) ((Object) this));
            if (entity.getType() == EntityType.WANDERING_TRADER) {
                if (!IsekaiClient.getConfig().highlightWanderingTrader) {
                    return;
                }
                if (!this.isInvisible()) {
                    return;
                }
                cir.setReturnValue(true);
            }
        }
    }

    @Nullable
    private static Level getBestWorld(Minecraft mc) {
        IntegratedServer server = mc.getSingleplayerServer();
        if (mc.level != null && server != null) {
            return server.getLevel(mc.level.dimension());
        } else {
            return mc.level;
        }
    }

}
