package net.kiminotes.commons.util;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public final class StringUtil {

    public static boolean isEmpty(String string) {
        return string == null
            || "".equals(string);
    }

    private StringUtil() {}

}
