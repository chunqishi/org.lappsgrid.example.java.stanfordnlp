package org.lappsgrid.example;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lappsgrid.api.Data;
import org.lappsgrid.api.WebService;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.DiscriminatorRegistry;
import org.lappsgrid.discriminator.Types;
import org.lappsgrid.vocabulary.Annotations;
import org.lappsgrid.vocabulary.Features;

import java.util.*;

public class StanfordTagger implements WebService {
    public static final  String VERSION = "0.0.1-SNAPSHOT";
    StanfordCoreNLP snlp = null;

    public StanfordTagger(){
        Properties props = new Properties();
        props.put("annotators", "tokenize ssplit pos");
        // Initialize StanfordCoreNLP
        snlp = new StanfordCoreNLP(props);
    }

    @Override
    public long[] requires() {
        return new long[]{};
    }

    @Override
    public long[] produces() {
        return new long[]{};
    }

    @Override
    public Data execute(Data data) {
        {
            long discriminator = data.getDiscriminator();
            if (discriminator == Types.ERROR)
            {
                return data;
            }
            else  if (discriminator == Types.TEXT) {
                String text = data.getPayload();
                TaggerJSONWrapper json = new TaggerJSONWrapper();
                json.setText(text);
                json.setProducer(this.getClass().getName() + ":" + VERSION);
                json.setType("annotation:tagger");

                // Stanford Tagger
                Annotation annotation = new Annotation(text);
                snlp.annotate(annotation);
                // sentences
                List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
                ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();

                for (CoreMap sentence : sentences) {
                    for (CoreLabel label : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                        JSONObject ann = json.newAnnotation();
                        // text
                        String word = label.get(CoreAnnotations.TextAnnotation.class);
                        json.setWord(ann, word);
                        // pos
                        String pos = label.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        json.setTag(ann, pos);
                    }
                }
                return DataFactory.json(json.toString());

            } else {
                String name = DiscriminatorRegistry.get(discriminator);
                String message = "Invalid input type. Expected Text but found " + name;
                return DataFactory.error(message);
            }
        }
    }


    @Override
    public Data configure(Data config) {
        return null;
    }
}