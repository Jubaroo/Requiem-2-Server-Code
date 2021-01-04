package org.jubaroo.mods.wurm.server.misc.templates;

public class SpawnerTemplate {

    public String sound;
    public String model;
    public String name;
    public int mobType;
    public int maxNum;
    public long timeout;
    public int templateID;

    public SpawnerTemplate(String name, String sound, String model, int mobType, int maxNumber, long timeout) {
        this.sound = sound;
        this.model = model;
        this.name = name;
        this.mobType = mobType;
        this.maxNum = maxNumber;
        this.timeout = timeout;
    }
}
