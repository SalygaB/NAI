import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserInterface {
    private static KNearestNeighbours knn;
    private JFrame frame;
    private JTextField inputField;
    private JLabel resultLabel;
    private JLabel accuracyLabel;
    private JComboBox<Integer> kSelector;

    public UserInterface(KNearestNeighbours knn) {
        UserInterface.knn = knn;
        this.initializeUI();
    }

    private void initializeUI() {
        this.frame = new JFrame("KNN");
        this.frame.setDefaultCloseOperation(3);
        this.frame.setSize(500, 300);
        this.frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        JLabel instructionLabel = new JLabel("Enter observation (sepal_length, sepal_width, petal_length, petal_width):");
        this.inputField = new JTextField();
        JButton predictButton = new JButton("Predict");
        this.resultLabel = new JLabel("Predicted Class: ", 0);
        this.accuracyLabel = new JLabel("Accuracy: ", 0);
        Integer[] kValues = new Integer[]{1, 3, 5, 7, 9};
        this.kSelector = new JComboBox(kValues);
        this.kSelector.setSelectedIndex(1);
        predictButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserInterface.this.makePrediction();
            }
        });
        this.kSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserInterface.this.updateKValue();
            }
        });
        panel.add(instructionLabel);
        panel.add(this.inputField);
        panel.add(this.kSelector);
        panel.add(predictButton);
        this.frame.add(panel, "Center");
        this.frame.add(this.resultLabel, "South");
        this.frame.add(this.accuracyLabel, "North");
        this.frame.setVisible(true);
    }

    private void makePrediction() {
        try {
            double[] features = Arrays.stream(this.inputField.getText().split(",")).mapToDouble(Double::parseDouble).toArray();
            DataPoint newPoint = new DataPoint(features, "");
            String predictedClass = knn.predict(newPoint);
            this.resultLabel.setText("Predicted Class: " + predictedClass);
            this.updateAccuracy();
        } catch (NumberFormatException var4) {
            this.resultLabel.setText("Invalid input. Use numeric values separated by commas.");
        }

    }

    private void updateKValue() {
        int newK = (Integer)this.kSelector.getSelectedItem();
        knn = new KNearestNeighbours(newK, knn.getTrainDataset());
        this.updateAccuracy();
    }

    private void updateAccuracy() {
        ArrayList realLabels = new ArrayList();
        ArrayList predictedLabels = new ArrayList();
        Iterator var3 = knn.getTrainDataset().iterator();

        while(var3.hasNext()) {
            DataPoint dp = (DataPoint)var3.next();
            realLabels.add(dp.label);
            predictedLabels.add(knn.predict(dp));
        }

        double accuracy = EvaluationMetrics.measureAccuracy(realLabels, predictedLabels);
        JLabel var10000 = this.accuracyLabel;
        Object[] var10002 = new Object[]{accuracy * 100.0};
        var10000.setText("Accuracy: " + String.format("%.2f", var10002) + "%");
    }
}
