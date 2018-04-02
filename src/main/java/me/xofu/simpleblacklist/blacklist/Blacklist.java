package me.xofu.simpleblacklist.blacklist;

import java.util.UUID;

public class Blacklist {

    private UUID player;
    private String punisher;
    private String reason;

    private String address;

    public Blacklist(UUID player, String punisher, String reason) {
        this.player = player;
        this.punisher = punisher;
        this.reason = reason;

        this.address = null;
    }

    public UUID getPlayer() {
        return player;
    }

    public String getPunisher() {
        return punisher;
    }

    public String getReason() {
        return reason;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
