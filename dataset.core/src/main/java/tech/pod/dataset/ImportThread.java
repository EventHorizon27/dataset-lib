package tech.pod.dataset;
import java.util.concurrent.Callable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.*;
import java.nio.channels.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ImportThread implements Callable < ByteBuffer > {
    // private static final Logger logger =Logger.getLogger(ImportThread.class.getName());
    RandomAccessFile file;
    FileChannel inChannel = file.getChannel();
    ByteBuffer buf;
    int bytesAllocated;
    int start;
    String globalLogger;
    ImportThread(RandomAccessFile file, int bytesAllocated, int start, String...globalLogger) {
        this.globalLogger = globalLogger[0];
        this.file = file;
        this.bytesAllocated = bytesAllocated;
        this.start = start;
    }
    public ByteBuffer call() throws Exception {
        Logger logger = null;        
        if (globalLogger != null) {
            logger = Logger.getLogger(globalLogger);
            logger.entering(globalLogger, "call()");
           
        } else {
            
            logger = Logger.getLogger(ImportThread.class.getName());
            logger.entering(getClass().getName(), "call()");
        }
        

        buf = ByteBuffer.allocate(bytesAllocated);
        int bytesRead = 0;
        try {
            bytesRead = inChannel.read(buf); //read into buffer.
        } catch (Exception e) {
            e.printStackTrace();
            logger.logp(Level.WARNING, "ImportThread", "call()", "IOException", e);
        }

        while (bytesRead != -1) {

            buf.flip(); //make buffer ready for read

            //Debug
            /* while(buf.hasRemaining()){
                  System.out.print((char) buf.get()); // read 1 byte at a time
              }*/

            try {
                bytesRead = inChannel.read(buf, start);
            } catch (IOException e) {
                e.printStackTrace();
                logger.logp(Level.WARNING, "ImportThread", "call()", "IOException", e);
            }
        }
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.logp(Level.WARNING, "ImportThread", "call()", "IOException", e);
        }

        logger.logp(Level.INFO, "ImportThread", "call()", "Import complete");
        return buf;
    }
}