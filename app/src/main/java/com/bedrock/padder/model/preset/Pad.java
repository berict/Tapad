package com.bedrock.padder.model.preset;

import com.google.gson.annotations.SerializedName;

public class Pad {

    @SerializedName("raw_id")
    private String rawId;

    @SerializedName("raw_id_up")
    private String rawIdUp;

    @SerializedName("raw_id_down")
    private String rawIdDown;

    @SerializedName("raw_id_left")
    private String rawIdLeft;

    @SerializedName("raw_id_right")
    private String rawIdRight;

    public Pad (String rawId) {
        this.rawId = rawId;
        this.rawIdUp = null;
        this.rawIdDown = null;
        this.rawIdLeft = null;
        this.rawIdRight = null;
    }

    public Pad(String rawId,
               String rawIdDown) {
        this.rawId = rawId;
        this.rawIdDown = rawIdDown;
        this.rawIdUp = null;
        this.rawIdLeft = null;
        this.rawIdRight = null;
    }

    public Pad(String rawId,
               String rawIdUp,
               String rawIdDown) {
        this.rawId = rawId;
        this.rawIdUp = rawIdUp;
        this.rawIdDown = rawIdDown;
        this.rawIdLeft = null;
        this.rawIdRight = null;
    }

    public Pad(String rawIdUp,
               String rawIdDown,
               String rawIdLeft,
               String rawIdRight) {
        this.rawId = rawIdUp;
        this.rawIdUp = rawIdUp;
        this.rawIdDown = rawIdDown;
        this.rawIdLeft = rawIdLeft;
        this.rawIdRight = rawIdRight;
    }

    public Pad (String rawId,
                String rawIdUp,
                String rawIdRight,
                String rawIdDown,
                String rawIdLeft) {
        this.rawId = rawId;
        this.rawIdUp = rawIdUp;
        this.rawIdDown = rawIdDown;
        this.rawIdLeft = rawIdLeft;
        this.rawIdRight = rawIdRight;
    }

    public String[] getAllRawId() {
        String raw[] = {rawId, rawIdUp, rawIdDown, rawIdLeft, rawIdRight};
        return raw;
    }

    public String getRaw() {
        return rawId;
    }

    public String getUp() {
        return rawIdUp;
    }

    public String getDown() {
        return rawIdDown;
    }

    public String getLeft() {
        return rawIdLeft;
    }

    public String getRight() {
        return rawIdRight;
    }
}
