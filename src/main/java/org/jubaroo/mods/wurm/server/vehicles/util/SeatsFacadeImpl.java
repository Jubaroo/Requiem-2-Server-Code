package org.jubaroo.mods.wurm.server.vehicles.util;

import com.wurmonline.server.behaviours.Seat;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SeatsFacadeImpl implements SeatsFacade {

    private static final Constructor<Seat> seat;

    static {
        try {

            Class<?> parameterTypes[] = {
                    byte.class
            };
            Class<Seat> clazz = Seat.class;


            seat = clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new HookException(e);
        }


    }

    public static void init() {


    }

    @Override
    public Seat CreateSeat(byte _type) {
        try {
            Seat s = ReflectionUtil.callPrivateConstructor(seat, _type);
            return s;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new HookException(e);
        }


    }

}
