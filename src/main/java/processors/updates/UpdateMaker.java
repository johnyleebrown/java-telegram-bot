package processors.updates;

import processors.Maker;
import processors.Processor;

/**
 * Created by Greg on 10/25/16.
 */
public class UpdateMaker implements Maker {

    @Override
    public Processor createMessageProcessor() {
        return new UpdateProcessor();
    }

}
