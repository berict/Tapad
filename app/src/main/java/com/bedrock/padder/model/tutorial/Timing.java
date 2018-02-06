package com.bedrock.padder.model.tutorial;

public class Timing {

    int deck;
    int pad = -1;
    int gesture = -1;

    long listenTime;
    BroadcastListener listener = null;

    public Timing(int deck) {
        if (deck > 0 && deck <= 4) {
            this.deck = deck;
        } else {
            throw new IllegalArgumentException("not matching deck > 0 && deck <= 4");
        }

        setListenTime();
    }

    public Timing(int deck, int pad) {
        if (deck > 0 && deck <= 4) {
            this.deck = deck;
        } else {
            throw new IllegalArgumentException("not matching deck > 0 && deck <= 4");
        }

        if (pad >= 0 && pad <= 44) {
            this.pad = pad;
        } else {
            throw new IllegalArgumentException("not matching pad >= 0 && pad <= 44");
        }

        setListenTime();
    }

    public Timing(int deck, int pad, int gesture) {
        if (deck > 0 && deck <= 4) {
            this.deck = deck;
        } else {
            throw new IllegalArgumentException("not matching deck > 0 && deck <= 4");
        }
        if (pad >= 0 && pad <= 44) {
            this.pad = pad;
        } else {
            throw new IllegalArgumentException("not matching pad >= 0 && pad <= 44");
        }
        if (gesture > 0 && gesture <= 4) {
            this.gesture = gesture;
        } else {
            throw new IllegalArgumentException("not matching gesture > 0 && gesture <= 4");
        }

        setListenTime();
    }

    private void setListenTime() {
        this.listenTime = System.currentTimeMillis();
    }

    public Timing setDeck(int deck) {
        if (deck > 0 && deck <= 4) {
            this.deck = deck;
        } else {
            throw new IllegalArgumentException("not matching deck > 0 && deck <= 4");
        }
        return this;
    }

    public Timing setPad(int pad) {
        if (pad >= 0 && pad <= 44) {
            this.pad = pad;
        } else {
            throw new IllegalArgumentException("not matching pad >= 0 && pad <= 44");
        }
        return this;
    }

    public Timing setGesture(int gesture) {
        if (gesture > 0 && gesture <= 4) {
            this.gesture = gesture;
        } else {
            throw new IllegalArgumentException("not matching gesture > 0 && gesture <= 4");
        }
        return this;
    }

    public void setListener(BroadcastListener listener) {
        this.listener = listener;
    }

    @Override
    public String toString() {
        return "Timing{" +
                "deck=" + deck +
                ", pad=" + pad +
                ", gesture=" + gesture +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        boolean v1 = this.deck == ((Timing) obj).deck;
        boolean v2 = this.pad == ((Timing) obj).pad;
        boolean v3 = this.gesture == ((Timing) obj).gesture;
        return v1 && v2 && v3;
    }

    public interface BroadcastListener {
        void onBroadcast(Timing timing, int delay);
    }
}
