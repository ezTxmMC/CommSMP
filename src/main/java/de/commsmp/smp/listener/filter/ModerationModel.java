package de.commsmp.smp.listener.filter;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class ModerationModel {
    private NaiveBayesUpdateable classifier;
    private Instances trainingData;
    private FilterExtractor extractor;

    public ModerationModel() throws Exception {
        extractor = new FilterExtractor();
        trainingData = extractor.getStructure();
        classifier = new NaiveBayesUpdateable();
        classifier.buildClassifier(trainingData);
    }

    public void updateModel(String message, String label) throws Exception {
        Instance instance = extractor.extractFilter(message);
        instance.setClassValue(label);
        instance.setDataset(trainingData);
        classifier.updateClassifier(instance);
        trainingData.add(instance);
    }

    public String classify(String message) throws Exception {
        Instance instance = extractor.extractFilter(message);
        double[] distribution = classifier.distributionForInstance(instance);
        int classIndex = distribution[1] > distribution[0] ? 1 : 0;
        return trainingData.classAttribute().value(classIndex);
    }

    public void saveModel(String filename) throws Exception {
        SerializationHelper.write(filename, classifier);
    }

    public void loadModel(String filename) throws Exception {
        classifier = (NaiveBayesUpdateable) SerializationHelper.read(filename);
    }
}
