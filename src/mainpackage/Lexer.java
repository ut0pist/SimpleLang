package mainpackage;

import Exceptions.WrongSymbolException;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Lexer {
    public ArrayList<Token> get_tokens(String input_str) throws WrongSymbolException {
        int counter; //Счетчик количества лексем. Используется, чтобы выйти из цикла поиска токенов.
        ArrayList<Token> tokens = new ArrayList<>();
        String buffer_str;

        while (!input_str.isEmpty()) {
            for (Lexems lexem : Lexems.values()) {
                counter = Lexems.values().length;
                Matcher matcher = lexem.pattern.matcher(input_str);
                if (matcher.find()) {
                    if (lexem == Lexems.SPACE) {
                        input_str = input_str.substring(matcher.end());
                        break;
                    }
                    buffer_str = input_str.substring(matcher.start(), matcher.end());
                    input_str = input_str.substring(matcher.end());

                    Token current_token = new Token();
                    current_token.SetTokenValue(buffer_str);
                    current_token.SetTokenType(lexem);
                    tokens.add(current_token);
                    break;
                }
                if (counter == 1 && !matcher.find()) {
                    throw new WrongSymbolException("Syntax error near \"" + input_str.substring(0, 1) + "\" symbol");
                }
                counter--;
            }
        }
        return tokens;
    }
}
