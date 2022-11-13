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
    private static <E> int binarySearch(ListIterator<E> listIterator, E key){
        int low = 0;
        int high= listIterator.previousIndex();
        while (low <= high) {
            int mid = (low + high) >>> 1;
            Comparable<E> midValue = (Comparable<E>) get(listIterator, mid);
            int comparator = midValue.compareTo(key);

            if (comparator < 0)
                low = mid + 1;
            else if (comparator > 0)
                high = mid - 1;
            else
                return mid;
        }
        //  When the list is empty we return the first element index of the list which will be always -1
        return -(low + 1);  // key not found
    }

    private static <E> E get(ListIterator<E> i, int index) {
        E objectE = null;
        int position = i.nextIndex();
        if (position <= index) {
            do {
                objectE = i.next();
            } while (position++ < index);
        } else {
            do {
                objectE = i.previous();
            } while (--position > index);
        }
        return objectE;
    }

    private static void move(ListIterator<?> i, int index) {
        int position = i.nextIndex();
        if (position==index)
            return;

        if (position < index) {
            do {
                i.next();
            } while (++position < index);
        }
        else {
            do {
                i.previous();
            } while (--position > index);
        }
    }


    /**
     * @param sortedArrayList
     * @param key
     * @param <E>
     *  Loop through the activity sortedArrayList and  call the insert method from the sortedArrayList class
     *  here we need to send the activity objects one by one and also an empty arraylist of <activity> initially
     *  insert method in the sortedArrayList class search for the activity object in the provided arrayList i.e expectedArrayList
     *  When the first activity is sent it will get added to the sortedArrayList and returned
     *  to,In, the next loop we will search for the activity in the sortedArrayList by binary search technique
     *  By getting the mid-value and compare the object by calling the compare to method based on that we will return the position i.e index
     *  once we get the index , we will call the move method from the sortedArrayList class to change the position of the sortedArrayList element
     *  after that we will the add object in the provided sortedArrayList
     */
    <E> void insert(List<E> sortedArrayList, E key){
        ListIterator<E> listIterator= sortedArrayList.listIterator(sortedArrayList.size());
        int index = binarySearch(listIterator, key);
        if (index<0){
            index=~index;
        }
        move(listIterator, index);
        ((ListIterator<E>)listIterator).add(key);
        listIterator.nextIndex();
    }


}
