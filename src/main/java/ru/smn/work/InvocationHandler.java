package ru.smn.work;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InvocationHandler<T> implements java.lang.reflect.InvocationHandler {

    private T fr;
    private Map<String, Boolean> annotationMutator = new HashMap<>();  //мапа: у метода есть "@Mutator"- false/true
    private Map<String, Boolean> annotationCache = new HashMap<>();  //мапа: у метода есть "@Cache"-false/true
    private Map<String, Object> mapCache = new HashMap<>();  //мапа "Cache"-Object=кэш

    public Map<String, Object> getMapCache() {
        return mapCache;
    }

    public InvocationHandler(T fr) {

        this.fr = fr;
        initProxy();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = new Object();
        String name = method.getName();
        if (annotationMutator.get(name)) {   //есть аннотацая @Mutator
            ret = method.invoke(fr, args);
            this.mapCache.clear(); //очищаем всю мапу
        }
        if (annotationCache.get(name)) {  //есть аннотацая @Cache
            if (this.mapCache.size() > 0) {
                ret = this.mapCache.get(name);
            } else {
                ret = method.invoke(fr, args);
                this.mapCache.put(name, ret);
            }
        }
        return ret;
    }

    public void initProxy() {
        String name;
        try {
            Method[] methods = fr.getClass().getDeclaredMethods();
            for (Method m : methods) {
                Annotation ann = m.getAnnotation(Cache.class);
                Boolean bann = (ann == null) ? false : true;
                name = m.getName();
                annotationCache.put(name, bann);     //Запоминаем признак присутствия аннотации @Cache перед методом = "1"
                ann = m.getAnnotation(Mutator.class);
                bann = (ann == null) ? false : true;
                annotationMutator.put(name, bann);    //Запоминаем признак присутствия аннотации @Mutator перед методом = "1"
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}