package org.seven.cloud.connectors.kafka.inbound;

import java.util.Objects;
import javax.resource.spi.endpoint.MessageEndpointFactory;

public class EndpointKey {
    
    private MessageEndpointFactory mef;
    private KafkaActivationSpec spec;

    public EndpointKey(MessageEndpointFactory mef, KafkaActivationSpec spec) {
        this.mef = mef;
        this.spec = spec;
    }
    
    public MessageEndpointFactory getMef() {
        return mef;
    }

    public void setMef(MessageEndpointFactory mef) {
        this.mef = mef;
    }

    public KafkaActivationSpec getSpec() {
        return spec;
    }

    public void setSpec(KafkaActivationSpec spec) {
        this.spec = spec;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.mef);
        hash = 71 * hash + Objects.hashCode(this.spec);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EndpointKey other = (EndpointKey) obj;
        if (!Objects.equals(this.mef, other.mef)) {
            return false;
        }
        if (!Objects.equals(this.spec, other.spec)) {
            return false;
        }
        return true;
    }
    
    
    
}
