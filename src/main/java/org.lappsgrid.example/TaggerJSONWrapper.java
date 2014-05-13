package org.lappsgrid.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lappsgrid.core.DataFactory;

import java.util.ArrayList;

/**
 * Created by shi on 5/13/14.
 */
public class TaggerJSONWrapper {
    JSONObject json = null;
    JSONObject currentStep = null;
    JSONObject currentStepMeta = null;
    JSONObject contains = null;
    JSONObject contain = null;
    JSONArray annotations = null;
    JSONArray steps = null;
    JSONObject text = null;
    JSONObject metadata = null;
    static int id = 0;

    JSONObject lastStep = null;
    JSONObject lastStepMeta = null;
    JSONArray lastStepAnnotations = null;
    JSONObject lastStepContains = null;

    public TaggerJSONWrapper(String textjson) {
        id = 0;
        json = new JSONObject(textjson);
        text = json.getJSONObject("text");
        String textvalue = text.getString("@value");
        steps =  json.getJSONArray("steps");
        metadata = json.getJSONObject("metadata");

        currentStep = new JSONObject();
        currentStepMeta = new JSONObject();
        contains = new JSONObject();
        contain = new JSONObject();
        annotations = new JSONArray();


        if(steps.length() > 0) {
            lastStep = (JSONObject)steps.get(steps.length() - 1);
            lastStepMeta = lastStep.getJSONObject("metadata");
            lastStepAnnotations = lastStep.getJSONArray("annotations");
            lastStepContains = lastStepMeta.getJSONObject("contains");
            if (lastStepContains.has("Token")) {
                // contains sentence
                for(int j = 0; j < lastStepAnnotations.length(); j++) {
                    JSONObject lastStepAnnotation = lastStepAnnotations.getJSONObject(j);
                }
            }
        }
        contain.put("producer", "");
        contain.put("type", "");
        contains.put("Tagger", contain);
        currentStepMeta.put("contains", contains);
        currentStep.put("metadata", currentStepMeta);
        currentStep.put("annotations", annotations);
        json.put("metadata", metadata);
        json.put("text", text);
        json.put("steps", steps.put(currentStep));

    }

    public TaggerJSONWrapper() {
        id = 0;
        json = new JSONObject();
        currentStep = new JSONObject();
        currentStepMeta = new JSONObject();
        contains = new JSONObject();
        contain = new JSONObject();
        annotations = new JSONArray();
        steps = new JSONArray();
        text = new JSONObject();
        metadata = new JSONObject();

        contain.put("producer", "");
        contain.put("type", "");
        text.put("@value", "");
        contains.put("Tagger", contain);
        currentStepMeta.put("contains", contains);
        currentStep.put("metadata", currentStepMeta);
        currentStep.put("annotations", annotations);
        json.put("metadata", metadata);
        json.put("text", text);
        json.put("steps", steps.put(currentStep));
    }

    public void setType(String type) {
        contain.put("type", type);
    }

    public void setProducer(String prod) {
        contain.put("producer", prod);
    }

    public void setText(String txt) {
        text.put("@value", txt);
    }

    public JSONObject newAnnotation(){
        JSONObject annotation = new JSONObject();
        annotation.put("@type", "Token");
        annotation.put("id", "pos" + id ++);
        annotations.put(annotation);
        return annotation;
    }

    public void setWord(JSONObject annotation, String word) {
        Object features = annotation.opt("features");
        if (features == null) {
            features = newFeatures(annotation);
        }
        ((JSONObject)features).put("word", word);
    }

    public void setTag(JSONObject annotation, String tag) {
        Object features = annotation.opt("features");
        if (features == null) {
            features = newFeatures(annotation);
        }
        ((JSONObject)features).put("category", tag);
    }

    public JSONObject newFeatures(JSONObject annotation) {
        JSONObject features = new JSONObject();
        annotation.put("features", features);
        return features;
    }

    public String toString(){
        return json.toString();
    }
}
