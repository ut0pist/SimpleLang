package mainpackage;

import Exceptions.*;
import HashSet.MyHashSet;
import LinkedList.MyLinkedList;
import java.util.ArrayList;


public class Project_main_class {
    public static void main(String[] args) throws WrongSymbolException, EndOfFileException, ValueErrorException, NoMatchException, WrongLogicExprException {
        String input_str = "List a; a.remove(5); a.add(10); a.add(20);";

        //x=((2)*3+2); y=x-5; z=0; List a; List b; HashSet m; HashSet n;" +
        //                "while (z<5){a.add(z); m.add(z); z=z+1;} if(m.contains(2)){n.add(a.get(2));}else{no=1;}" +
        //                "a.remove(3); m.add(4);
        //a = b+2; if(2<3) {d = 5; if(c<5){c=c+1;}a=b+1;}
        //if(2<3) {a=b+1;}
        //if(2<3) {a=b+1;} else {b = a;}
        //while(a<b) {a=b+1;}
        //a = b+2; while(a!=b) {if(2<3) {d = 5; if(c<5){c=c+1;}a=b+1;}}
        //while(a<b) {a=b+1;} if(a<b){c=c+2;}
        //while(a<b) {a=b+1; while(a!=b){a=b;}}
        //while(a<b) {a=b+1; if(a!=b){a=b;} else{c=d;}}
        //List a; a.add(5); b = a.get(0);


        /*Lexer lexer = new Lexer();
        ArrayList<Token> tokens = lexer.get_tokens(input_str);
        for(int i = 0; i < tokens.size(); i++){
            System.out.println(tokens.get(i).GetTokenValue());
        }

        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");*/

        /*Parser parser = new Parser(input_str);
        parser.lang();
        boolean parser_res = parser.parsing_result();
        System.out.println(parser_res);*/

        Poliz poliz = new Poliz(input_str);
        /*for(int i = 0; i<poliz.get_poliz().size(); i++) {
            System.out.println(poliz.get_poliz().get(i).GetTokenValue());
        }*/

        StackMachine stackMachine = new StackMachine(poliz.get_poliz());
        stackMachine.do_calculations();

        /** Вывод переменных **/
        System.out.println("Vars: ");
        System.out.println(stackMachine.getVarsHashMap());
        System.out.println("\r");

        /** Вывод листов **/
        for(MyLinkedList list : stackMachine.getLinkedListsHashMap().values()){
            System.out.println("List " + list.getName() + ":");
            for(int i = 0; i < list.getSize(); i++){
                System.out.println(list.GetElement(i));
            }
            System.out.println("\r");
        }

        /** Вывод хэшсетов **/
        for(MyHashSet hashSet : stackMachine.getHashSetHashMap().values()){
            System.out.println("HashSet " + hashSet.getName() + ":");
            for(int i = 0; i < 4; i++){
                if(hashSet.getAllLists()[i].getSize() != 0){
                    for(int j = 0; j < hashSet.getAllLists()[i].getSize(); j++){
                        System.out.println(hashSet.getAllLists()[i].GetElement(j));
                    }
                }
            }
            System.out.println("\r");
        }
    }
}
