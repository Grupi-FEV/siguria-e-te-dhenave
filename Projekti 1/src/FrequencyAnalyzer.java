import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FrequencyAnalyzer {


    Map<Character, Integer> countMap = new HashMap<>();

    public void readFile(String path) throws Exception {
        List<String> result;
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            result = lines.map(s -> s.toLowerCase().split(" ")).flatMap(Arrays::stream).toList();//.stream().map( s -> Arrays.stream(s.split(" ")).collect());
        }
        getCharacterCount(result);
    }

    public Map<Character, Double> countPercentage(Map<Character, Integer> m) {
        int sum = m.values().stream().reduce(0, Integer::sum);
        Map<Character, Double> r = new LinkedHashMap<>(); m.forEach((k, v) -> r.put(k, (v / (double) sum) * 100.0)); //        r.forEach((k, v) -> System.out.println(k + " -> " + v));
        return r;
    }

    public void getCharacterCount(List<String> text) {
        text.forEach(data -> {
            for (int i = 0; i < data.length(); i++) {
                if (countMap.get(data.charAt(i)) == null)
                    countMap.put(data.charAt(i), 1);
                else
                    countMap.compute(data.charAt(i), (key, val) -> val + 1);
            }
        });
    }

    public Map<Character, Integer> descendingMapSort(Map<Character, Integer> map) {
        LinkedHashMap<Character, Integer> m = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> m.put(k.getKey(), k.getValue()));
        return m;
    }

    public void writeCsv(String path) throws IOException {
        FileWriter writer = new FileWriter(path);
        countPercentage(descendingMapSort(countMap)).forEach((ch, i) -> {
            try {
                writer.write(ch + "," + i+ "\n"); //+ "," + "BLUE\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        writer.close();
    }
}