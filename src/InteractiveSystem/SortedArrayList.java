package InteractiveSystem;

import java.util.*;

/*
* Derive a SortedArrayList<E> class as a subclass of the java.util.ArrayList<E> class in such a way that
the items of a sorted ArrayList are sorted in ascending order. This subclass of the ArrayList<E> class will
be needed to complete Task 2 (see Additional Assumptions (2) below). You only need to provide your new
insertion method in the SortedArrayList<E> class. You do not need to consider the other methods from
the ArrayList<E> class that can modify the list.
* */
@SuppressWarnings("unchecked")
public class SortedArrayList<E> extends ArrayList<E>{
    private static <E> int binarySearch(ListIterator<E> i, E key){
        int low = 0;
        int high= i.previousIndex();
        while (low <= high) {
            int mid = (low + high) >>> 1;
            Comparable<E> midVal = (Comparable<E>) get(i, mid);
            int cmp = midVal.compareTo(key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -(low + 1);  // key not found
    }

    private static <E> E get(ListIterator<E> i, int index) {
        E obj = null;
        int pos = i.nextIndex();
        if (pos <= index) {
            do {
                obj = i.next();
            } while (pos++ < index);
        } else {
            do {
                obj = i.previous();
            } while (--pos > index);
        }
        return obj;
    }

    private static void move(ListIterator<?> i, int index) {
        int pos = i.nextIndex();
        if (pos==index)
            return;

        if (pos < index) {
            do {
                i.next();
            } while (++pos < index);
        }
        else {
            do {
                i.previous();
            } while (--pos > index);
        }
    }

    <E> void insert(List<E> list, E key){
        ListIterator<E> i= list.listIterator(list.size());
        int idx = binarySearch(i, key);
        if (idx<0){
            idx=~idx;
        }
        move(i, idx);
        ((ListIterator<E>)i).add(key);
        i.nextIndex();
    }
}
