package org.jubaroo.mods.wurm.server.utils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class MethodQuery {
    private Set<Class<?>> result = new HashSet<>();
    private String theMethodName;

    private void traverse(Class<?> cls) {
        for (Class<?> c : cls.getInterfaces()) {
            for (Method m : c.getDeclaredMethods()) {
                if (theMethodName.equals(m.getName())) {
                    result.add(c);
                }
            }

            traverse(c);
        }
    }

    public Set<Class<?>> getInterfacesForMethod(Class<?> cls, String methodName) {
        result.clear();
        theMethodName = methodName;
        traverse(cls);
        return result;
    }

}