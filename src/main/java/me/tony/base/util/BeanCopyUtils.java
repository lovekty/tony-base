package me.tony.base.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.apache.commons.beanutils.ConvertUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by tony on 2017/2/28.
 */
public final class BeanCopyUtils {
    private static final int cacheCapacity = 1 << 10;
    private static final Converter converter = (value, targetClass, context) -> ConvertUtils.convert(value, targetClass);
    private static final Cache<Key, BeanCopier> cache = CacheBuilder.newBuilder().expireAfterAccess(12, TimeUnit.HOURS).maximumSize(cacheCapacity).build();

    private BeanCopyUtils() {
    }

    public static <S, T> T copy(S source, Class<T> targetClass) throws IllegalAccessException, ExecutionException, InstantiationException {
        return copy(source, targetClass, source.getClass() != targetClass);
    }

    public static <S, T> T copy(S source, Class<T> targetClass, boolean useConverter) throws IllegalAccessException, InstantiationException, ExecutionException {
        BeanCopier copier = getBeanCopier(source.getClass(), targetClass, useConverter);
        T target = targetClass.newInstance();
        copier.copy(source, target, useConverter ? converter : null);
        return target;
    }

    public static <S, T> void copy(S source, T target) throws ExecutionException {
        copy(source, target, source.getClass() != target.getClass());
    }

    public static <S, T> void copy(S source, T target, boolean useConverter) throws ExecutionException {
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass(), useConverter);
        copier.copy(source, target, useConverter ? converter : null);
    }

    public static <S, T> T copySilence(S source, Class<T> targetClass) {
        try {
            return copy(source, targetClass);
        } catch (IllegalAccessException | ExecutionException | InstantiationException e) {
            throw new BeanCopyException(e);
        }
    }

    public static <S, T> List<T> listCopy(List<S> sourceList, final Class<T> targetClass) {
        return sourceList.stream().map(s -> copySilence(s, targetClass)).collect(Collectors.toList());
    }

    private static <S, T> Key<S, T> cacheKeyGen(Class<S> sourceClass, Class<T> targetClass, boolean useConverter) {
        return new Key<>(sourceClass, targetClass, useConverter);
    }

    private static <S, T> BeanCopier getBeanCopier(Class<S> sourceClass, Class<T> targetClass, boolean useConverter) throws ExecutionException {
        Key<S, T> cacheKey = cacheKeyGen(sourceClass, targetClass, useConverter);
        return cache.get(cacheKey, () -> BeanCopier.create(sourceClass, targetClass, useConverter));
    }

    private static final class Key<S, T> implements Serializable {

        private static final long serialVersionUID = 1359775567655222904L;
        Class<S> sourceClass;
        Class<T> targetClass;
        boolean userConverter;

        Key(Class<S> sourceClass, Class<T> targetClass, boolean userConverter) {
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
            this.userConverter = userConverter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key<?, ?> key = (Key<?, ?>) o;
            return userConverter == key.userConverter &&
                    Objects.equals(sourceClass, key.sourceClass) &&
                    Objects.equals(targetClass, key.targetClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceClass, targetClass, userConverter);
        }
    }

    public static final class BeanCopyException extends RuntimeException {

        private static final long serialVersionUID = 7122836291489379041L;

        public BeanCopyException() {
        }

        public BeanCopyException(String message) {
            super(message);
        }

        public BeanCopyException(String message, Throwable cause) {
            super(message, cause);
        }

        public BeanCopyException(Throwable cause) {
            super(cause);
        }
    }
}
