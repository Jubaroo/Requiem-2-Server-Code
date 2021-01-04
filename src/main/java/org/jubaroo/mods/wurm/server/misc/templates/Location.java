package org.jubaroo.mods.wurm.server.misc.templates;

public class Location {
    public float x;
    public float y;
    public byte type;

    public Location(float coordinateX, float coordinateY, byte depotType) {
        x = coordinateX;
        y = coordinateY;
        type = depotType;
    }
}