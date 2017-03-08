package processors.venues;

import processors.Maker;
import processors.Processor;

/**
 * Created by Greg on 10/25/16.
 */
public class VenueMaker implements Maker {
    @Override
    public Processor createMessageProcessor() {
        return new VenueProcessor();
    }
}
