package com.deckerpw.launchpadx.api;

import java.awt.*;

public class PadButtonEvent {

    public final Launchpad launchpad;
    public final Point buttonPos;
    public final int force;

    public PadButtonEvent(Launchpad launchpad, Point buttonPos, int force) {
        this.launchpad = launchpad;
        this.buttonPos = buttonPos;
        this.force = force;
    }
}
