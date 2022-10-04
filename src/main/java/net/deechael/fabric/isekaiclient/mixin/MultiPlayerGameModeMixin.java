package net.deechael.fabric.isekaiclient.mixin;

import net.deechael.fabric.isekaiclient.IsekaiClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Shadow
    private int destroyDelay;

    @Inject(method = "continueDestroyBlock",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyDelay:I",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 2, shift = At.Shift.AFTER))
    private void removeBreakingCooldown(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (IsekaiClient.getConfig().removeBreakingCooldown) {
            destroyDelay = 0;
        }
    }

    @Inject(method = "useItemOn",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void preventIntentionalGameDesign(LocalPlayer player,
                                              InteractionHand hand,
                                              BlockHitResult hitResult,
                                              CallbackInfoReturnable<InteractionResult> cir) {
        ClientLevel world = (ClientLevel) player.getLevel();
        //#endif
        if (!IsekaiClient.getConfig().avoidIntentionalGameDesign) {
            return;
        }
        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if ((blockState.getBlock() instanceof BedBlock &&
                !world.dimensionType().bedWorks()) ||
                (blockState.getBlock() instanceof RespawnAnchorBlock && !world.dimensionType().respawnAnchorWorks())
        ) {
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

}
