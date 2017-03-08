package processors.messages;

import processors.Maker;
import processors.Processor;

/**
 * Created by Greg
 */
public class MessageMaker implements Maker {

    public Processor createMessageProcessor() {
        return new MessageProcessor();
    }
}
