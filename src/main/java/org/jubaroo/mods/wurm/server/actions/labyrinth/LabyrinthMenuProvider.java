package org.jubaroo.mods.wurm.server.actions.labyrinth;

import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;

import java.util.ArrayList;
import java.util.List;

public class LabyrinthMenuProvider implements BehaviourProvider {
    private List<ActionEntry> menu;

    public LabyrinthMenuProvider() {
        menu = new ArrayList<>();
        menu.add(new LabyrinthPerformer().actionEntry);
        menu.add(new LabyrinthRemovePerformer().actionEntry);
        menu.add(0, new ActionEntry((short) (-1 * menu.size()), "Labyrinth", ""));
    }

    static boolean isAllowed(final Creature performer) {
        return (LabyrinthPerformer.isGMonly && performer.getPower() < LabyrinthPerformer.requiredGMlevel) || (performer.getPower() < LabyrinthPerformer.requiredGMlevel);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
        return this.getBehavioursFor(performer, object);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
        if (performer instanceof Player && object != null && isAllowed(performer)) {
            return menu;
        }
        return null;
    }


}
