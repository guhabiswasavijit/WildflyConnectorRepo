package org.seven.cloud.connectors.kafka.inbound;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkListener;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;

@Connector( 
        displayName = "Apache Kafka Resource Adapter",
        vendorName = "Seven Heaven",
        version = "1.0"
)
public class KafkaResourceAdapter implements ResourceAdapter, Serializable, WorkListener {
    
    private static final Logger LOGGER = Logger.getLogger(KafkaResourceAdapter.class.getName());
    private final Map<EndpointKey,KafkaWorker> registeredWorkers;
    private BootstrapContext context;
    private WorkManager workManager;
    private boolean running;

    public KafkaResourceAdapter() {
        registeredWorkers = new ConcurrentHashMap<>();
    }

    @Override
    public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
        LOGGER.info("Kafka Resource Adapter Started..");
        context = ctx;
        workManager = context.getWorkManager();
        running = true;
    }

    @Override
    public void stop() {
        LOGGER.info("Kafka Resource Adapter Stopped");
        for (KafkaWorker work : registeredWorkers.values()) {
            work.stop();
        }
        running = false;
    }

    @Override
    public void endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) throws ResourceException {
        if (spec instanceof KafkaActivationSpec) {
            EndpointKey endpointKey = new EndpointKey(endpointFactory, (KafkaActivationSpec) spec);
            if (((KafkaActivationSpec) spec).getUseSynchMode()) {
                KafkaSynchWorker kafkaWork = new KafkaSynchWorker(endpointKey);
                registeredWorkers.put(endpointKey,kafkaWork);
                workManager.scheduleWork(kafkaWork);
            } else {
                KafkaAsynchWorker kafkaWork = new KafkaAsynchWorker(endpointKey, workManager);
                registeredWorkers.put(endpointKey, kafkaWork);
                workManager.scheduleWork(kafkaWork, ((KafkaActivationSpec) spec).getPollInterval(), null, this);
            }
        } else {
            LOGGER.warning("Got endpoint activation for an ActivationSpec of unknown class " + spec.getClass().getName());
        } 
    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) {
        KafkaWorker work = registeredWorkers.remove(new EndpointKey(endpointFactory, (KafkaActivationSpec) spec));
        if (work != null) {
            work.stop();
        }
    }

    @Override
    public XAResource[] getXAResources(ActivationSpec[] specs) throws ResourceException {
        return null;
    }

    @Override
    public boolean equals(Object o) {
       return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void workAccepted(WorkEvent we) {
   }

    @Override
    public void workRejected(WorkEvent we) {
    }

    @Override
    public void workStarted(WorkEvent we) {
    }

    @Override
    public void workCompleted(WorkEvent we) {
        // when an asynch worker is completed it needs to be rescheduled if it is still active
        try {
            KafkaWorker worker = (KafkaWorker) we.getWork();
            if (running && !worker.isStopped()) {
                workManager.scheduleWork(worker, 1000, null, this);
            }
        } catch (WorkException ex) {
            Logger.getLogger(KafkaResourceAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
