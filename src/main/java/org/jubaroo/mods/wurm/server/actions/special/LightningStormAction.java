package org.jubaroo.mods.wurm.server.actions.special;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Players;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.EffectConstants;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.Collections;
import java.util.List;

public class LightningStormAction implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
    static short actionId;
    static ActionEntry actionEntry;
    private Item markArr;
    private boolean status;

    public LightningStormAction() {
        this.markArr = null;
        this.status = false;
        LightningStormAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(LightningStormAction.actionEntry = ActionEntry.createEntry(LightningStormAction.actionId, "Add/remove storm", "making", new int[]{
                ActionTypesProxy.ACTION_TYPE_BLOCKED_FENCE
        }));
    }

    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    public ActionPerformer getActionPerformer() {
        return this;
    }

    public short getActionId() {
        return LightningStormAction.actionId;
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item target) {
        if (performer.getPower() > MiscConstants.POWER_DEMIGOD) {
            return Collections.singletonList(LightningStormAction.actionEntry);
        }
        return null;
    }

    public boolean action(final Action act, final Creature performer, final Item source, final Item target, final short action, final float counter) {
        try {
            if (source.getTemplateId() == ItemList.bodyHand && target.getTemplateId() == ItemList.tempmarker && this.status) {
                if (this.markArr == null) {
                    return true;
                }
                this.status = false;
                final Player[] players = Players.getInstance().getPlayers();
                if (players.length != 0) {
                    for (Player player : players) {
                        if (player != null) {
                            player.getCommunicator().sendRemoveEffect(this.markArr.getWurmId());
                        }
                    }
                }
                this.markArr.removeAndEmpty();
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item object, final int tilex, final int tiley, final boolean onSurface, final int tile) {
        if (performer.getPower() > MiscConstants.POWER_DEMIGOD) {
            return Collections.singletonList(LightningStormAction.actionEntry);
        }
        return null;
    }

    public boolean action(final Action act, final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short action, final float counter) {
        try {
            if (source.getTemplateId() == ItemList.bodyHand && !this.status) {
                final VolaTile vTile = Zones.getOrCreateTile(tilex, tiley, performer.isOnSurface());
                try {
                    (this.markArr = ItemFactory.createItem(ItemList.tempmarker, 99.0f, null)).setIsNoTake(true);
                    this.markArr.setHasNoDecay(true);
                    vTile.addItem(this.markArr, true, false);
                    this.status = true;
                    final Player[] players = Players.getInstance().getPlayers();
                    if (players.length != 0) {
                        for (Player player : players) {
                            if (player != null) {
                                player.getCommunicator().sendAddEffect(this.markArr.getWurmId(), EffectConstants.EFFECT_METEOR, (float) ((tilex << 2) + 2), (float) ((tiley << 2) + 2), 0.0f, (byte) 0);
                            }
                        }
                    }
                    this.timeToBeam(tilex, tiley);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return true;
        } catch (Exception e2) {
            return true;
        }
    }

    private void timeToBeam(final int tilex, final int tiley) {
        if (!LightningStormAction.this.status) {
            return;
        }
        final Player[] players = Players.getInstance().getPlayers();
        if (players.length != 0) {
            for (Player player : players) {
                if (player != null) {
                    player.getCommunicator().sendRemoveEffect(LightningStormAction.this.markArr.getWurmId());
                    player.getCommunicator().sendAddEffect(LightningStormAction.this.markArr.getWurmId(), EffectConstants.EFFECT_ERUPTION, (float) ((tilex << 2) + 2), (float) ((tiley << 2) + 2), 0.0f, (byte) 0);
                }
            }
        }
    }
}

