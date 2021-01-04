package org.jubaroo.mods.wurm.server.misc.templates;

public class ScrollTemplate {

    public String model;
    public String name;
    public String description;
    public int templateID;
    public int skillID;
    public float skillModifier;

    public ScrollTemplate(String name, String description, String model, int skillID, float skillModifier) {
        this.model = model;
        this.name = name;
        this.description = description;
        this.skillID = skillID;
        this.skillModifier = skillModifier;
    }

}