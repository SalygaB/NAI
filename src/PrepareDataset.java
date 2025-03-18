import java.util.*;

public class PrepareDataset {
    public PrepareDataset() {
    }

    public static void trainTestSplit(List<DataPoint> dataset, List<DataPoint> train, List<DataPoint> test) {
        Collections.shuffle(dataset, new Random());

        Map<String, List<DataPoint>> grouped = new HashMap<>();
        for (DataPoint dp : dataset) {
            grouped.computeIfAbsent(dp.label, k -> new ArrayList<>()).add(dp);
        }

        for (List<DataPoint> points : grouped.values()) {
            Collections.shuffle(points, new Random());
            int splitIndex = (int) (points.size() * 0.66);
            train.addAll(points.subList(0, splitIndex));
            test.addAll(points.subList(splitIndex, points.size()));
        }
    }
}