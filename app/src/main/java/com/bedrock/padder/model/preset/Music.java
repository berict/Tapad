package com.bedrock.padder.model.preset;

public class Music {

    private String name;

    private String fileName;

    private Boolean isGesture;

    private Integer soundCount;

    private Integer bpm;

    public Music(String name, String fileName, Boolean isGesture, Integer soundCount, Integer bpm) {
        this.name = name;
        this.fileName = fileName;
        this.isGesture = isGesture;
        this.soundCount = soundCount;
        this.bpm = bpm;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public Boolean getGesture() {
        return isGesture;
    }

    public Integer getSoundCount() {
        return soundCount;
    }

    public Integer getBpm() {
        return bpm;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}