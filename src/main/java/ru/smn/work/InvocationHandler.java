package ru.smn.work;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InvocationHandler<T> implements java.lang.reflect.InvocationHandler {

    private T fr;
    private Map<String, String> annotationMutator = new HashMap<>();  //мапа "@Mutator"-"0/1"=false/true
    private Map<String, String> annotationCache = new HashMap<>();  //мапа "@Cache"-"0/1"=false/true
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
        if (annotationMutator.get(name) == "1") {   //есть аннотацая @Mutator
            ret = method.invoke(fr, args);
            this.mapCache.remove("cache");
        }
        if (annotationCache.get(name) == "1") {  //есть аннотацая @Cache
            if (this.mapCache.size() > 0) {
                ret = this.mapCache.get("cache");
            }
            else{
              //  System.out.println("------ invoke --------");
                ret = method.invoke(fr, args);
                this.mapCache.put("cache", ret);
              //  ret = this.mapCache.get("cache");
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
                String sann = (ann == null) ? "0" : "1";
                name = m.getName();
                annotationCache.put(name, sann);     //Запоминаем признак присутствия аннотации @Cache перед методом = "1"
                ann = m.getAnnotation(Mutator.class);
                sann = (ann == null) ? "0" : "1";
                annotationMutator.put(name, sann);    //Запоминаем признак присутствия аннотации @Mutator перед методом = "1"
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}