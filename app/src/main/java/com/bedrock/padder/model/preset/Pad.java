package com.bedrock.padder.model.preset;

public class Pad {
    private Integer rawId;
    private Integer rawIdUp;
    private Integer rawIdDown;
    private Integer rawIdLeft;
    private Integer rawIdRight;

    public Pad (Integer rawId,
                Integer rawIdUp,
                Integer rawIdDown,
                Integer rawIdLeft,
                Integer rawIdRight) {
        this.rawId = rawId;
        this.rawIdUp = rawIdUp;
        this.rawIdDown = rawIdDown;
        this.rawIdLeft = rawIdLeft;
        this.rawIdRight = rawIdRight;
    }

    public Pad (Integer rawId) {
        this.rawId = rawId;
        this.rawIdUp = -1;
        this.rawIdDown = -1;
        this.rawIdLeft = -1;
        this.rawIdRight = -1;
    }

    public Integer getRaw() {
        return rawId;
    }

    public Integer[] getAllRawId() {
        Integer raw[] = {rawId, rawIdUp, rawIdDown, rawIdLeft, rawIdRight};
        return raw;
    }

    public Integer getUp() {
        return rawIdUp;
    }

    public Integer getDown() {
        return rawIdDown;
    }

    public Integer getLeft() {
        return rawIdLeft;
    }

    public Integer getRight() {
        return rawIdRight;
    }
}
