package com.wansenai.utils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;

/**
 *
 */
public class AnnotationUtil {
    public static <A extends Annotation> A getAnnotation(Class<?> cls, Class<A> annotationClass) {
        A res = cls.getAnnotation(annotationClass);
        if (res == null) {
            for (Annotation annotation : cls.getAnnotations()) {
                if (annotation instanceof Documented) {
                    break;
                }
                res = getAnnotation(annotation.annotationType(), annotationClass);
                if (res != null)
                    break;
            }
        }
        return res;
    }

    public static <T, A extends Annotation> A getAnnotation(T obj, Class<A> annotationClass) {
        return getAnnotation(obj.getClass(), annotationClass);
    }
}
