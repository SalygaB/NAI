import java.text.Normalizer;
import java.util.*;

public class MultiClassPerceptron {
    private final int numClasses;
    private final int inputSize;
    private final double[][] weights;
    private final double[] biases;
    private final double alpha;
    private final List<String> labels;

    public MultiClassPerceptron(List<String> labels, int inputSize, double alpha) {
        this.labels = new ArrayList<>(labels);
        this.numClasses = labels.size();
        this.inputSize = inputSize;
        this.alpha = alpha;
        this.weights = new double[numClasses][inputSize];
        this.biases = new double[numClasses];
        initializeWeights();
    }

    private void initializeWeights() {
        Random rand = new Random();
        for (int c = 0; c < numClasses; c++) {
            for (int i = 0; i < inputSize; i++) {
                weights[c][i] = (rand.nextDouble() * 0.02) - 0.01;
            }
            biases[c] = (rand.nextDouble() * 0.02) - 0.01;
        }
    }

    public int predictIndex(double[] x) {
        double maxScore = Double.NEGATIVE_INFINITY;
        int predictedIndex = -1;
        for (int c = 0; c < numClasses; c++) {
            double score = biases[c];
            for (int i = 0; i < inputSize; i++) {
                score += weights[c][i] * x[i];
            }
            if (score > maxScore) {
                maxScore = score;
                predictedIndex = c;
            }
        }
        return predictedIndex;
    }

    public String predict(String text) {
        double[] x = transformTextToVector(text);
        int idx = predictIndex(x);
        return labels.get(idx);
    }

    public void train(List<String> texts, List<String> trueLabels, int epochs) {
        int n = texts.size();
        Map<String, Integer> labelToIndex = new HashMap<>();
        for (int i = 0; i < labels.size(); i++) {
            labelToIndex.put(labels.get(i), i);
        }
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            indices.add(i);
        }
        Random rand = new Random();
        for (int epoch = 0; epoch < epochs; epoch++) {
            Collections.shuffle(indices, rand);
            for (int idx : indices) {
                String text = texts.get(idx);
                double[] x = transformTextToVector(text);
                int trueIdx = labelToIndex.get(trueLabels.get(idx));
                int predictedIdx = predictIndex(x);
                if (predictedIdx != trueIdx) {
                    for (int i = 0; i < inputSize; i++) {
                        weights[trueIdx][i] += alpha * x[i];
                        weights[predictedIdx][i] -= alpha * x[i];
                    }
                    biases[trueIdx] += alpha;
                    biases[predictedIdx] -= alpha;
                }
            }
        }
    }

    public void evaluate(List<String> testTexts, List<String> testLabels) {
        int[] tp = new int[numClasses];
        int[] fp = new int[numClasses];
        int[] fn = new int[numClasses];
        int[] tn = new int[numClasses];
        Map<String, Integer> labelToIndex = new HashMap<>();
        for (int i = 0; i < labels.size(); i++) {
            labelToIndex.put(labels.get(i), i);
        }
        for (int i = 0; i < testTexts.size(); i++) {
            String text = testTexts.get(i);
            String trueLabel = testLabels.get(i);
            String predLabel = predict(text);
            for (int c = 0; c < numClasses; c++) {
                String labelC = labels.get(c);
                boolean isTrue = trueLabel.equals(labelC);
                boolean isPred = predLabel.equals(labelC);
                if (isTrue && isPred) {
                    tp[c]++;
                } else if (!isTrue && isPred) {
                    fp[c]++;
                } else if (isTrue && !isPred) {
                    fn[c]++;
                } else {
                    tn[c]++;
                }
            }
        }
        for (int c = 0; c < numClasses; c++) {
            String label = labels.get(c);
            int TPi = tp[c], FPi = fp[c], FNi = fn[c], TNi = tn[c];
            double accuracy = (double)(TPi + TNi) / (TPi + TNi + FPi + FNi);
            double precision = (TPi + FPi) > 0 ? (double)TPi / (TPi + FPi) : 0;
            double recall = (TPi + FNi) > 0 ? (double)TPi / (TPi + FNi) : 0;
            double f1 = (precision + recall) > 0 ? 2 * precision * recall / (precision + recall) : 0;
            System.out.println("=== Evaluation for Language: " + label + " ===");
            System.out.println("TP = " + TPi + ", FP = " + FPi + ", FN = " + FNi + ", TN = " + TNi);
            System.out.printf("Accuracy = %.4f%n", accuracy);
            System.out.printf("Precision = %.4f%n", precision);
            System.out.printf("Recall = %.4f%n", recall);
            System.out.printf("F1-Score = %.4f%n", f1);
            System.out.println();
        }
    }

    public static double[] transformTextToVector(String text) {
        text = text.toLowerCase(Locale.ROOT);
        text = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        text = text.replaceAll("[^a-z]", "");
        int[] freq = new int[26];
        int total = 0;
        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                freq[c - 'a']++;
                total++;
            }
        }
        double[] vector = new double[26];
        for (int i = 0; i < 26; i++) {
            vector[i] = total > 0 ? (double) freq[i] / total : 0;
        }
        return vector;
    }
}
