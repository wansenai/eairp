/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
