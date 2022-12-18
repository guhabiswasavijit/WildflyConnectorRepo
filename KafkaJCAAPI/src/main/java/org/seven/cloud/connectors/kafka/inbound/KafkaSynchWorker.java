package org.seven.cloud.connectors.kafka.inbound;

import org.seven.cloud.connectors.kafka.api.OnRecord;
import org.seven.cloud.connectors.kafka.api.OnRecords;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.endpoint.MessageEndpoint;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


public class KafkaSynchWorker implements KafkaWorker {

    private static final Logger LOGGER = Logger.getLogger(KafkaSynchWorker.class.getName());
    private final EndpointKey key;
    @SuppressWarnings("rawtypes")
	private KafkaConsumer consumer;
    private AtomicBoolean ok = new AtomicBoolean(true);

    private final List<Method> onRecordMethods = new ArrayList<>();
    private final List<Method> onRecordsMethods = new ArrayList<>();

    
    public KafkaSynchWorker(EndpointKey key) {
    	LOGGER.log(Level.INFO,"new work for the specified key");
        this.key = key;
        LOGGER.log(Level.INFO,"work out what methods we have");
        Class<?> mdbClass = key.getMef().getEndpointClass();
        for (Method m : mdbClass.getMethods()) {
            if (m.isAnnotationPresent(OnRecord.class)) {
                if (m.getParameterCount() == 1 && ConsumerRecord.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    onRecordMethods.add(m);
                } else {
                    LOGGER.log(Level.WARNING, "@{0} annotated MDBs must have only one parameter of type {1}. {2}#{3} endpoint will be ignored.", new Object[]{
                        OnRecord.class.getSimpleName(), ConsumerRecord.class.getSimpleName(), mdbClass.getName(), m.getName()});
                }
            }

            if (m.isAnnotationPresent(OnRecords.class)) {
                if (m.getParameterCount() == 1 && ConsumerRecords.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    onRecordsMethods.add(m);
                } else {
                    LOGGER.log(Level.WARNING, "@{0} annotated MDBs must have only one parameter of type {1}. {2}#{3} endpoint will be ignored.", new Object[]{
                        OnRecords.class.getSimpleName(), ConsumerRecords.class.getSimpleName(), mdbClass.getName(), m.getName()});
                }
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void run() {
        try {
            Thread.sleep(key.getSpec().getInitialPollDelay());
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Interrupt Exception in wait for start");
        }
        try {
        	LOGGER.log(Level.INFO,"create the consumer");
            consumer = new KafkaConsumer(key.getSpec().getConsumerProperties());
            consumer.subscribe(Arrays.asList(key.getSpec().getTopics().split(",")));
            MessageEndpoint endpoint  = key.getMef().createEndpoint(null);
            while (ok.get()) {
                ConsumerRecords<Object, Object> records = consumer.poll(Duration.of(key.getSpec().getPollInterval(), ChronoUnit.MILLIS));
                if (records.isEmpty()) {
                    continue;
                }                
                for (Method m : onRecordsMethods) {
                	LOGGER.log(Level.INFO,"method receives a ConsumerRecord");
                    OnRecords recordsAnnt = m.getAnnotation(OnRecords.class);
                    try {
                        deliverRecords(endpoint, m, records);
                    } catch (UnavailableException ex) {
                        Logger.getLogger(KafkaSynchWorker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (!recordsAnnt.matchOtherMethods()) {
                        return;
                    }
                }
                LOGGER.log(Level.INFO,"match methods with OnRecord annotation");
                for (ConsumerRecord<Object, Object> record : records) {
                    for (Method m : onRecordMethods) {
                    	LOGGER.log(Level.INFO,"method receives a ConsumerRecord");
                        OnRecord recordAnnt = m.getAnnotation(OnRecord.class);
                        String topics[] = recordAnnt.topics();
                        if ((topics.length == 0) || Arrays.binarySearch(recordAnnt.topics(), record.topic()) >= 0) {
                            try {
                                deliverRecord(endpoint, m, record);
                            } catch (UnavailableException ex) {
                                Logger.getLogger(KafkaSynchWorker.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!recordAnnt.matchOtherMethods()) {
                                break;
                            }
                        }
                    }
                }
                if (key.getSpec().getCommitEachPoll()) {
                    consumer.commitSync();
                }
            }
            consumer.close();
            endpoint.release();
        } catch (UnavailableException ex) {
            Logger.getLogger(KafkaSynchWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void deliverRecords (MessageEndpoint endpoint, Method m, ConsumerRecords<Object,Object> records) throws UnavailableException {
        try {
            endpoint.beforeDelivery(m);
            m.invoke(endpoint, records);
            endpoint.afterDelivery();
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ResourceException ex) {
            Logger.getLogger(KafkaResourceAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void deliverRecord (MessageEndpoint endpoint, Method m, ConsumerRecord<Object,Object> record) throws UnavailableException {
        try {
            endpoint.beforeDelivery(m);
            m.invoke(endpoint, record);
            endpoint.afterDelivery();
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ResourceException ex) {
            Logger.getLogger(KafkaResourceAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void stop() {
        ok.set(false);
    }

    @Override
    public boolean isStopped() {
        return !ok.get();
    }
    
    

    @Override
    public void release() {
        stop();
    }

}
