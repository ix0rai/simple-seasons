package io.github.steveplays28.simpleseasons.mixin.block;

import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowBlock.class)
public class SnowBlockMixin {
	@Inject(method = "randomTick", at = @At(value = "HEAD"), cancellable = true)
	public void randomTickInject(@NotNull BlockState blockState, @NotNull ServerWorld serverWorld, @NotNull BlockPos blockPos, @NotNull Random random, @NotNull CallbackInfo ci) {
		@NotNull var biome = serverWorld.getBiome(blockPos);
		if (!SimpleSeasonsApi.worldHasSeasons(serverWorld) || SimpleSeasonsApi.biomeHasWetAndDrySeasons(biome)) {
			return;
		}

		if (SimpleSeasonsApi.getSeason(serverWorld) == SeasonTracker.Seasons.WINTER) {
			ci.cancel();
			return;
		}

		serverWorld.removeBlock(blockPos, false);
	}
}
