

import java.lang.foreign.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Arrays;

import static java.lang.foreign.SegmentAllocator.implicitAllocator;
import static java.lang.foreign.ValueLayout.JAVA_INT;


public class implementOffHeap {
    public static void main(String[] args) {
        // 1. Find foreign function on the C library path
        Linker linker = Linker.nativeLinker();
        SymbolLookup stdlib = linker.defaultLookup();
//        MethodHandle radixSort = linker.downcallHandle(
//                stdlib.lookup("radixsort"),null);
// 2. Allocate on-heap memory to store four strings
        String[] javaStrings = {"mouse", "cat", "dog", "car"};
// 3. Allocate off-heap memory to store four pointers
        SegmentAllocator allocator = implicitAllocator();
        MemorySegment offHeap = allocator.allocateArray(ValueLayout.ADDRESS, javaStrings.length);
// 4. Copy the strings from on-heap to off-heap
        for (int i = 0; i < javaStrings.length; i++) {
            // Allocate a string off-heap, then store a pointer to it
            MemorySegment cString = allocator.allocateUtf8String(javaStrings[i]);
            offHeap.setAtIndex(ValueLayout.ADDRESS, i, cString);
        }

// 5. Sort the off-heap data by calling the foreign function
//        try {
//            radixSort.invoke(offHeap, javaStrings.length, MemoryAddress.NULL, '\0');
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        }
// 6. Copy the (reordered) strings from off-heap to on-heap
        for (int i = 0; i < javaStrings.length; i++) {
            MemoryAddress cStringPtr = offHeap.getAtIndex(ValueLayout.ADDRESS, i);
            javaStrings[i] = cStringPtr.getUtf8String(0);
        }
       System.out.println( Arrays.equals(javaStrings, new String[]{"car", "cat", "dog", "mouse"}));  // true
        ((MemorySession)offHeap).close();

        try (MemorySession session = MemorySession.openConfined()) {
            SegmentAllocator allocatorYO = SegmentAllocator.newNativeArena(session);
            for (int i = 0 ; i < 100 ; i++) {
                MemorySegment s = allocatorYO.allocateArray(JAVA_INT,
                        new int[] { 1, 2, 3, 4, 5 });
            }
        } // all memory allocated is released here, essentially after the try shit gets released back

    }
}
