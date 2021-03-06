package tech.pod.dataset.io;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
/*StreamOutput is a basic output for a StreamThread, 
coming from the output of a BinaryStreamParser, 
and adding the content an output list or a StreamCache or ScheduledStreamCache.
*/
public class StreamOutput < T > {
    ReentrantLock pauseLock;
    ReentrantLock stopLock;
    BinaryStreamParser < T > b;
    List < T > output;
    int sync;
    @SafeVarargs
    StreamOutput(ReentrantLock pauseLock, ReentrantLock stopLock, BinaryStreamParser < T > b, int sync, List < T > ...output) {
        this.b = b;
        this.pauseLock = pauseLock;
        this.stopLock = stopLock;
        this.sync = sync;
        if (output.length != 0) {
            this.output = output[0];
        }
    }

    public void output(List < T > sharedList) {

        while (!stopLock.isLocked() && !pauseLock.isLocked()) {
            output.addAll(sharedList);
        }
        if (stopLock.isLocked()) {
            return;
        }
    }

    public void output(StreamCache < T > c, List < T > sharedList) {
        while (!stopLock.isLocked() && !pauseLock.isLocked()) {
            c.add(sharedList);
        }
        if (stopLock.isLocked()) {
            return;
        }
    }

    public void output(ScheduledStreamCache < T > sc, List < T > sharedList) {
        while (!stopLock.isLocked() && !pauseLock.isLocked()) {
            sc.add(sharedList);
        }
        if (stopLock.isLocked()) {
            return;
        }
    }


}