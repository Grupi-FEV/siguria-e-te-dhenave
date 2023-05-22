import java.util.*;
import java.util.stream.Collector;

public class Kripto implements FreqAnalyzer {

    /**
     * Decrypted using the metrics below which were retrieved from analyzing 5 Albanian books
     * e -> 8.6602
     * ë -> 8.2824
     * t -> 7.8652
     * i -> 7.7404
     * a -> 7.1214
     * ---------
     * r -> 6.4997
     * n -> 6.1751
     * s -> 5.2385
     * h -> 4.5852
     * u -> 3.7900
     * d -> 3.5072
     * o -> 3.4474
     * k -> 3.3507
     * m -> 3.1678
     * j -> 3.0828
     * p -> 2.5370
     * l -> 2.3479
     * g -> 1.4474
     * v -> 1.3399
     * b -> 1.2656
     */
    Character[] letters = new Character[]{'e','ë', 't', 'i', 'a', 'r', 'n', 's', 'h', 'u', 'd', 'o', 'k', 'm', 'j', 'p', 'l', 'g', 'v', 'b', ',', '.', 'q', 'f', 'y', 'z', 'c', '-','ç'};

    public LinkedHashMap<String, LinkedHashMap<Character, Pair<Character, Double>>> decrypt(String input) {
        FrequencyAnalyzer fA = new FrequencyAnalyzer();

        String in = input.toLowerCase();
        fA.getCharacterCount(Arrays.stream(in.split(" ")).toList());

        // descending percentage map
        Map<Character, Double> descPercMap = fA.countPercentage(fA.descendingMapSort(fA.countMap));
//        descPercMap.forEach((k, v) -> System.out.println(k + " -> " + v));

        LinkedHashMap<String, LinkedHashMap<Character, Pair<Character, Double>>> result = new LinkedHashMap<>();
        //29, 34
        Character[][] c = new Character[34][];
        populateLetters(c);
        List<List<Character>> metricPermutations = Arrays.stream(c).map(Arrays::asList).toList();
        // List<List<Character>> metricPermutations = Arrays.stream(c).map(Arrays::asList).toList();
        metricPermutations.forEach(metric -> {
            LinkedHashMap<Character, Pair<Character, Double>> decrKeysRes = new LinkedHashMap<>();
            Map<Character, Character> decrKeys = new LinkedHashMap<>();
            int count = 0;
            for (Map.Entry<Character, Double> ent : descPercMap.entrySet()) {
                decrKeysRes.put(metric.get(count), new Pair<>(ent.getKey(), ent.getValue()));
                decrKeys.put(ent.getKey(), metric.get(count));
//                System.out.println(metric.get(count) + " " + ent.getKey() + " " + ent.getValue());
                if (count == metric.size() - 1)
                    break;
                count++;
            }
            result.put(charExchanger(in, decrKeys, '_'), decrKeysRes);
//            System.out.println(charExchanger(in, decrKeys, 'o'));
        });
        return result;
    }

    public Pair<String, LinkedHashMap<Character, Character>> encrypt(String input) {
        FrequencyAnalyzer fA = new FrequencyAnalyzer();

        String in = input.toLowerCase();
        fA.getCharacterCount(Arrays.stream(in.split(" ")).toList());
//        fA.countPercentage(fA.descendingMapSort(fA.countMap));

        List<Character> odL = fA.countMap.keySet().stream().toList();
        List<Character> uOdL = new ArrayList<>(odL);
        Collections.shuffle(uOdL);
        Deque<Character> oStack = new ArrayDeque<>(odL);
        Deque<Character> uoStack = new ArrayDeque<>(uOdL);

        LinkedHashMap<Character, Character> m = new LinkedHashMap<>();
        oStack.forEach(_unused -> m.put(oStack.pop(), uoStack.pop()));

//        m.forEach((k, v) -> System.out.println(k + " -> " + v));

        String s = charExchanger(in, m, null);
        return new Pair<>(s, m);
    }

    public String charExchanger(String s, Map<Character, Character> map, Character replace) {
        return s.chars().mapToObj(c -> (char) c).map(c -> (map.get(c) != null) ? map.get(c) : (replace == null || c == ' ') ? c : replace).collect(Collector.of(
                StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append,
                StringBuilder::toString));
    }

    public void populateLetters(Character[][] c){
        c[0] = new Character[] {'e', 'ë', 't', 'i', 'a'};
        c[1] = new Character[] {'ë', 'e', 't', 'i', 'a'};
        c[2] = new Character[] {'e', 't', 'ë', 'i', 'a'};
        c[3] = new Character[] {'e', 'ë', 'i', 't', 'a'};
        c[4] = new Character[] {'e', 'ë', 'i', 'a', 't'};
        c[5] = letters;
        int i = 6;
        int j = 0;
        System.out.println(c.length);
        while (i < c.length){
            Character[] temp = new Character[29];
            for (int k = 0; k < temp.length; k++) {
                if(j == k){
                    temp[k] = letters[k+1];
                    temp[k+1] = letters[k];
                    continue;
                }
                if(j+1 == k){
                    continue;
                }
                temp[k] = letters[k];
            }
            c[i] = temp;
            i++;
            j++;
        }
    }
}
