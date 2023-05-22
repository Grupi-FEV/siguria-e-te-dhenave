import java.util.LinkedHashMap;

public interface FreqAnalyzer {

    LinkedHashMap<String, LinkedHashMap<Character, Pair<Character, Double>>> decrypt(String input);

    Pair<String, LinkedHashMap<Character, Character>> encrypt(String input);
}
