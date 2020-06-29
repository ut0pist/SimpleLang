package mainpackage;

import LinkedList.*;
import HashSet.*;

import java.util.ArrayList;
import java.util.HashMap;

public class StackMachine {
    private ArrayList<Token> poliz;
    private ArrayList<Token> stack = new ArrayList<>();

    private int poliz_index;

    private Token temp_token = new Token();
    private Token logic_op_res = new Token();

    private MyLinkedList newList = null;
    private MyHashSet newHashSet = null;

    private HashMap<String, Integer> VarsHashMap = new HashMap<>();
    private HashMap<String, MyLinkedList> linkedListsHashMap = new HashMap<>();
    private HashMap<String, MyHashSet> hashSetHashMap = new HashMap<>();


    public HashMap<String, Integer> getVarsHashMap() {
        return VarsHashMap;
    }

    public HashMap<String, MyLinkedList> getLinkedListsHashMap() {
        return linkedListsHashMap;
    }

    public HashMap<String, MyHashSet> getHashSetHashMap() {
        return hashSetHashMap;
    }

    public StackMachine(ArrayList<Token> poliz) {
        this.poliz = poliz;
        this.poliz_index = 0;
    }

    public void do_calculations() {
        while (poliz_index < poliz.size()) {
            while (poliz.get(poliz_index).GetTokenType() == Lexems.VAR
                    || poliz.get(poliz_index).GetTokenType() == Lexems.DIGIT
                    || poliz.get(poliz_index).GetTokenType() == Lexems.MARK) {

                stack.add(poliz.get(poliz_index));
                poliz_index++;
            }
            if ((poliz.get(poliz_index).GetTokenType() == Lexems.OP
                    || poliz.get(poliz_index).GetTokenType() == Lexems.ASSIGN_OP)) {
                switch (poliz.get(poliz_index).GetTokenValue()) {
                    case ("+"):
                        temp_token.SetTokenValue(Integer.toString(add(stack.get(stack.size() - 2), stack.get(stack.size() - 1))));
                        move_operationRes_into_stack(temp_token);
                        poliz_index++;
                        break;
                    case ("-"):
                        temp_token.SetTokenValue(Integer.toString(subtraction(stack.get(stack.size() - 2), stack.get(stack.size() - 1))));
                        move_operationRes_into_stack(temp_token);
                        poliz_index++;
                        break;
                    case ("*"):
                        temp_token.SetTokenValue(Integer.toString(multiplication(stack.get(stack.size() - 2), stack.get(stack.size() - 1))));
                        move_operationRes_into_stack(temp_token);
                        poliz_index++;
                        break;
                    case ("/"):
                        temp_token.SetTokenValue(Integer.toString(division(stack.get(stack.size() - 2), stack.get(stack.size() - 1))));
                        move_operationRes_into_stack(temp_token);
                        poliz_index++;
                        break;
                    case ("="):
                        assign();
                        stack.remove(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        poliz_index++;
                        break;
                }
                continue; /** Нужно для завершения очередной итерации цикла, если нужный токен был найден **/
            }

            if (poliz.get(poliz_index).GetTokenType() == Lexems.LOGIC_OP) {
                switch (poliz.get(poliz_index).GetTokenValue()) {
                    case (">"):
                        logic_op_res.SetTokenType(Lexems.LOGIC_RES);
                        if (greater_than(stack.get(stack.size() - 2), stack.get(stack.size() - 1))) {
                            logic_op_res.SetTokenValue("true");
                        } else {
                            logic_op_res.SetTokenValue("false");
                        }
                        move_operationRes_into_stack(logic_op_res);
                        poliz_index++;
                        break;

                    case ("<"):
                        logic_op_res.SetTokenType(Lexems.LOGIC_RES);
                        if (less_than(stack.get(stack.size() - 2), stack.get(stack.size() - 1))) {
                            logic_op_res.SetTokenValue("true");
                        } else {
                            logic_op_res.SetTokenValue("false");
                        }
                        move_operationRes_into_stack(logic_op_res);
                        poliz_index++;
                        break;

                    case (">="):
                        logic_op_res.SetTokenType(Lexems.LOGIC_RES);
                        if (greater_or_equals(stack.get(stack.size() - 2), stack.get(stack.size() - 1))) {
                            logic_op_res.SetTokenValue("true");
                        } else {
                            logic_op_res.SetTokenValue("false");
                        }
                        move_operationRes_into_stack(logic_op_res);
                        poliz_index++;
                        break;

                    case ("<="):
                        logic_op_res.SetTokenType(Lexems.LOGIC_RES);
                        if (less_or_equals(stack.get(stack.size() - 2), stack.get(stack.size() - 1))) {
                            logic_op_res.SetTokenValue("true");
                        } else {
                            logic_op_res.SetTokenValue("false");
                        }
                        move_operationRes_into_stack(logic_op_res);
                        poliz_index++;
                        break;

                    case ("=="):
                        logic_op_res.SetTokenType(Lexems.LOGIC_RES);
                        if (equals(stack.get(stack.size() - 2), stack.get(stack.size() - 1))) {
                            logic_op_res.SetTokenValue("true");
                        } else {
                            logic_op_res.SetTokenValue("false");
                        }
                        move_operationRes_into_stack(logic_op_res);
                        poliz_index++;
                        break;

                    case ("!="):
                        logic_op_res.SetTokenType(Lexems.LOGIC_RES);
                        if (not_equals(stack.get(stack.size() - 2), stack.get(stack.size() - 1))) {
                            logic_op_res.SetTokenValue("true");
                        } else {
                            logic_op_res.SetTokenValue("false");
                        }
                        move_operationRes_into_stack(logic_op_res);
                        poliz_index++;
                        break;
                }
                continue;
            }

            if (poliz.get(poliz_index).GetTokenType() == Lexems.JUMP) {
                switch (poliz.get(poliz_index).GetTokenValue()) {
                    case ("!F"):
                        logic_op_res.SetTokenType(Lexems.LOGIC_RES);
                        if (stack.get(stack.size() - 2).GetTokenValue().equals("false")) {
                            poliz_index = Integer.parseInt(stack.get(stack.size() - 1).GetTokenValue());
                        } else {
                            poliz_index++;
                        }
                        break;
                    case ("!"):
                        poliz_index = Integer.parseInt(stack.get(stack.size() - 1).GetTokenValue());
                }
                continue;
            }

            if (poliz.get(poliz_index).GetTokenType() == Lexems.VAR_TYPE) {
                if (VarIsAvailable(stack.get(stack.size() - 1))) {
                    switch (poliz.get(poliz_index).GetTokenValue()) {
                        case ("List"):
                            MyLinkedList list = new MyLinkedList(stack.get(stack.size() - 1).GetTokenValue());
                            this.newList = list;
                            linkedListsHashMap.put(stack.get(stack.size() - 1).GetTokenValue(), this.newList);
                            break;
                        case ("HashSet"):
                            MyHashSet hashSet = new MyHashSet(stack.get(stack.size() - 1).GetTokenValue());
                            this.newHashSet = hashSet;
                            hashSetHashMap.put(stack.get(stack.size() - 1).GetTokenValue(), this.newHashSet);
                            break;
                    }
                    stack.remove(stack.size() - 1);
                    poliz_index++;
                    continue;
                } else
                    throw new RuntimeException("Variable \"" + stack.get(stack.size() - 1).GetTokenValue() + "\" already exists");
            }

            /** Проверяем функции, у которых одинаковое количество параметров. Это нужно для того, чтобы точно определить положение название листа в стэке. **/
            if (poliz.get(poliz_index).GetTokenType() == Lexems.FUNC_INT_PARAM_1
                    || poliz.get(poliz_index).GetTokenType() == Lexems.FUNC_BOOLEAN_PARAM_1
                    || poliz.get(poliz_index).GetTokenType() == Lexems.FUNC_VOID_PARAM_1) {
                if (list_exists(stack.get(stack.size() - 2))
                        || hashSet_exists(stack.get(stack.size() - 2))) {
                    switch (poliz.get(poliz_index).GetTokenValue()) {
                        case ("add"):
                            try {
                                get_list(stack.get(stack.size() - 2)).AddIntoEndOfList(get_token_value(stack.get(stack.size() - 1)));
                            } catch (RuntimeException e) {
                                try {
                                    get_HashSet(stack.get(stack.size() - 2)).add(get_token_value(stack.get(stack.size() - 1)));
                                } catch (RuntimeException e1) {
                                    throw new RuntimeException(e.getMessage() + e1.getMessage());
                                }
                            }
                            stack.remove(stack.size() - 1);
                            stack.remove(stack.size() - 1);
                            break;

                        case ("get"):
                            temp_token.SetTokenValue(Integer.toString(get_list(stack.get(stack.size() - 2)).GetElement(get_token_value(stack.get(stack.size() - 1)))));
                            move_operationRes_into_stack(temp_token);
                            break;

                        case ("remove"):
                            try {
                                get_list(stack.get(stack.size() - 2)).RemoveFromList(get_token_value(stack.get(stack.size() - 1)));
                            } catch (RuntimeException e) {
                                try {
                                    get_HashSet(stack.get(stack.size() - 2)).remove(get_token_value(stack.get(stack.size() - 1)));
                                } catch (RuntimeException e1) {
                                    throw new RuntimeException("Neither List nor " + e1.getMessage());
                                }
                            }
                            stack.remove(stack.size() - 1);
                            stack.remove(stack.size() - 1);
                            break;

                        case ("contains"):
                            if (get_HashSet(stack.get(stack.size() - 2)).contains(get_token_value(stack.get(stack.size() - 1)))) {
                                logic_op_res.SetTokenValue("true");
                            } else {
                                logic_op_res.SetTokenValue("false");
                            }
                            move_operationRes_into_stack(logic_op_res);
                            break;
                    }
                }
                poliz_index++;
                continue;
            }

            if (poliz.get(poliz_index).GetTokenType() == Lexems.FUNC_VOID_PARAM_2) {
                switch (poliz.get(poliz_index).GetTokenValue()) {
                    case ("set"):
                        get_list(stack.get(stack.size() - 3)).SetElement(get_token_value(stack.get(stack.size() - 2)), get_token_value(stack.get(stack.size() - 1)));
                        stack.remove(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        break;
                }
                poliz_index++;
                continue;
            }
        }
    }

    private int add(Token first_token, Token second_token) {
        int op_result = 0;
        op_result = get_token_value(first_token) + get_token_value(second_token);
        return op_result;
    }

    private int subtraction(Token first_token, Token second_token) {
        int op_result = 0;
        op_result = get_token_value(first_token) - get_token_value(second_token);
        return op_result;
    }


    private int multiplication(Token first_token, Token second_token) {
        int op_result = 0;
        op_result = get_token_value(first_token) * get_token_value(second_token);
        return op_result;
    }

    private int division(Token first_token, Token second_token) {
        int op_result = 0;
        op_result = get_token_value(first_token) / get_token_value(second_token);
        return op_result;
    }

    private boolean greater_than(Token first_token, Token second_token) {
        boolean logic_op_res = false;
        if (get_token_value(first_token) > get_token_value(second_token)) logic_op_res = true;
        return logic_op_res;
    }

    private boolean less_than(Token first_token, Token second_token) {
        boolean logic_op_res = false;
        if (get_token_value(first_token) < get_token_value(second_token)) logic_op_res = true;
        return logic_op_res;
    }

    private boolean greater_or_equals(Token first_token, Token second_token) {
        boolean logic_op_res = false;
        if (get_token_value(first_token) >= get_token_value(second_token)) logic_op_res = true;
        return logic_op_res;
    }

    private boolean less_or_equals(Token first_token, Token second_token) {
        boolean logic_op_res = false;
        if (get_token_value(first_token) >= get_token_value(second_token)) logic_op_res = true;
        return logic_op_res;
    }

    private boolean equals(Token first_token, Token second_token) {
        boolean logic_op_res = false;
        if (get_token_value(first_token) == get_token_value(second_token)) logic_op_res = true;
        return logic_op_res;
    }

    private boolean not_equals(Token first_token, Token second_token) {
        boolean logic_op_res = false;
        if (get_token_value(first_token) != get_token_value(second_token)) logic_op_res = true;
        return logic_op_res;
    }

    private void assign() {
        if(VarIsAvailable(stack.get(stack.size() - 2))){
            if (stack.get(stack.size() - 1).GetTokenType() != Lexems.VAR) {
                VarsHashMap.put(stack.get(stack.size() - 2).GetTokenValue(), Integer.parseInt(stack.get(stack.size() - 1).GetTokenValue()));
            } else if (stack.get(stack.size() - 1).GetTokenType() == Lexems.VAR & VarsHashMap.containsKey(stack.get(stack.size() - 1).GetTokenValue())) {
                VarsHashMap.put(stack.get(stack.size() - 2).GetTokenValue(), VarsHashMap.get(stack.get(stack.size() - 1).GetTokenValue()));
            }
        }
        else {
            throw new RuntimeException("Variable \"" + stack.get(stack.size() - 2).GetTokenValue() + "\" already exists");
        }
    }

    private void move_operationRes_into_stack(Token token) {
        Token new_token = new Token();
        new_token = token;
        stack.remove(stack.size() - 1);
        stack.remove(stack.size() - 1);
        if (token.GetTokenType() != Lexems.LOGIC_RES) {
            new_token.SetTokenType(Lexems.DIGIT);
        } else {
            new_token.SetTokenType(Lexems.LOGIC_RES);
        }
        stack.add(new_token);
    }

    private int get_token_value(Token token) {
        if (token.GetTokenType() == Lexems.VAR && VarsHashMap.containsKey(token.GetTokenValue())) {
            return VarsHashMap.get(token.GetTokenValue());
        } else if (token.GetTokenType() == Lexems.DIGIT) {
            return Integer.parseInt(token.GetTokenValue());
        } else {
            throw new RuntimeException("Variable \"" + token.GetTokenValue() + "\" is not initialized or is not a variable");
        }
    }

    private boolean list_exists(Token list_name) {
        if (linkedListsHashMap.containsKey(list_name.GetTokenValue())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hashSet_exists(Token hashSet_name) {
        if (hashSetHashMap.containsKey(hashSet_name.GetTokenValue())) {
            return true;
        } else {
            return false;
        }
    }

    private MyLinkedList get_list(Token list_name) {
        if (linkedListsHashMap.containsKey(list_name.GetTokenValue())) {
            return linkedListsHashMap.get(list_name.GetTokenValue());
        } else {
            throw new RuntimeException("List \"" + list_name.GetTokenValue() + "\" does not exist");
        }
    }

    private MyHashSet get_HashSet(Token hashset_name) {
        if (hashSetHashMap.containsKey(hashset_name.GetTokenValue())) {
            return hashSetHashMap.get(hashset_name.GetTokenValue());
        } else {
            throw new RuntimeException("HashSet \"" + hashset_name.GetTokenValue() + "\" does not exist");
        }
    }

    private boolean VarIsAvailable(Token var_token) {
        if (!linkedListsHashMap.containsKey(var_token.GetTokenValue())
                && !hashSetHashMap.containsKey(var_token.GetTokenValue())) {
            return true;
        } else return false;
    }
}