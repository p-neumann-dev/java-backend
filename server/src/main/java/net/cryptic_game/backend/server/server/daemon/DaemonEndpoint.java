package net.cryptic_game.backend.server.server.daemon;

import com.google.gson.JsonObject;

public abstract class DaemonEndpoint {

    private final String name;

    public DaemonEndpoint(final String name) {
        this.name = name;
    }

    public abstract JsonObject handleRequest(JsonObject data) throws Exception;

    public String getName() {
        return this.name;
    }

}
