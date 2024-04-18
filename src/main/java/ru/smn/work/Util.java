package ru.smn.work;

import java.lang.reflect.Proxy;

public class Util {

    public Util() {
    }

    static public <T> T cache(T fr) {
        ClassLoader frClassLoader;
        Class[] interfaces;
        T proxyFr = null;

        //Получаем загрузчик класса у оригинального объекта
        frClassLoader = fr.getClass().getClassLoader();
        //Получаем все интерфейсы, которые реализует оригинальный объект
        interfaces = fr.getClass().getInterfaces();
        //настройка прокси
        InvocationHandler ih = new InvocationHandler(fr);
        proxyFr = (T) Proxy.newProxyInstance(frClassLoader, interfaces, ih);
        return proxyFr;
    }
}
