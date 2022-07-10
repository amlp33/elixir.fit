package com.example.yoga_;

public class Video {
    String exerciseName;
    String type;
    String videoLink;
    String targetedPart;

    public Video() {
    }

    public Video(String exerciseName, String type, String videoLink, String targetedPart) {
        this.exerciseName = exerciseName;
        this.type = type;
        this.videoLink = videoLink;
        this.targetedPart = targetedPart;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getTargetedPart() {
        return targetedPart;
    }

    public void setTargetedPart(String targetedPart) {
        this.targetedPart = targetedPart;
    }
}
