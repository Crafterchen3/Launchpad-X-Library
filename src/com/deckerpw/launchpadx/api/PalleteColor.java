package com.deckerpw.launchpadx.api;

public class PalleteColor {

    public static final PalleteColor BLACK = new PalleteColor(0);
    public static final PalleteColor RED = new PalleteColor(5);
    public static final PalleteColor BLUE = new PalleteColor(45);
    public static final PalleteColor GREEN = new PalleteColor(21);
    public static final PalleteColor PURPLE = new PalleteColor(80);

    public final int color;

    public PalleteColor(int color) {
        this.color = color;
    }
}
