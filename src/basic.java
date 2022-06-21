import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;

public class basic {
    public static void main(String[] args){
        MemorySegment segment = MemorySegment.allocateNative(10 * 4, MemorySession.openConfined());
        for (int i = 0 ; i < 10 ; i++) {
            segment.setAtIndex(ValueLayout.JAVA_INT, i, i);
        }
       System.out.println(segment.isMapped());

    }
}
