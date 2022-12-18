package org.seven.cloud.connectors.kafka.api;

import javax.resource.ResourceException;
import javax.resource.cci.ConnectionSpec;


public interface KafkaConnectionFactory {
    
    KafkaConnection createConnection() throws ResourceException;

    KafkaConnection createConnection(ConnectionSpec spec) throws ResourceException;
    
}
