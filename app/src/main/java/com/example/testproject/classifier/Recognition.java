package com.example.testproject.classifier;

public class Recognition {
    private int label;
    private float confidence;
    private long timeCost;

    public Recognition(int label, float confidence, long timeCost) {
        this.label = label;
        this.confidence = confidence;
        this.timeCost = timeCost;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(long timeCost) {
        this.timeCost = timeCost;
    }
}
