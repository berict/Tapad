package com.bedrock.padder.model.tutorial;

import android.app.Activity;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.bedrock.padder.helper.FileHelper.PROJECT_LOCATION_PRESETS;

/* Example from
* https://developer.android.com/training/basics/network-ops/xml.html */

public class TutorialXmlParser {

    String tag;

    Activity activity = null;

    public TutorialXmlParser(String tag, Activity activity) {
        this.activity = activity;
        this.tag = tag;
    }

    // We don't use namespaces
    private static final String ns = null;

    public Tutorial parse() {
        try {
            return parse(new FileInputStream(PROJECT_LOCATION_PRESETS + "/" + tag + "/timing/timing.tpt"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("XMLParser", "File not found at " + PROJECT_LOCATION_PRESETS + "/" + tag + "/timing/timing.tpt");
            return null;
        }
    }

    public Tutorial parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            Log.i(getClass().getSimpleName(), "parse");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readTutorial(parser);
        } finally {
            in.close();
        }
    }

    private Tutorial readTutorial(XmlPullParser parser) throws XmlPullParserException, IOException {
        Tutorial tutorial = new Tutorial(tag, activity);
        Log.i(getClass().getSimpleName(), "readTutorial");

        parser.require(XmlPullParser.START_TAG, ns, "tutorial");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("head")) {
                tutorial.setBpm(readHeadBpm(parser));
            } else if (name.equals("body")) {
                tutorial.addSyncs(readBody(parser));
            } else {
                skip(parser);
            }
        }

        Log.i(this.getClass().getSimpleName(), "tutorial.size = " + tutorial.size());

        return tutorial;
    }

    private int readHeadBpm(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "head");
        Log.i(getClass().getSimpleName(), "readHeadBpm");
        int bpm = -1;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("bpm")) {
                parser.require(XmlPullParser.START_TAG, ns, "bpm");
                bpm = readInt(parser);
                parser.require(XmlPullParser.END_TAG, ns, "bpm");
            } else {
                skip(parser);
            }
        }
        return bpm;
    }

    private ArrayList<Sync> readBody(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "body");
        Log.i(getClass().getSimpleName(), "readBody");
        ArrayList<Sync> syncList = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("sync")) {
                syncList.add(readSync(parser));
            } else {
                skip(parser);
            }
        }
        return syncList;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Sync readSync(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "sync");
        Log.i(getClass().getSimpleName(), "readSync");
        Sync sync = new Sync();

        // set start from attr
        String start = parser.getAttributeValue(null, "start");
        if (start != null) {
            sync.setStart(Integer.valueOf(start));
        }

        // get items
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            // name is the tag string value
            String name = parser.getName();
            if (name.equals("item")) {
                sync.add(readItem(parser));
            } else {
                skip(parser);
                return null;
            }
        }

        return sync;
    }

    private Sync.Item readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        Sync.Item item = null;
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String tag = parser.getName();

        // get values from attr
        if (tag.equals("item")) {
            String deck = parser.getAttributeValue(null, "deck");
            if (deck != null) {
                item = new Sync.Item(Integer.valueOf(deck));
                String pad = parser.getAttributeValue(null, "pad");
                if (pad != null) {
                    item.setPad(Integer.valueOf(pad));
                    String gesture = parser.getAttributeValue(null, "gesture");
                    if (gesture != null) {
                        item.setGesture(Integer.valueOf(gesture));
                    }
                }
                Log.i(getClass().getSimpleName(), "readItem = " + item.toString());
            }
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, "item");
        return item;
    }

    // For the tags title and summary, extracts their text values.
    private int readInt(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return Integer.valueOf(result);
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.i(getClass().getSimpleName(), "skip");
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
