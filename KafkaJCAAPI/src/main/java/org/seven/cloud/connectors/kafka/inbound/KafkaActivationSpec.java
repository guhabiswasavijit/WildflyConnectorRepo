package org.seven.cloud.connectors.kafka.inbound;

import org.seven.cloud.connectors.kafka.api.KafkaListener;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import javax.resource.ResourceException;
import javax.resource.spi.Activation;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.ResourceAdapter;
import java.util.Properties;

@Activation(messageListeners = KafkaListener.class)
public class KafkaActivationSpec implements ActivationSpec {

    private final Properties consumerProperties;
    private ResourceAdapter ra;
    private Long autoCommitInterval;
    private String bootstrapServersConfig;
    private String clientId;
    private Boolean enableAutoCommit = true;
    private String groupIdConfig;
    private String valueDeserializer;
    private String keyDeserializer;
    private String topics;
    private Long pollInterval = 1000L;
    private Long initialPollDelay = 1000L;
    private Long fetchMinBytes;
    private Long fetchMaxBytes;
    private Integer heartbeatInterval;
    private Integer maxPartitionFetchBytes;
    private Integer sessionTimeout;
    private String autoOffsetReset;
    private Long connectionsMaxIdle;
    private Integer receiveBuffer;
    private Integer requestTimeout;
    private Boolean checkCRCs;
    private Integer fetchMaxWait;
    private Long metadataMaxAge;
    private Long reconnectBackoff;
    private Long retryBackoff;
    private Boolean commitEachPoll = false;
    private Boolean useSynchMode = false;

    public KafkaActivationSpec() {
        consumerProperties = new Properties();
    }

    @Override
    public void validate() throws InvalidPropertyException {
        if (bootstrapServersConfig == null) {
            throw new InvalidPropertyException("bootstrapServersConfig is a mandatory property");
        }

        if (keyDeserializer == null) {
            throw new InvalidPropertyException("keyDeserializer is a mandatory property");
        }

        if (valueDeserializer == null) {
            throw new InvalidPropertyException("valueDeserializer is a mandatory property");
        }
    }

    @Override
    public ResourceAdapter getResourceAdapter() {
        return ra;
    }

    @Override
    public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
        this.ra = ra;
    }

    public Boolean getCommitEachPoll() {
        return commitEachPoll;
    }

    public void setCommitEachPoll(Boolean commitEachPoll) {
        this.commitEachPoll = commitEachPoll;
    }

    public Long getAutoCommitInterval() {
        return autoCommitInterval;
    }

    public void setAutoCommitInterval(Long autoCommitInterval) {
        this.autoCommitInterval = autoCommitInterval;
        consumerProperties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, Long.toString(autoCommitInterval));
    }

    public Boolean getUseSynchMode() {
        return useSynchMode;
    }

    public void setUseSynchMode(Boolean useSynchMode) {
        this.useSynchMode = useSynchMode;
    }
    
    

    public String getBootstrapServersConfig() {
        return bootstrapServersConfig;
    }

    public void setBootstrapServersConfig(String bootstrapServersConfig) {
        this.bootstrapServersConfig = bootstrapServersConfig;
        consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
        consumerProperties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
    }

    public Boolean getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(Boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
        consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.toString(enableAutoCommit));
    }

    public String getGroupIdConfig() {
        return groupIdConfig;
    }

    public void setGroupIdConfig(String groupIdConfig) {
        this.groupIdConfig = groupIdConfig;
        consumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig);
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
        consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
        consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
    }

    public Long getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(Long pollInterval) {
        this.pollInterval = pollInterval;
    }

    public Properties getConsumerProperties() {
        return consumerProperties;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public Long getInitialPollDelay() {
        return initialPollDelay;
    }

    public void setInitialPollDelay(Long initialPollDelay) {
        this.initialPollDelay = initialPollDelay;
    }

    public ResourceAdapter getRa() {
        return ra;
    }

    public void setRa(ResourceAdapter ra) {
        this.ra = ra;
    }

    public Long getFetchMinBytes() {
        return fetchMinBytes;
    }

    public void setFetchMinBytes(Long fetchMinBytes) {
        this.fetchMinBytes = fetchMinBytes;
        consumerProperties.setProperty(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, Long.toString(fetchMinBytes));
    }

    public Long getFetchMaxBytes() {
        return fetchMaxBytes;
    }

    public void setFetchMaxBytes(Long fetchMaxBytes) {
        this.fetchMaxBytes = fetchMaxBytes;
        consumerProperties.setProperty(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, Long.toString(fetchMaxBytes));
    }

    public Integer getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(Integer heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
        consumerProperties.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, Integer.toString(heartbeatInterval));
    }

    public Integer getMaxPartitionFetchBytes() {
        return maxPartitionFetchBytes;
    }

    public void setMaxPartitionFetchBytes(Integer maxPartitionFetchBytes) {
        this.maxPartitionFetchBytes = maxPartitionFetchBytes;
        consumerProperties.setProperty(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, Integer.toString(maxPartitionFetchBytes));
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        consumerProperties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, Integer.toString(sessionTimeout));
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
    }

    public Long getConnectionsMaxIdle() {
        return connectionsMaxIdle;
    }

    public void setConnectionsMaxIdle(Long connectionsMaxIdle) {
        this.connectionsMaxIdle = connectionsMaxIdle;
        consumerProperties.setProperty(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, Long.toString(connectionsMaxIdle));
    }

    public Integer getReceiveBuffer() {
        return receiveBuffer;
    }

    public void setReceiveBuffer(Integer receiveBuffer) {
        this.receiveBuffer = receiveBuffer;
        consumerProperties.setProperty(ConsumerConfig.RECEIVE_BUFFER_CONFIG, Integer.toString(receiveBuffer));
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
        consumerProperties.setProperty(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, Integer.toString(requestTimeout));
    }

    public Boolean getCheckCRCs() {
        return checkCRCs;
    }

    public void setCheckCRCs(Boolean checkCRCs) {
        this.checkCRCs = checkCRCs;
        consumerProperties.setProperty(ConsumerConfig.CHECK_CRCS_CONFIG, Boolean.toString(checkCRCs));
    }

    public Integer getFetchMaxWait() {
        return fetchMaxWait;
    }

    public void setFetchMaxWait(Integer fetchMaxWait) {
        this.fetchMaxWait = fetchMaxWait;
        consumerProperties.setProperty(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, Integer.toString(fetchMaxWait));
    }

    public Long getMetadataMaxAge() {
        return metadataMaxAge;
    }

    public void setMetadataMaxAge(Long metadataMaxAge) {
        this.metadataMaxAge = metadataMaxAge;
        consumerProperties.setProperty(ConsumerConfig.METADATA_MAX_AGE_CONFIG, Long.toString(metadataMaxAge));
    }

    public Long getReconnectBackoff() {
        return reconnectBackoff;
    }

    public void setReconnectBackoff(Long reconnectBackoff) {
        this.reconnectBackoff = reconnectBackoff;
        consumerProperties.setProperty(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, Long.toString(reconnectBackoff));
    }

    public Long getRetryBackoff() {
        return retryBackoff;
    }

    public void setRetryBackoff(Long retryBackoff) {
        this.retryBackoff = retryBackoff;
        consumerProperties.setProperty(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, Long.toString(retryBackoff));
    }
}
