package org.jubaroo.mods.wurm.server.misc.templates;

public class StructureTemplate {

    public String sound;
    public String model;
    public String name;
    public int templateProduce;
    public int templateConsume;
    public int templateSecondaryConsume;
    public int maxNum;
    public int maxitems;
    public long timeout;
    public int templateID;

    public StructureTemplate( String name, String sound, String model, int templateProduce, int templateConsume, int templateSecondaryConsume, int maxNum, int maxitems, long timeout) {
        this.sound = sound;
        this.model = model;
        this.name = name;
        this.templateProduce = templateProduce;
        this.templateConsume = templateConsume;
        this.templateSecondaryConsume = templateSecondaryConsume;
        this.maxNum = maxNum;
        this.maxitems = maxitems;
        this.timeout = timeout;
    }


}
