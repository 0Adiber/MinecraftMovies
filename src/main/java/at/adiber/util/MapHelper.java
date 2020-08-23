package at.adiber.util;

import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.map.MapPalette;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static at.adiber.reflect.Reflection.*;
import static java.util.Collections.emptyList;

public class MapHelper {

    static final int DEFAULT_STARTING_ID = 8000;
    private static final Field ENTITY_ID = findField(Entity.class, "id");
    private static final DataWatcherObject<Integer> ROTATION =
            getFieldValue(EntityItemFrame.class, null, "g");
    private static final Map<UUID, AtomicInteger> MAP_IDS = new HashMap<>(4);

    public static int nextMapId(org.bukkit.World world) {
        return MAP_IDS.computeIfAbsent(world.getUID(), __ ->
                new AtomicInteger(DEFAULT_STARTING_ID)).getAndIncrement();
    }

    public static byte[] getPixels(BufferedImage image) {
        int pixelCount = image.getHeight() * image.getWidth();
        int[] pixels = new int[pixelCount];
        image.getRGB(0,0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        byte[] colors = new byte[pixelCount];
        for(int i = 0; i<pixelCount; i++) {
            colors[i] = MapPalette.matchColor(new Color(pixels[i], true));
        }

        return colors;
    }

    public static void createMap(Player player, int frameId, int mapId, Location location, BlockFace direction, int rotation, byte[] pixels) {

        ItemStack item = new ItemStack(Items.FILLED_MAP);
        item.getOrCreateTag().setInt("map", mapId);

        EntityItemFrame frame = new EntityItemFrame(((CraftWorld) player.getWorld()).getHandle(),
                new BlockPosition(location.getX(), location.getY(), location.getZ()),
                CraftBlock.blockFaceToNotch(direction));
        frame.setItem(item, false, false);
        setFieldValue(ENTITY_ID, frame, frameId);
        if (rotation != 0) {
            frame.getDataWatcher().set(ROTATION, rotation);
        }

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutSpawnEntity(frame, EntityTypes.ITEM_FRAME,
                frame.getDirection().c(), frame.getBlockPosition()));
        connection.sendPacket(new PacketPlayOutEntityMetadata(frame.getId(), frame.getDataWatcher(), true));
        connection.sendPacket(new PacketPlayOutMap(mapId, (byte) 3, false, false, emptyList(), pixels, 0, 0, 128, 128));

    }

    public static void destroyMap(Player player, int frameId) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(frameId));
    }

}
