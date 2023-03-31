import java.io.IOException;

// Press ⇧ twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.


public class Main {
    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer("input_scode.txt", "output.txt");
            lexer.tokenize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}