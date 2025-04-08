import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) {
        List<String> trainTexts = new ArrayList<>();
        List<String> trainLabels = new ArrayList<>();
        List<String> engLines = readResource("eng.txt");
        for (String line : engLines) {
            String text = line.replaceAll("^\\d+\\.\\s*", "").trim();
            if (!text.isEmpty()) {
                trainTexts.add(text);
                trainLabels.add("English");
            }
        }
        List<String> esLines = readResource("es.txt");
        for (String line : esLines) {
            String text = line.replaceAll("^\\d+\\.\\s*", "").trim();
            if (!text.isEmpty()) {
                trainTexts.add(text);
                trainLabels.add("Spanish");
            }
        }
        List<String> frLines = readResource("fr.txt");
        for (String line : frLines) {
            String text = line.replaceAll("^\\d+\\.\\s*", "").trim();
            if (!text.isEmpty()) {
                trainTexts.add(text);
                trainLabels.add("French");
            }
        }
        List<String> languageLabels = Arrays.asList("English", "Spanish", "French");
        double alpha = 0.1;
        MultiClassPerceptron model = new MultiClassPerceptron(languageLabels, 26, alpha);
        int epochs = 100;
        model.train(trainTexts, trainLabels, epochs);
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.println("Enter text to analyze (or type 'exit' to quit):");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;
            String prediction = model.predict(input);
            System.out.println("Prediction: " + prediction);
        }
        scanner.close();
    }

    private static List<String> readResource(String resourceName) {
        List<String> lines = new ArrayList<>();
        try (InputStream is = App.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) return lines;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
