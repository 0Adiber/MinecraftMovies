package at.adiber.discord;

public class Config {

    private String token;
    private String prefix;

    private String MMRole;

    public Config(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMMRole() {
        return MMRole;
    }

    public void setMMRole(String MMRole) {
        this.MMRole = MMRole;
    }

}
