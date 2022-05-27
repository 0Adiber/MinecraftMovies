package at.adiber.util;

import org.bukkit.ChatColor;

public class Messages {

    /*
     *****MINECRAFT*****
     */
    public static final String PREFIX = "§c§lMMovies§7 ";
    public static final String INFO = "§8[§9INFO§8]§7 ";
    public static final String ERROR = "§8[§4ERROR§8]§7 ";
    public static final String SECURE = "§8[§eSECURE§8]§7 ";

    //ERRORS
    public static final String NO_PERM = PREFIX + ERROR + "§cYou have no permissions!";
    public static final String WRONG_ARGS = PREFIX + ERROR +"§cWrong arguments: §7%s";
    public static final String CV_NOT_EXIST = PREFIX + ERROR +"§cCanvas or Video does not exist";
    public static final String C_NOT_EXIST = PREFIX + ERROR +"§cCanvas#ID §4%s §cdoes not exist";
    public static final String UNKNOWN = PREFIX + ERROR +"§cSomething went wrong (see console for more output)";
    public static final String NOT_A_PLAYER = PREFIX + ERROR +"You have to be a player!";
    public static final String NEED_TO_VERIFY = PREFIX + ERROR + "You need to verify first!";
    public static final String ALREADY_VERIFIED = PREFIX + ERROR + "Your account is already linked!";

    //INFO
    public static final String ID_OF_PLAYER = PREFIX + INFO + "§7Id of Player: §a%s";
    public static final String ID_OF_CANVAS = PREFIX + INFO + "§7Id of the Canvas: §b%s";
    public static final String RENDER_START = PREFIX + INFO + "§3Starting §7render... §8(see progress in console)";
    public static final String CANVAS_CREATED = PREFIX + INFO + "§aCreated §7a new Canvas!";
    public static final String RENDER_COMPLETE = PREFIX + INFO + "§7Render §acomplete";

    //VERIFY
    public static final String VERIFY_ID = PREFIX + SECURE + "§7Your verification code: §3%s §8(don't show it to anyone)";

    /*
     *****DISCORD*****
     */
    public static final String NO_VERIFY_CODE = "Please provide the verification code!";
    public static final String WRONG_VERIFY_CODE = "The verification code `%s` does not exist!";
    public static final String VERIFY_SUCCESS = "Successfully verified as `%s`";
}
