package cc.barnab.codeOfConductAnim.mixin;

import cc.barnab.codeOfConductAnim.AnimatedCodeOfConduct;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.configuration.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class ServerConfigurationPacketListenerImplMixin extends ServerCommonPacketListenerImpl implements ServerConfigurationPacketListener, TickablePacketListener {
    @Shadow @Final private Queue<ConfigurationTask> configurationTasks;

    @Inject(method = "addOptionalTasks", at = @At("TAIL"))
    private void addOptionalTasks(CallbackInfo ci) {
        new AnimatedCodeOfConduct(this, this.configurationTasks).run();
    }

    public ServerConfigurationPacketListenerImplMixin(MinecraftServer minecraftServer, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraftServer, connection, commonListenerCookie);
    }
}
