import org.junit.jupiter.api.Test;
import ru.smn.work.Fraction;
import ru.smn.work.Fractionable;
import ru.smn.work.Util;

import java.util.HashMap;
import java.util.Map;

public class Tests {
    @Test
    public void testCache() {
        Object ob = new Object();
        Object ob1 = new Object();
        Object ob2 = new Object();
        Fraction fr = new Fraction(33, 11);
        Fractionable num1 = Util.cache(fr);
        ob = num1.doubleValue();  //результат считается
        ob = num1.doubleValue();  //результат из кэша
        fr.setNum(24);            //вызов объекта "впрямую" - кэш не должна меняться
        fr.setDenum(6);           //вызов объекта "впрямую" - кэш не должна меняться
        ob1 = fr.doubleValue();   //вызов объекта "впрямую" - кэш не должна меняться
        ob2 = num1.doubleValue();  //вызов объекта "через прокси" - должны взять из кэша
        if (!ob.equals(ob2)) {
            throw new RuntimeException("test error - Результат либо не сохранился в кэш, либо взялся не из кэша!");
        }
        num1.setNum(33);    //кэш должна обновиться
        ob2 = num1.doubleValue();
        if (ob.equals(ob2)) {
            throw new RuntimeException("test error - Значение в кэш не обновилось!");
        }
    }
}

class  Runner implements Runnable{
    @Override
    public void run() {

    }
}
