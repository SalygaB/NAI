import java.util.List;

class EvaluationMetrics {
    EvaluationMetrics() {
    }

    public static double measureAccuracy(List<String> real, List<String> predicted) {
        int correct = 0;

        for(int i = 0; i < real.size(); ++i) {
            if (real.get(i).equals(predicted.get(i))) {
                ++correct;
            }
        }

        return (double)correct / (double)real.size();
    }
}
