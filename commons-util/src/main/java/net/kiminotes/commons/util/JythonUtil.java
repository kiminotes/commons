package net.kiminotes.commons.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public final class JythonUtil {

    private static final Log LOGGER = LogFactory.getLog(JythonUtil.class);

    private static PythonInterpreter INTERPRETER;
    private static PyObject          METHOD_QUOTE_PLUS;
    private static PyObject          METHOD_UNQUOTE;
    private static PyObject          METHOD_QUOTE;

    static {
        try {
            INTERPRETER = new PythonInterpreter();
            INTERPRETER.exec("from urllib import quote_plus");
            INTERPRETER.exec("from urllib import unquote");
            INTERPRETER.exec("from urllib import quote");
            METHOD_QUOTE_PLUS = INTERPRETER.get("quote_plus");
            METHOD_UNQUOTE = INTERPRETER.get("unquote");
            METHOD_QUOTE = INTERPRETER.get("quote");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        if (METHOD_QUOTE_PLUS == null) {
            throw new RuntimeException("Failed to load urllib.quote_plus");
        }

        if (METHOD_UNQUOTE == null) {
            throw new RuntimeException("Failed to load urllib.unquote");
        }

        if (METHOD_QUOTE == null) {
            throw new RuntimeException("Failed to load urllib.quote");
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Load jython successfully");
        }
    }

    public static void preLoad() {

    }

    public static void setDefaultEncoding(String encoding) {
        if (StringUtil.isEmpty(encoding)) {
            throw new IllegalArgumentException("encoding is empty");
        }
        INTERPRETER.exec("import sys");
        INTERPRETER.exec("reload(sys)");
        INTERPRETER.exec("sys.setdefaultencoding('" + encoding + "')");
    }

    public static String unquote(String string) {
        try {
            if (METHOD_UNQUOTE != null) {
                PyString pyString = new PyString(string);
                PyObject result = METHOD_UNQUOTE.__call__(pyString);
                return result.__tojava__(String.class).toString();
            }
        } catch (Throwable e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Failed to execute unquote " + string, e);
            }
        }
        throw new RuntimeException("Failed to execute unquote " + string);
    }

    public static String quote(String string) {
        try {
            if (METHOD_QUOTE != null) {
                PyString pyString = new PyString(convert(string));
                PyObject result = METHOD_QUOTE.__call__(pyString);
                return result.__tojava__(String.class).toString();
            }
        } catch (Throwable e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Failed to execute quote " + string, e);
            }
        }
        throw new RuntimeException("Failed to execute quote " + string);
    }

    private static String convert(String string) throws UnsupportedEncodingException {
        if (StringUtil.isEmpty(string)) {
            return string;
        }
        return new String(string.getBytes("UTF-8"), "8859_1");
    }

    public static String quote_plus(String string) {
        try {
            if (METHOD_QUOTE_PLUS != null) {
                PyString pyString = new PyString(convert(string));
                PyObject result = METHOD_QUOTE_PLUS.__call__(pyString);
                return result.__tojava__(String.class).toString();
            }
        } catch (Throwable e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Failed to execute quote_plus " + string, e);
            }
        }
        throw new RuntimeException("Failed to execute quote_plus " + string);
    }

    private JythonUtil() {
    }

}
