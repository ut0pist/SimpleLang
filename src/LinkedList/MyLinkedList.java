package LinkedList;

public class MyLinkedList {
    private String name;
    private LinkedListObj first;
    private LinkedListObj last;
    private int size = 0;

    public MyLinkedList() {
    }

    public MyLinkedList(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public LinkedListObj getFirst(){
        return first;
    }

    /*public void AddIntoStartOfList(int element) {
        LinkedListObj f = first;
        LinkedListObj NewElement = new LinkedListObj(null, element, f);
        first = NewElement;
        if (f == null) {
            last = NewElement;
        } else {
            f.setPrev(NewElement);
        }
        size++;
    }*/

    public void AddIntoEndOfList(int element) {
        LinkedListObj l = last;
        LinkedListObj NewElement = new LinkedListObj(l, element, null);
        last = NewElement;
        if (l == null) {
            first = last;
        } else {
            l.setNext(last);
        }
        size++;
    }

    public Integer GetElement(int index) {
        LinkedListObj newObj = GetObject(index);
        return newObj.getValue();
    }

    public void SetElement(int index, Integer element) {
        LinkedListObj newObj = GetObject(index);
        newObj.setValue(element);
    }

    public void RemoveFromList(int element_position) {
        LinkedListObj newObj = GetObject(element_position);
        if (HasNext(element_position)) {
            newObj.getNext().setPrev(newObj.getPrev());
        } else {
            last = newObj.getPrev();
        }
        if (HasPrevious(element_position)) {
            newObj.getPrev().setNext(newObj.getNext());
        } else {
            first = newObj.getNext();
        }
        size--;
    }

    private LinkedListObj GetObject(int index) {
        if (index < size) {
            LinkedListObj newObj = first;
            while (index > 0) {
                newObj = newObj.getNext();
                index--;
            }
            return newObj;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    private boolean HasNext(int element_position) {
        if (GetObject(element_position).getNext() != null)
            return true;
        else
            return false;
    }

    private boolean HasPrevious(int element_position) {
        if (GetObject(element_position).getPrev() != null)
            return true;
        else
            return false;
    }

    /**
     * Этот метод используется для реализации hashset
     **/
    public boolean Contains(int element) {
        LinkedListObj newObj = first;
        for (int temp_size = size; temp_size != 0; temp_size--) {
            if (newObj.getValue() == element) {
                return true;
            } else {
                newObj = newObj.getNext();
            }
        }
        return false;
    }

    /**
     * Этот метод используется для реализации hashset
     **/
    /*public void RemoveElementByValue(int element){
        LinkedListObj newObj = first;
        while(newObj.getValue() != element){
            newObj = newObj.getNext();
        }
        if (newObj.getNext() != null) {
            newObj.getNext().setPrev(newObj.getPrev());
        } else {
            last = newObj.getPrev();
        }
        if (newObj.getPrev() != null) {
            newObj.getPrev().setNext(newObj.getNext());
        } else {
            first = newObj.getNext();
        }
        size--;
    }*/

    /**
     * Этот метод используется для реализации hashset
     **/
    public int GetIndex(int element){
        int element_index;
        LinkedListObj newObj = first;
        for(element_index = 0; newObj.getValue() != element; element_index++){
            newObj = newObj.getNext();
        }
        return element_index;
    }

}