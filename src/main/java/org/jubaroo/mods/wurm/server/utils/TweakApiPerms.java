package org.jubaroo.mods.wurm.server.utils;

import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TweakApiPerms {
    public static final ClassMap classmap = new ClassMap();

    public static Class getClass(String name) {
        return classmap.getClass(name);
    }

    public static boolean setItemField(Object item, String fieldName, Object valu) {

        Field field = null;
        Class clas = item.getClass();

        try {
            field = clas.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            RequiemLogging.logException(String.format("setItemField(): No Such Field: %s on %s", fieldName, clas.getName()), e);
            for (Field oops : clas.getDeclaredFields()) {
                String butHas = String.format("has field: %s", oops.getName());
                RequiemLogging.logInfo(butHas);
            }
            return false;
        }

        field.setAccessible(true);

        try {
            field.set(item, valu);
        } catch (IllegalAccessException e) {
            RequiemLogging.logException("setItemField Illegal: " + fieldName, e);
            return false;
        }
        return true;

    }

    public static Object getItemField(Object item, String fieldName) {

        Field field;
        Class clas = item.getClass();

        try {
            field = clas.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            RequiemLogging.logException(String.format("getItemField(): No Such Field: %s on %s", fieldName, clas.getName()), e);
            return null;
        }

        field.setAccessible(true);

        try {
            return field.get(item);
        } catch (IllegalAccessException e) {
            RequiemLogging.logException("getItemField Illegal: " + fieldName, e);
            return null;
        }
    }

    public static boolean setClassField(String className, String fieldName, Object item, Object valu) {

        Field field;

        Class clas = classmap.getClass(className);
        if (clas == null) {
            return false;
        }

        try {
            field = clas.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            RequiemLogging.logException(String.format("setItemField(): No Such Field: %s on %s", fieldName, clas.getName()), e);
            for (Field oops : clas.getDeclaredFields()) {
                String butHas = String.format("has field: %s", oops.getName());
                RequiemLogging.logInfo(butHas);
            }
            return false;
        }

        field.setAccessible(true);

        try {
            field.set(item, valu);
        } catch (IllegalAccessException e) {
            RequiemLogging.logException("setItemField Illegal: " + fieldName, e);
            return false;
        }
        return true;

    }

    public static Object getClassField(String className, String fieldName, Object item) {
        Field field;
        Class clas = classmap.getClass(className);
        if (clas == null) {
            return false;
        }
        try {
            field = clas.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            RequiemLogging.logException(String.format("getItemField(): No Such Field: %s on %s", fieldName, clas.getName()), e);
            return null;
        }
        field.setAccessible(true);
        try {
            return field.get(item);
        } catch (IllegalAccessException e) {
            RequiemLogging.logException("getItemField Illegal: " + fieldName, e);
            return null;
        }
    }


    public static Method getClassMeth(String cname, String mname, String... params) {

        Method meth;

        Class[] ptypes = classmap.getClassArray(params);
        if (ptypes == null) {
            return null;
        }

        Class clas = classmap.getClass(cname);
        if (clas == null) {
            return null;
        }

        try {
            meth = clas.getDeclaredMethod(mname, ptypes);
        } catch (NoSuchMethodException e) {
            RequiemLogging.logException("Method Not Found: " + mname, e);
            return null;
        }

        meth.setAccessible(true);
        return meth;
    }
}
