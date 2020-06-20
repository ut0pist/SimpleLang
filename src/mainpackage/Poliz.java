package mainpackage;

import Exceptions.*;

import java.util.ArrayList;

public class Poliz {
    private ArrayList<Token> tokens;
    private ArrayList<Token> poliz = new ArrayList<>();
    private ArrayList<Token> temp_stack = new ArrayList<>();
    private int current_token_index = 0;
    private int unconditional_mark_value = 0; /** Значения меток безусловного перехода. Для конструкции else это значение может быть использовано, как индекс **/
    private int conditional_mark_index = 0; /** Индекс меток условного перехода **/
    private ArrayList<Integer> marks_values_array = new ArrayList<>(); /** массив значений меток БП **/
    private ArrayList<Integer> marks_index_array = new ArrayList<>(); /** массив индексов меток УП **/


    public ArrayList<Token> get_poliz() {
        this.create_poliz();
        return poliz;
    }

    public Poliz(String input_str) throws WrongSymbolException, NoMatchException, ValueErrorException, WrongLogicExprException, EndOfFileException {
        Parser parser = new Parser(input_str);
        parser.lang();
        tokens = parser.get_tokens();
    }

    public void create_poliz() {
        while (current_token_index < tokens.size()) {
            if(tokens.get(current_token_index).GetTokenType() == Lexems.VAR_TYPE){
                temp_stack.add(tokens.get(current_token_index));
                current_token_index++;
            }

            if(tokens.get(current_token_index).GetTokenType() == Lexems.FULL_STOP){
                current_token_index++;
            }

            if(tokens.get(current_token_index).GetTokenType() == Lexems.FUNC_VOID_PARAM_1
                    || tokens.get(current_token_index).GetTokenType() == Lexems.FUNC_VOID_PARAM_2
                    || tokens.get(current_token_index).GetTokenType() == Lexems.FUNC_BOOLEAN_PARAM_1
                    || tokens.get(current_token_index).GetTokenType() == Lexems.FUNC_INT_PARAM_1){

                temp_stack.add(tokens.get(current_token_index));
                current_token_index++;
            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.IF_KW) {
                Token false_jump = new Token();
                Token mark = new Token();

                temp_stack.add(tokens.get(current_token_index));

                false_jump.SetTokenType(Lexems.JUMP);
                false_jump.SetTokenValue("!F");
                temp_stack.add(false_jump);

                mark.SetTokenType(Lexems.MARK);
                //mark.value = "p" + mark_index;
                temp_stack.add(mark);

                //mark_index++;
                current_token_index++;
            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.ELSE_KW) {
                Token unconditional_jump = new Token();
                Token mark = new Token();

                temp_stack.add(tokens.get(current_token_index));

                unconditional_jump.SetTokenType(Lexems.JUMP);
                unconditional_jump.SetTokenValue("!");
                mark.SetTokenType(Lexems.MARK);
                //mark.value = "p" + mark_index;

                poliz.add(mark);
                unconditional_mark_value = poliz.size() - 1;
                marks_index_array.add(unconditional_mark_value);
                poliz.add(unconditional_jump);

                //mark_index++;
                current_token_index++;
            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.WHILE_KW) {
                Token false_jump = new Token();
                Token mark = new Token();

                temp_stack.add(tokens.get(current_token_index));

                false_jump.SetTokenType(Lexems.JUMP);
                false_jump.SetTokenValue("!F");
                temp_stack.add(false_jump);

                mark.SetTokenType(Lexems.MARK);
                //mark.value = "p" + mark_index;
                temp_stack.add(mark);


                unconditional_mark_value = poliz.size(); //Присвоили метке безусловного перехода значение
                marks_values_array.add(unconditional_mark_value); //Добавили значение этой метки в массив значений меток

                //mark_index++;
                current_token_index++;

            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.VAR
                    || tokens.get(current_token_index).GetTokenType() == Lexems.DIGIT) {

                poliz.add(tokens.get(current_token_index));
                current_token_index++;
            }

            if(tokens.get(current_token_index).GetTokenType() == Lexems.COMMA){
                current_token_index++;
            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.ASSIGN_OP) {
                temp_stack.add(tokens.get(current_token_index));
                current_token_index++;
            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.OP
                    || tokens.get(current_token_index).GetTokenType() == Lexems.LOGIC_OP) {

                actions_with_operations();
            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.L_B) {
                temp_stack.add(tokens.get(current_token_index));
                current_token_index++;
            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.R_B) {
                while (temp_stack.get(temp_stack.size() - 1).GetTokenType() != Lexems.L_B) {
                    poliz.add(temp_stack.get(temp_stack.size() - 1));
                    temp_stack.remove(temp_stack.size() - 1);
                }
                temp_stack.remove(temp_stack.size() - 1);
                current_token_index++;
            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.L_CB) {
                while (!temp_stack.isEmpty()) {
                    if (temp_stack.get(temp_stack.size() - 1).GetTokenType() == Lexems.WHILE_KW
                            || temp_stack.get(temp_stack.size() - 1).GetTokenType() == Lexems.IF_KW) {

                        conditional_mark_index = poliz.size() - 2;
                        marks_index_array.add(conditional_mark_index); //Закинули в массив индекс метки УП
                        break;
                    }
                    if (temp_stack.get(temp_stack.size() - 1).GetTokenType() == Lexems.L_CB || temp_stack.get(temp_stack.size() - 1).GetTokenType() == Lexems.ELSE_KW) {
                        break;
                    }
                    poliz.add(temp_stack.get(temp_stack.size() - 1));
                    temp_stack.remove(temp_stack.size() - 1);
                }

                temp_stack.add(tokens.get(current_token_index));
                current_token_index++;
            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.SEMICOLON) {
                while (!temp_stack.isEmpty()) {
                    if (temp_stack.get(temp_stack.size() - 1).GetTokenType() == Lexems.L_CB) {
                        break;
                    }
                    poliz.add(temp_stack.get(temp_stack.size() - 1));
                    temp_stack.remove(temp_stack.size() - 1);
                }
                current_token_index++;
                if (current_token_index == tokens.size()) {
                    break;
                }
            }

            if (tokens.get(current_token_index).GetTokenType() == Lexems.R_CB) {
                temp_stack.remove(temp_stack.size() - 1);
                if (!temp_stack.isEmpty() && temp_stack.get(temp_stack.size() - 1).GetTokenType() == Lexems.WHILE_KW) {
                    Token mark = new Token();
                    Token unconditional_jump = new Token();

                    mark.SetTokenType(Lexems.MARK);
                    //mark.value = "p" + mark_index;
                    unconditional_jump.SetTokenType(Lexems.JUMP);
                    unconditional_jump.SetTokenValue("!");
                    //mark_index++;

                    poliz.add(mark);
                    poliz.add(unconditional_jump);

                    poliz.get(marks_index_array.get(marks_index_array.size() - 1)).SetTokenValue(Integer.toString(poliz.size())); //Присваиваем значение метке условного перехода
                    marks_index_array.remove(marks_index_array.size() - 1);
                    poliz.get(poliz.size() - 2).SetTokenValue(Integer.toString(marks_values_array.get(marks_values_array.size() - 1))); //Присваиваем значение метке безусловного перехода
                    marks_values_array.remove(marks_values_array.size() - 1);

                    temp_stack.remove(temp_stack.size() - 1);
                }

                if (!temp_stack.isEmpty() && temp_stack.get(temp_stack.size() - 1).GetTokenType() == Lexems.IF_KW) {
                    if ((current_token_index < tokens.size() - 1) && tokens.get(current_token_index + 1).GetTokenType() != Lexems.ELSE_KW) {
                        poliz.get(marks_index_array.get(marks_index_array.size() - 1)).SetTokenValue(Integer.toString(poliz.size()));

                    } else if ((current_token_index < tokens.size() - 1) && tokens.get(current_token_index + 1).GetTokenType() == Lexems.ELSE_KW) {
                        poliz.get(marks_index_array.get(marks_index_array.size() - 1)).SetTokenValue(Integer.toString((poliz.size() + 2)));
                    } else {
                        poliz.get(marks_index_array.get(marks_index_array.size() - 1)).SetTokenValue(Integer.toString(poliz.size()));
                    }
                    marks_index_array.remove(marks_index_array.size() - 1);
                    temp_stack.remove(temp_stack.size() - 1);

                }

                if (!temp_stack.isEmpty() && temp_stack.get(temp_stack.size() - 1).GetTokenType() == Lexems.ELSE_KW) {
                    poliz.get(marks_index_array.get(marks_index_array.size() - 1)).SetTokenValue(Integer.toString(poliz.size()));
                    marks_index_array.remove(marks_index_array.size() - 1);
                    temp_stack.remove(temp_stack.size() - 1);

                }
                current_token_index++;
            }
        }
    }

    private int get_token_priority(Token current_token) {
        int token_priority = 0;
        if (current_token.GetTokenType() == Lexems.LOGIC_OP) {
            token_priority = 1;
        }
        if (current_token.GetTokenValue().equals("+") || current_token.GetTokenValue().equals("-")) {
            token_priority = 1;
        } else if (current_token.GetTokenValue().equals("*") || current_token.GetTokenValue().equals("/")) {
            token_priority = 2;
        }
        return token_priority;
    }

    private void actions_with_operations() {
        if (this.temp_stack.isEmpty() || get_token_priority(temp_stack.get(temp_stack.size() - 1)) < get_token_priority(tokens.get(current_token_index))) {
            temp_stack.add(tokens.get(current_token_index));
        } else {
            while (get_token_priority(temp_stack.get(temp_stack.size() - 1)) >= get_token_priority(tokens.get(current_token_index))) {
                poliz.add(temp_stack.get(temp_stack.size() - 1));
                temp_stack.remove(temp_stack.size() - 1);
            }
            temp_stack.add(tokens.get(current_token_index));
        }
        current_token_index++;
    }
}