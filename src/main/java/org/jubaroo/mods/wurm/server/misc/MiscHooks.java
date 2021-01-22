package org.jubaroo.mods.wurm.server.misc;

import com.wurmonline.server.Server;
import com.wurmonline.server.bodys.BodyTemplate;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSpaceException;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import net.bdew.wurm.tools.server.ServerThreadExecutor;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MiscHooks {

    public static void sendKingdoms(Communicator comm) {
        if (comm.player != null && comm.player.hasLink()) {
            try {
                final ByteBuffer bb = comm.getConnection().getBuffer();
                bb.put((byte) 40);
                bb.putInt(DecorativeKingdoms.values().length);
                for (DecorativeKingdoms kingdom : DecorativeKingdoms.values()) {
                    bb.put((byte) kingdom.ordinal());
                    final byte[] kingdomNameStringArr = kingdom.display.getBytes(StandardCharsets.UTF_8);
                    bb.put((byte) kingdomNameStringArr.length);
                    bb.put(kingdomNameStringArr);
                    final byte[] suffixStringArr = kingdom.suffix.getBytes(StandardCharsets.UTF_8);
                    bb.put((byte) suffixStringArr.length);
                    bb.put(suffixStringArr);
                }
                comm.getConnection().flush();
            } catch (Exception ex) {
                RequiemLogging.logException(String.format("Error sending kingdoms to %s: %s", comm.player.getName(), ex.toString()), ex);
                comm.player.setLink(false);

            }
        }
    }

    public static byte creatureKingdom(Creature creature, byte original) {
        if (creature.isPlayer()) {
            try {
                Item tabardSlot = creature.getStatus().getBody().getBodyPart(BodyTemplate.tabardSlot);
                if (tabardSlot != null) {
                    for (Item tabard : tabardSlot.getItems()) {
                        if (tabard != null && tabard.getTemplateId() == ItemList.tabard && tabard.getAuxData() > 0)
                            return tabard.getAuxData();
                    }
                }
            } catch (NoSpaceException e) {
                return original;
            }
        }
        return original;
    }

    public static void sendWearHook(Item item, Creature creature) {
        if (item.getTemplateId() == ItemList.tabard && item.getAuxData() > 0 && creature != null && creature.isPlayer()) {
            ServerThreadExecutor.INSTANCE.execute(() -> {
                creature.setVisible(false);
                creature.setVisible(true);
                updateKingdom(creature, item.getAuxData());
            });
        }
    }

    public static void updateKingdom(Creature creature, byte kingdom) {
        try {
            if (creature.isPlayer() && creature.hasLink()) {
                final ByteBuffer bb = creature.getCommunicator().getConnection().getBuffer();
                bb.put((byte) (-37));
                bb.put(kingdom);
                creature.getCommunicator().getConnection().flush();
            }
        } catch (IOException ignored) {
        }
    }

    public static void serverTick(Server server) {
        ServerThreadExecutor.INSTANCE.tick();
    }

    static void hookSurfaceMine(ClassPool pool) {
        try {
            CtClass tileRockBehav = pool.get("com.wurmonline.server.behaviours.TileRockBehaviour");
            CtMethod cm = tileRockBehav.getMethod("action", "(Lcom/wurmonline/server/behaviours/Action;Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;IIZIISF)Z");

            cm.instrument(
                    new ExprEditor() {
                        public void edit(MethodCall m)
                                throws CannotCompileException {
                            //logger.log(Level.INFO, String.format("MethodCall: %s %s", m.getClassName(), m.getMethodName()));
                            if (!m.getClassName().equals("java.util.Random")) {
                                return;
                            }
                            if (!m.getMethodName().equals("nextInt")) {
                                return;
                            }
                            // A bit ghetto but...  right now, the nextInt() call we want
                            // is the only one in the method that calls with nextInt(5)...
                            m.replace("{ if ( $1 == 5 ) { $_ = 0; } else { $_ = $proceed($$); } }");
                        }
                    });
        } catch (Throwable e) {
            RequiemLogging.logException("hookSurfaceMine()", e);
        }
    }

    public static void newPlayerQuestion() throws NotFoundException, CannotCompileException {
        final ClassPool classPool = HookManager.getInstance().getClassPool();
        // New creation
        classPool.getCtClass("com.wurmonline.server.LoginHandler").getMethod("handleLogin", "(Ljava/lang/String;Ljava/lang/String;ZZZZLjava/lang/String;Ljava/lang/String;)V")
                .instrument(new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("sendQuestion")) {
                            m.replace("if (!com.wurmonline.server.questions.NewPlayerQuestion.send($0.getResponder())) $proceed();");
                        }
                    }
                });
    }
}
