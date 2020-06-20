package mainpackage;

public class Token {
    //TODO изменить поля на private и добавить геттеры и сеттеры
    private Lexems type;
    private String value;

    public Lexems GetTokenType(){
        return this.type;
    }
    public String GetTokenValue(){
        return this.value;
    }
    public void SetTokenType(Lexems type){
        this.type = type;
    }
    public void SetTokenValue(String value){
        this.value = value;
    }
}
