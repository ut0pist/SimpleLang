package HashSet;

import LinkedList.LinkedListObj;
import LinkedList.MyLinkedList;

public class MyHashSet {
    /**
     * Устанавливаем количество групп
     **/
    private int amount_of_groups = 4;
    private MyLinkedList[] group = new MyLinkedList[amount_of_groups];
    public String name;


    public MyHashSet(String name) {
        this.name = name;
        CreateLists();
    }

    public String getName(){
        return this.name;
    }

    public MyLinkedList[] getAllLists(){
        return group;
    }

    public int GetListSize(MyLinkedList list){
        return list.getSize();
    }

    public void add(int element) {
        if (!group[HashFunc(element)].Contains(element)) {
            group[HashFunc(element)].AddIntoEndOfList(element);
        }
    }

    public void remove(int element) {
        MyLinkedList newList = group[HashFunc(element)];
        LinkedListObj newObj = newList.getFirst();
        if (group[HashFunc(element)].Contains(element)) {
            newList.RemoveFromList(newList.GetIndex(element));
        }
    }

    public boolean contains(int element) {
        if (group[HashFunc(element)].Contains(element)) {
            return true;
        } else return false;
    }

    private int HashFunc(int element) {
        int HashKey;
        HashKey = element % this.amount_of_groups;
        return HashKey;
    }

    private void CreateLists() {
        for (int i = 0; i < amount_of_groups; i++) {
            this.group[i] = new MyLinkedList();
        }
    }
}