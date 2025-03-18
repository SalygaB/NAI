import java.util.*;

public class KNearestNeighbours {
    private int k;
    private List<DataPoint> trainDataset;

    public KNearestNeighbours(int k, List<DataPoint> trainDataset) {
        this.k = k;
        this.trainDataset = trainDataset;
    }

    private double calculateEuclideanDistance(DataPoint a, DataPoint b) {
        double sum = 0.0;
        for (int i = 0; i < a.features.length; i++) {
            sum += Math.pow(a.features[i] - b.features[i], 2.0);
        }
        return Math.sqrt(sum);
    }

    private List<DataPoint> sortDistances(DataPoint newPoint) {
        List<DataPoint> sortedList = new ArrayList<>(trainDataset);
        sortedList.sort(Comparator.comparingDouble(p -> calculateEuclideanDistance(p, newPoint)));
        return sortedList;
    }

    private String findPredictedClass(List<DataPoint> neighbors) {
        Map<String, Integer> classCounts = new HashMap<>();
        for (DataPoint dp : neighbors) {
            classCounts.put(dp.label, classCounts.getOrDefault(dp.label, 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(classCounts.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        int maxCount = sortedEntries.get(0).getValue();
        List<String> topClasses = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            if (entry.getValue() == maxCount) {
                topClasses.add(entry.getKey());
            }
        }

        return topClasses.get(new Random().nextInt(topClasses.size()));
    }

    public String predict(DataPoint newPoint) {
        List<DataPoint> sorted = sortDistances(newPoint);
        List<DataPoint> nearestNeighbors = sorted.subList(0, Math.min(k, sorted.size()));
        return findPredictedClass(nearestNeighbors);
    }

    public List<DataPoint> getTrainDataset() {
        return trainDataset;
    }
}
