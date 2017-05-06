package me.tony.base.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tony on 2017/3/9.
 */
public final class EnumUtils {

    private EnumUtils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(EnumUtils.class);

    public static <ET extends Enum<ET>, ES extends Enum<ES>> ET transformSilence(ES source, Class<ET> targetEnumType) {
        try {
            return Enum.valueOf(targetEnumType, source.name());
        } catch (Exception e) {
            LOGGER.warn("enum transform exception", e);
            return null;
        }
    }
}
