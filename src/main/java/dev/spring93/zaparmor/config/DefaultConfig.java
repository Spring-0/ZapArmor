package dev.spring93.zaparmor.config;


public class DefaultConfig extends Config{
    public DefaultConfig() {
        super("config");
    }

    public String getInvalidArgsNumberMessage() {
        return getConfigString("invalid-argument-amount-message");
    }

    public String getInvalidArgMessage() {
        return getConfigString("invalid-argument-message");
    }

    /**
     * Method used to return the configured message prefix.
     * @return
     */
    public String getMessagePrefix() {
        return getConfigString("message-prefix");
    }
    public String getIndexString(String path) {
        return getConfigString(path );
    }

}
