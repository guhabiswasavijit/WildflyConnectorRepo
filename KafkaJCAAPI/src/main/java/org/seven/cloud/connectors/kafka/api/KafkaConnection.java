package org.seven.cloud.connectors.kafka.api;

import java.util.List;
import java.util.concurrent.Future;
import javax.resource.ResourceException;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;


public interface KafkaConnection extends AutoCloseable {
    
    public Future<RecordMetadata> send(ProducerRecord record) throws ResourceException;
    
    public Future<RecordMetadata> send(ProducerRecord record, Callback callback) throws ResourceException;
    
    public void flush() throws ResourceException;
    
    List<PartitionInfo> partitionsFor(String topic) throws ResourceException;
    
    public java.util.Map<MetricName,? extends Metric> metrics​() throws ResourceException;
    
}
