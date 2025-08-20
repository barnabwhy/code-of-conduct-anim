package cc.barnab.codeOfConductAnim;

import net.minecraft.network.protocol.configuration.ClientboundCodeOfConductPacket;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.config.ServerCodeOfConductConfigurationTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AnimatedCodeOfConduct implements Runnable {
    private static List<String> frames = new  ArrayList<>();
    private final Queue<ConfigurationTask> configurationTasks;
    private final ServerCommonPacketListenerImpl listener;

    static final int DELAY_MS = 1000 / 30;
    static final int NUM_FRAMES = 6572;

    public static void loadFrames() {
        frames = new ArrayList<>();

        System.out.println("Loading frames...");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (int i = 1; i <= NUM_FRAMES; i++) {
            try (InputStream stream = classLoader.getResourceAsStream(String.format("frames/%04d.txt", i))) {
                if (stream == null)
                    continue;

                frames.add(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
            } catch (IOException ignored) {
            }
        }

        System.out.printf("Loaded %d frames.%n", frames.size());
    }

    public AnimatedCodeOfConduct(ServerCommonPacketListenerImpl listener, Queue<ConfigurationTask> configurationTasks) {
        this.configurationTasks = configurationTasks;
        this.listener = listener;
    }

    @Override
    public void run() {
        if (frames.isEmpty())
            return;

        configurationTasks.add(new ServerCodeOfConductConfigurationTask(() -> ""));

        for (String frame : frames) {
            listener.send(new ClientboundCodeOfConductPacket(frame));

            try {
                Thread.sleep(DELAY_MS);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
