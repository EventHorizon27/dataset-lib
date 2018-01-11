package tech.pod.dataset.ims;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
/* SearcAgent acts as the  agent of a search, managing the classifiers with arraylists for From DateClassifiers,To DateClassifiers,Regex StringClassifiers,and WordHistogramClassifiers*/
public class SearchAgent {
    List < List < String >> searchGroup;
    List < IndexKey > searchOutput;
    SearchAgent(List < List < String >> searchGroup, List < IndexKey > searchOutput) {
        this.searchGroup = searchGroup;
        this.searchOutput = searchOutput;
    }
    public List < IndexKey > search() {
            DateFormat format = new SimpleDateFormat("   yyyy.MM.dd  HH:mm:ss z", Locale.ENGLISH);
            if (searchGroup.get(0).get(0).indexOf("any") == -1) {//from:date
                List < DateClassifier > dateClassifiers = new ArrayList < DateClassifier > ();
                for (int i = 0; i < searchGroup.get(0).length; i++) {
                    dateClassifiers.add(new DateClassifier(format.parse(searchGroup.get(0).get(i).split("-")[1]), "after", searchGroup.get(0).get(i).split("-")[0], searchOutput)); //need to format first parameter
                    searchOutput = dateClassifiers.get(i).classify();
                }
            }
            if (searchGroup.get(1).get(0).indexOf("any") == -1) {// to:date
                List < DateClassifier > dateClassifiers = new ArrayList < DateClassifier > ();
                for (int i = 0; i < searchGroup.get(1).length; i++) {
                    dateClassifiers.add(new DateClassifier(format.parse(searchGroup.get(1).get(i).split("-")[1]), "after", searchGroup.get(1).get(i).split("-")[0], searchOutput)); //need to format first parameter
                    searchOutput = dateClassifiers.get(i).classify();
                }
            }
            if (searchGroup.get(2).get(0).indexOf("any") == -1) {//regexes
                List < StringClassifier > stringClassifiers = new ArrayList < StringClassifier > ();
                for (int i = 0; i < searchGroup.get(1).length; i++) {
                    stringClassifiers.add(new StringClassifier(searchGroup.get(1).get(i).split("-")[1], searchGroup.get(1).get(i).split("-")[0], searchOutput));
                    searchOutput = stringClassifiers.get(i).classify();
                }
            }

            if (searchGroup.get(3).get(0).indexOf("any") == -1) {//word histogram classifier
                List < String > wordKeys = new ArrayList < String > ();
                HashMap < String, Integer > wordClassifiers = new HashMap < String, Integer > ();
                for (int i = 0; i < searchOutput.get(3).length; i++) {
                    String s = (String) searchOutput.get(4).get(i);
                    wordKeys.add(s.split("-")[0]);
                    wordClassifiers.put(s.split("-")[0], (Integer) s.split("-")[0]);
                }
                WordHistogramClassifier classifier = new WordHistogramClassifier(wordKeys, wordClassifiers, searchOutput);
                searchOutput = classifier.classify();
            }
       
        
       return searchOutput;
    }
}