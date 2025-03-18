import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public Main() {
    }

    public static void main(String[] args) throws IOException {
        List<DataPoint> dataset = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("eyedata.csv"));
        boolean isHeader = true;

        String line;
        while((line = br.readLine()) != null) {
            if (isHeader) {
                isHeader = false;
            } else {
                String[] parts = line.split(",");

                try {
                    double[] features = Arrays.stream(parts, 0, parts.length - 1).mapToDouble(Double::parseDouble).toArray();
                    dataset.add(new DataPoint(features, parts[parts.length - 1]));
                } catch (NumberFormatException var12) {
                    System.err.println("Skipping invalid row: " + line);
                }
            }
        }

        br.close();
        List<DataPoint> train = new ArrayList<>();
        List<DataPoint> test = new ArrayList<>();
        PrepareDataset.trainTestSplit(dataset, train, test);
        KNearestNeighbours knn = new KNearestNeighbours(3, train);
        List<String> realLabels = new ArrayList<>();
        List<String> predictedLabels = new ArrayList<>();

        for (DataPoint dp : test) {
            realLabels.add(dp.label);
            predictedLabels.add(knn.predict(dp));
        }


        new UserInterface(knn);
    }
}
