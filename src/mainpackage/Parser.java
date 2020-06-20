package mainpackage;

import Exceptions.*;
import java.util.ArrayList;


public class Parser {
    private Lexer lexer = new Lexer();
    private ArrayList<Token> tokens;
    private int current_token_index = 0;
    private boolean verified = false;
    private int prev_expr_index = 0;

    public boolean parsing_result() {
        return this.verified;
    }
    public ArrayList<Token> get_tokens() {
        return tokens;
    }

    public Parser(String input_str) throws WrongSymbolException {
        this.tokens = lexer.get_tokens(input_str);
    }

    public void lang() throws EndOfFileException, ValueErrorException, NoMatchException {
        while (this.current_token_index < this.tokens.size()) {
            expr();
        }
        this.verified = true;
    }

    private void expr() throws EndOfFileException, ValueErrorException, NoMatchException {
        try {
            assign_expr();
        } catch (NoMatchException e) {
            this.current_token_index = prev_expr_index;
            try {
                cycle_expr();
            } catch (NoMatchException e1) {
                this.current_token_index = prev_expr_index;
                try {
                    this.current_token_index = prev_expr_index;
                    condition_expr();
                } catch (NoMatchException e2) {
                    try {
                        this.current_token_index = prev_expr_index;
                        structure_declaration();
                    } catch (NoMatchException e3) {
                        try {
                            structure_operation();
                        } catch (NoMatchException e4) {
                            throw new NoMatchException("Syntax error at \"" + get_current_token().GetTokenValue() + "\" token on index " + current_token_index);
                        }
                    }
                }
            }
        }
    }

    private void assign_expr() throws EndOfFileException, ValueErrorException, NoMatchException {
        this.prev_expr_index = this.current_token_index;
        var();
        assign_op();
        value_expr();
        semicolon();
    }

    private void value_expr() throws EndOfFileException, ValueErrorException, NoMatchException {
        try {
            value();
        } catch (NoMatchException e1) {
            try {
                l_b();
                value_expr();
                r_b();
            } catch (NoMatchException e2) {
                throw new NoMatchException(e1.getMessage() + e2.getMessage());
            }
        }
        if (get_current_token().GetTokenType() == Lexems.OP) {
            op();
            value_expr();
        }
    }

    private void condition_expr() throws EndOfFileException, ValueErrorException, NoMatchException {
        if_expr();
        if (this.current_token_index < this.tokens.size() && this.get_current_token().GetTokenType() == Lexems.ELSE_KW) {
            else_expr();
        }
    }

    private void structure_declaration() throws EndOfFileException, ValueErrorException, NoMatchException {
        this.prev_expr_index = this.current_token_index;
        var_type();
        var();
        semicolon();
    }

    private void var_type() throws EndOfFileException, ValueErrorException, NoMatchException {
        match(get_current_token(), Lexems.VAR_TYPE);
    }

    private void structure_operation() throws EndOfFileException, ValueErrorException, NoMatchException {
        this.prev_expr_index = this.current_token_index;
        var();
        fullstop();
        try {
            func_void_param_1();
            one_param();
        } catch (NoMatchException e) {
            func_void_param_2();
            two_params();
        }
        semicolon();
    }



    private void fullstop() throws EndOfFileException, ValueErrorException, NoMatchException {
        match(get_current_token(), Lexems.FULL_STOP);
    }

    private void func_void_param_1() throws EndOfFileException, ValueErrorException, NoMatchException {
        match(get_current_token(), Lexems.FUNC_VOID_PARAM_1);
    }

    private void func_boolean_param_1() throws EndOfFileException, ValueErrorException, NoMatchException {
        match(get_current_token(), Lexems.FUNC_BOOLEAN_PARAM_1);
    }

    private void func_int_param_1() throws EndOfFileException, ValueErrorException, NoMatchException {
        match(get_current_token(), Lexems.FUNC_INT_PARAM_1);
    }

    private void func_void_param_2() throws EndOfFileException, ValueErrorException, NoMatchException {
        match(get_current_token(), Lexems.FUNC_VOID_PARAM_2);
    }


    private void one_param() throws EndOfFileException, ValueErrorException, NoMatchException {
        l_b();
        value();
        r_b();
    }

    private void two_params() throws EndOfFileException, ValueErrorException, NoMatchException {
        l_b();
        value();
        comma();
        value();
        r_b();
    }

    private void comma() throws EndOfFileException, ValueErrorException, NoMatchException {
        match(get_current_token(), Lexems.COMMA);
    }

    private void if_expr() throws EndOfFileException, ValueErrorException, NoMatchException {
        this.prev_expr_index = this.current_token_index;
        if_kw();
        head();
        body();
    }

    private void else_expr() throws EndOfFileException, ValueErrorException, NoMatchException {
        this.prev_expr_index = this.current_token_index;
        else_kw();
        body();
    }

    private void if_kw() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.IF_KW);
    }

    private void else_kw() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.ELSE_KW);
    }

    private void cycle_expr() throws EndOfFileException, ValueErrorException, NoMatchException {
        while_expr();
    }

    private void while_expr() throws EndOfFileException, ValueErrorException, NoMatchException {
        prev_expr_index = this.current_token_index;
        while_kw();
        head();
        body();
        //prev_expr_index = this.current_token_index;
    }

    private void while_kw() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.WHILE_KW);
    }

    private void head() throws EndOfFileException, ValueErrorException, NoMatchException {
        prev_expr_index = this.current_token_index;
        l_b();
        try {
            prev_expr_index = this.current_token_index;
            logic_expr();
        } catch (NoMatchException e) {
            current_token_index = prev_expr_index;
            var();
            fullstop();
            func_boolean_param_1();
            one_param();
        }
        r_b();
    }

    private void body() throws EndOfFileException, ValueErrorException, NoMatchException {
        boolean cycle_end = false;
        l_cb();

        expr();
        while (cycle_end == false) {
            try {
                expr();
            } catch (NoMatchException e) {
                cycle_end = true;
            }
        }

        r_cb();
    }

    private void l_cb() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.L_CB);
    }

    private void r_cb() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.R_CB);
    }

    private void l_b() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.L_B);
    }

    private void r_b() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.R_B);
    }

    private void logic_expr() throws EndOfFileException, ValueErrorException, NoMatchException {
        value();
        logic_op();
        value();

    }

    private void logic_op() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.LOGIC_OP);
    }


    private void semicolon() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.SEMICOLON);
    }

    private void value() throws EndOfFileException, ValueErrorException, NoMatchException {
        try{
            digit();
        }
        catch (NoMatchException e){
            var();
            if(tokens.get(current_token_index).GetTokenType() == Lexems.FULL_STOP){
                fullstop();
                func_int_param_1();
                one_param();
            }
        }
    }

    private void var() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.VAR);
    }

    private void digit() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.DIGIT);
    }

    private void op() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.OP);
    }

    private void assign_op() throws EndOfFileException, NoMatchException {
        match(get_current_token(), Lexems.ASSIGN_OP);
    }

    private void match(Token current_token, Lexems lexem) throws NoMatchException {
        if (current_token.GetTokenType() == lexem) {
            this.current_token_index++;
        } else {
            throw new NoMatchException("On " + current_token_index + "th index " + lexem.name() + " expected, but " + current_token.GetTokenType().name() + " found");
        }
    }

    private Token get_current_token() throws EndOfFileException {
        Token current_token = new Token();

        if (this.current_token_index < this.tokens.size()) {
            current_token = this.tokens.get(this.current_token_index);
        } else {
            throw new EndOfFileException("End of file");
        }

        return current_token;
    }
}