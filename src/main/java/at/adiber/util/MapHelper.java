package at.adiber.util;

import net.minecraft.server.v1_16_R2.*;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.map.MapPalette;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static at.adiber.reflect.Reflection.*;
import static java.util.Collections.emptyList;

public class MapHelper {

    static final int DEFAULT_STARTING_ID = 8000;
    private static final Field ENTITY_ID = findField(Entity.class, "id");
    private static final Map<UUID, AtomicInteger> MAP_IDS = new HashMap<>(4);

    private static final Map<Location, Integer> PREV_IDS = new HashMap<>();

    public static int nextMapId(Location location) {
        Integer id = PREV_IDS.get(location);
        if(id != null) {
            return id;
        }
        id = MAP_IDS.computeIfAbsent(location.getWorld().getUID(), __ ->
                new AtomicInteger(DEFAULT_STARTING_ID)).getAndIncrement();
        PREV_IDS.put(location, id);
        return id;
    }

    public static byte[] getPixels(BufferedImage image) {
        int pixelCount = image.getHeight() * image.getWidth();
        int[] pixels = new int[pixelCount];
        image.getRGB(0,0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        byte[] colors = new byte[pixelCount];
        for(int i = 0; i<pixelCount; i++) {
            colors[i] = MapPalette.matchColor(new Color(pixels[i], true));
        }

        List<Byte> finals = new ArrayList<>();

        byte len = 0;
        for(int i = 0; i < colors.length-1; i++) {
            if(colors[i] == colors[i+1] && len < 127) {
                len++;
                continue;
            }
            finals.add(len);
            finals.add(colors[i]);
            len = 0;
        }
        finals.add(len);
        finals.add(colors[127]);

        return ArrayUtils.toPrimitive(finals.toArray(new Byte[0]));
    }

    public static byte[] uncompress(byte[] compressed) {
        List<Byte> pixels = new ArrayList<>();
        for(int i = 0; i<compressed.length; i+=2) {
            for(int n = 0; n<=compressed[i]; n++) {
                pixels.add(compressed[i+1]);
            }
        }

        return ArrayUtils.toPrimitive(pixels.toArray(new Byte[0]));
    }

    public static void createMap(Player player, int frameId, int mapId, Location location, BlockFace direction, byte[] pixels) {

        ItemStack item = new ItemStack(Items.FILLED_MAP);
        item.getOrCreateTag().setInt("map", mapId);

        EntityItemFrame frame = new EntityItemFrame(((CraftWorld) player.getWorld()).getHandle(),
                new BlockPosition(location.getX(), location.getY(), location.getZ()),
                CraftBlock.blockFaceToNotch(direction));
        frame.setItem(item, false, false);
        setFieldValue(ENTITY_ID, frame, frameId);

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutSpawnEntity(frame, EntityTypes.ITEM_FRAME,
                frame.getDirection().c(), frame.getBlockPosition()));
        connection.sendPacket(new PacketPlayOutEntityMetadata(frame.getId(), frame.getDataWatcher(), true));
        connection.sendPacket(new PacketPlayOutMap(mapId, (byte) 3, false, false, emptyList(), pixels, 0, 0, 128, 128));

    }

    public static void destroyMap(Player player, int frameId) {
        ItemFrame frame = null;
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(frameId));
    }

}
