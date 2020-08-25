package at.adiber.util;

import org.bukkit.ChatColor;

public class Messages {

    public static final String PREFIX = "§5MMovies§8= ";

    //ERRORS
    public static final String NO_PERM = PREFIX + "§cYou have no permissions!";
    public static final String WRONG_ARGS = PREFIX + "§cWrong arguments: §7%s";
    public static final String CV_NOT_EXIST = PREFIX + "§cCanvas or Video does not exist";
    public static final String C_NOT_EXIST = PREFIX + "§cCanvas#ID §4%s §cdoes not exist";
    public static final String UNKNOWN = PREFIX + "§cSomething went wrong (see console for more output)";
    public static final String NOT_A_PLAYER = PREFIX + "You have to be a player!";

    //INFO
    public static final String ID_OF_PLAYER = PREFIX + "§7Id of Player: §a%s";
    public static final String ID_OF_CANVAS = PREFIX + "§7Id of the Canvas: §b%s";
    public static final String RENDER_START = PREFIX + "§3Starting §7render... §8(see progress in console)";
    public static final String CANVAS_CREATED = PREFIX + "§aCreated §7a new Canvas!";
    public static final String RENDER_COMPLETE = PREFIX + "§7Render §acomplete";

}
