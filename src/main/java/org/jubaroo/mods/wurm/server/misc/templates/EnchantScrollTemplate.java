package org.jubaroo.mods.wurm.server.misc.templates;

public class EnchantScrollTemplate {

    public String model;
    public String name;
    public String description;
    public int templateID;
    public int enchantID;

    public EnchantScrollTemplate(String name, String description, String model, int enchantID) {
        this.model = model;
        this.name = name;
        this.description = description;
        this.enchantID = enchantID;
    }
}
