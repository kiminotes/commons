package net.kiminotes.commons.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public final class JythonUtil {

    private static final Log LOG = LogFactory.getLog(JythonUtil.class);

    private static PythonInterpreter INTERPRETER;
    private static PyObject          METHOD_QUOTE_PLUS;

    static {
        try {
            INTERPRETER = new PythonInterpreter();
            INTERPRETER.exec("from urllib import quote_plus");
            METHOD_QUOTE_PLUS = INTERPRETER.get("quote_plus");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        if (METHOD_QUOTE_PLUS == null) {
            throw new RuntimeException("Failed to load urllib.quote_plus");
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("Load jython successfully");
        }
    }

    public static void load() {

    }

    public static String quote_plus(String string) {
        try {
            if (METHOD_QUOTE_PLUS != null) {
                PyString pyString = new PyString(string);
                PyObject result = METHOD_QUOTE_PLUS.__call__(pyString);
                return result.toString();
            }
        } catch (Throwable e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(e.getMessage());
            }
        }
        return string;
    }

    private JythonUtil() {
    }

}

