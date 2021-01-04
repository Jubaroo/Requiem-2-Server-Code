package org.jubaroo.mods.wurm.server.server;

import org.jubaroo.mods.wurm.server.items.CustomItems;

public class OnItemTemplateCreated {
    public static int[] portalItems = {CustomItems.nymphPortal.getTemplateId(), CustomItems.demonPortal.getTemplateId(), CustomItems.nymphHomePortal.getTemplateId(), CustomItems.demonHomePortal.getTemplateId()};
    public static int[] homePortalItems = {CustomItems.nymphHomePortal.getTemplateId(), CustomItems.demonHomePortal.getTemplateId()};
}
