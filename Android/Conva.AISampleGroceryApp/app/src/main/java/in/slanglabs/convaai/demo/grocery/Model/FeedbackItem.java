package in.slanglabs.convaai.demo.grocery.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FeedbackItem implements Serializable {
    public transient Map<String,String> journeyDetails = new HashMap<>();
    public transient Boolean isPositiveFeedback;
    public String feedbackComments = "";
    public String utterance = "";
}
