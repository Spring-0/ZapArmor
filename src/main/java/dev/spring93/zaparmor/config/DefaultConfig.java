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

    public String getMessagePrefix() {
        return getConfigString("message-prefix");
    }

}
