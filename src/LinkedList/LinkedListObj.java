package LinkedList;

public class LinkedListObj {
    private Integer value;
    private LinkedListObj prev;
    private LinkedListObj next;


    public LinkedListObj(LinkedListObj prev_element, Integer current_element, LinkedListObj next_element) {
        this.prev = prev_element;
        this.value = current_element;
        this.next = next_element;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public LinkedListObj getPrev() {
        return prev;
    }

    public void setPrev(LinkedListObj prev) {
        this.prev = prev;
    }

    public LinkedListObj getNext() {
        return next;
    }

    public void setNext(LinkedListObj next) {
        this.next = next;
    }
}
