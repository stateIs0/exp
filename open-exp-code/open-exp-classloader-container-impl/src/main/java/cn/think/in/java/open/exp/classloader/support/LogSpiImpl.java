package cn.think.in.java.open.exp.classloader.support;

import cn.think.in.java.open.exp.classloader.LogSpi;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/28
 * @version 1.0
 **/
@Slf4j
public class LogSpiImpl implements LogSpi {
    @Override
    public void enhance(Class c) {
        try {
            Field field = c.getDeclaredField("log");
            int modify = field.getModifiers();

            if (field.getType().isPrimitive() && Modifier.isFinal(modify)) {
                return;
            }

            if (!Modifier.isPublic(modify) || Modifier.isFinal(modify)) {
                field.setAccessible(true);
            }

            boolean removeFinal = Modifier.isStatic(modify) && Modifier.isFinal(modify);
            if (removeFinal) {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, modify & ~Modifier.FINAL);
            }
            field.set(null, LoggerFactory.getLogger(c.getSimpleName() + "_ExpPlugin"));
        } catch (Exception e) {

        }
    }
}
