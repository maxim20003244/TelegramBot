package org.example.service.enums;

public enum ServiceCommands {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");
    private final String cmd;

    ServiceCommands(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return cmd;
    }
    public static ServiceCommands fromValue(String v) {
        for (ServiceCommands c: ServiceCommands.values()) {
            if (c.cmd.equals(v)) {
                return c;
            }
        }
        return null;
    }



}
