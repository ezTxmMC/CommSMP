package de.commsmp.smp.listener.filter;

import de.commsmp.smp.SMP;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FilterExtractor {
    private List<String> vocabulary;
    private Instances structure;

    public FilterExtractor() {
        loadVocabulary();
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (String word : vocabulary) {
            attributes.add(new Attribute(word));
        }
        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("ok");
        classValues.add("flagged");
        attributes.add(new Attribute("class", classValues));

        structure = new Instances("ChatMessages", attributes, 0);
        structure.setClassIndex(structure.numAttributes() - 1);
    }

    private void loadVocabulary() {
        try {
            vocabulary = Files.readAllLines(Path.of(SMP.getInstance().getDataFolder().getAbsolutePath() + "/ai/vocabulary.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            vocabulary = new ArrayList<>();
        }
    }

    public Instance extractFilter(String message) {
        double[] values = new double[structure.numAttributes()];
        String[] tokens = message.toLowerCase().split("\\s+");
        for (int i = 0; i < vocabulary.size(); i++) {
            int count = 0;
            for (String token : tokens) {
                if (token.equals(vocabulary.get(i))) {
                    count++;
                }
            }
            values[i] = count;
        }
        values[structure.numAttributes() - 1] = Double.NaN;
        Instance instance = new DenseInstance(1.0, values);
        instance.setDataset(structure);
        return instance;
    }

    public Instances getStructure() {
        return structure;
    }
}
