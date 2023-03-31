import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private BufferedReader reader;
    private PrintWriter writer;
    private String line;
    public Lexer(String inputFile, String outputFile) throws IOException {
        reader = new BufferedReader(new FileReader(inputFile));
        writer = new PrintWriter(new FileWriter(outputFile));
    }

    public void tokenize() throws IOException {
        // Define the width of each column
        int tokenWidth = 20;
        int lexemeWidth = 20;

        writer.printf("%-"+tokenWidth+"s %-" + lexemeWidth + "s\n", "Token", "Lexeme");
        boolean insideMultiLineComment = false;
        while ((line = reader.readLine()) != null) {
            if (insideMultiLineComment) {
                if (line.contains("*/")) {
                    insideMultiLineComment = false;
                    line = line.substring(line.indexOf("*/") + 2);
                } else {
                    continue; // Skip this line since it's part of the multi-line comment
                }
            } else if (line.contains("/*")) {
                // If this line starts a multi-line comment, check if it also ends the comment
                if (line.contains("*/")) {
                    line = line.substring(0, line.indexOf("/*")) + line.substring(line.indexOf("*/") + 2);
                } else {
                    insideMultiLineComment = true;
                    line = line.substring(0, line.indexOf("/*"));
                }
            } else if (line.contains("//")) {
                // Remove single-line comments
                line = line.substring(0, line.indexOf("//"));
            }
            tokenizeTokens(line, tokenWidth, lexemeWidth);
        }
        writer.close();
        reader.close();
    }
    private void tokenizeTokens(String code, int tokenWidth, int lexemeWidth) {
        // define regular expressions

        String keywordRegex = "\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while)\\b";
        String operatorRegex = "[+\\-*/%&|^=!<>~?]";
        String parenthesisRegex = "[().{};,:\\[\\]]";
        String identifierRegex = "[a-zA-Z_][\\w.]*";
        String constantRegex = "-?\\d+(\\.\\d+)?([eE][-+]?\\d+)?";
        String stringLiteralRegex = "\"(?:[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*)\"";
        String characterLiteralRegex = "'(?:[^'\\\\]*(?:\\\\.[^'\\\\]*)*)'";

        Pattern pattern = Pattern.compile("(" + keywordRegex + ")|(" + operatorRegex + ")|(" + parenthesisRegex + ")|(" + constantRegex + ")|(" + stringLiteralRegex + ")|(" + characterLiteralRegex + ")|(" + identifierRegex + ")");
        Matcher matcher = pattern.matcher(code);
        // iterate through matches and write to output file
        while (matcher.find()) {
            String match = matcher.group();
            if (match.matches(keywordRegex)) {
                writer.printf("%-" + tokenWidth + "s %-" + lexemeWidth + "s\n", "Keyword", match);
            } else if (match.matches(operatorRegex)) {
                writer.printf("%-" + tokenWidth + "s %-" + lexemeWidth + "s\n", "Operator", match);
            } else if (match.matches(parenthesisRegex)) {
                writer.printf("%-" + tokenWidth + "s %-" + lexemeWidth + "s\n", "Separator", match);
            } else if (match.matches(identifierRegex)) {
                String[] idParts = match.split("\\.");
                for (String idPart : idParts) {
                    writer.printf("%-" + tokenWidth + "s %-" + lexemeWidth + "s\n", "Identifier", idPart);
                }
            } else if (match.matches(constantRegex)) {
                writer.printf("%-" + tokenWidth + "s %-" + lexemeWidth + "s\n", "Constant", match);
            } else if (match.matches(stringLiteralRegex)) {
                writer.printf("%-" + tokenWidth + "s %-" + lexemeWidth + "s\n", "String Literal", match);
            } else if (match.matches(characterLiteralRegex)) {
                writer.printf("%-" + tokenWidth + "s %-" + lexemeWidth + "s\n", "Character Literal", match);
            }
        }
    }
}

