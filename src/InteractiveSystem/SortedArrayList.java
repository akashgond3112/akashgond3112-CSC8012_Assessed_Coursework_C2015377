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

    @Override
    public boolean add(E element)
    {
        int i = 0;
        for (Iterator<E> it = this.iterator(); it.hasNext();)
        {
            Comparable<E> current = (Comparable<E>) it.next();
            int comparator = current.compareTo(element);
            if(comparator>0)
            {
                super.add(i, element);
                return true;
            }
            i++;
        }
        return super.add(element);
    }


}
