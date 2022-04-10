package com.epam.esm.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The Object utils class.
 * Provides a set of tools to work convenient with java objects
 */
public final class ObjectUtils {

    private ObjectUtils() {
    }

    /**
     * Merge objects of one type.
     * This method perform merging inner state of source object to target one
     *
     * @param <T>           the objects type
     * @param source        the source object
     * @param target        the target object
     * @param excludedProps the excluded properties, they won't be merged
     */
    public static <T> void merge(T source, T target, String... excludedProps) {
        String[] nullProps = getNullPropertyNames(source);
        String[] props = Stream.concat(Arrays.stream(excludedProps), Arrays.stream(nullProps))
                .toArray(String[]::new);
        BeanUtils.copyProperties(source, target, props);
    }

    private static <T> String[] getNullPropertyNames(T source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for(PropertyDescriptor pd : pds) {
            T srcValue = (T) src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
