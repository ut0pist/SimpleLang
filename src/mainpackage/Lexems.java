package mainpackage;

import java.util.regex.*;

public enum Lexems {
    LOGIC_RES("^(true|false)"),
    VAR_TYPE("^(List|HashSet)"),
    FUNC_VOID_PARAM_1("^(add|remove)"),
    FUNC_BOOLEAN_PARAM_1("^contains"),
    FUNC_INT_PARAM_1("^get"),
    FUNC_VOID_PARAM_2("^set"),
    IF_KW("^if"),
    ELSE_KW("^else"),
    // FOR_KW ("^for"),
    WHILE_KW("^while"),
    VAR("^([a-zA-Z]+)"),
    DIGIT("^(0|[1-9][0-9]*)"),
    OP("^(\\+|-|\\*|/)"),
    LOGIC_OP("^(>|<|>=|<=|==|!=)"),
    ASSIGN_OP("^="),
    L_B("^\\("),
    R_B("^\\)"),
    L_CB("^\\{"),
    R_CB("^\\}"),
    SEMICOLON("^;"),
    COMMA("^,"),
    FULL_STOP("^\\."),
    SPACE("^\\s"),

    //Эти лексемы не должны быть учтены в лексере
    MARK("p(0|[1-9][0-9]*)"),
    JUMP("!F|!");


    public Pattern pattern;

    Lexems(String regexp) {
        pattern = Pattern.compile(regexp);
    }
}
