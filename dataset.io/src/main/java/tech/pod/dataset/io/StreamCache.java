package tech.pod.dataset.io;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/*StreamCache is a cache allowing for flow coming from a constant StreamOutput
to be batched and flushed by user input to allow for batch operations on the set*/
public class StreamCache < T > {
    String globalLogger;
    List < T > output;
    List < T > internal = new ArrayList < T > ();
    Logger logger = null;
    StreamCache(List < T > output, String...globalLogger) {
        this.output = output;
        if (globalLogger.length != 0) {
            this.globalLogger = globalLogger[0];
        }

        if (this.globalLogger != null) {
            logger = Logger.getLogger(this.globalLogger);
            logger.entering(this.globalLogger, "binaryDecode()");
        } else {
            logger = Logger.getLogger(StreamCache.class.getName());
            logger.entering(getClass().getName(), "binaryDecode()");
        }
    }
    List < T > flush() {
        internal.clear();
        logger.logp(Level.INFO, "StreamCache", "flush()", "Flushed cache");
        return output;
    }

    void add(List < T > toAdd) {
        internal.addAll(toAdd);
        logger.logp(Level.INFO, "StreamCache", "add()", "Added List<T> to internal List");
    }

    void add(T toAdd) {
        internal.add(toAdd);
        logger.logp(Level.INFO, "StreamCache", "add()", "Added T to internal List");
    }
}