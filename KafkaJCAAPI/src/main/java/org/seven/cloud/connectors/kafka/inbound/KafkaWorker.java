package org.seven.cloud.connectors.kafka.inbound;

import javax.resource.spi.work.Work;

public interface KafkaWorker extends Work {
    
    public void stop();
    public boolean isStopped();
    
}
