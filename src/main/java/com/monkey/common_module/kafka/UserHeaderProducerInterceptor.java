package com.monkey.common_module.kafka;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UserHeaderProducerInterceptor implements ProducerInterceptor<String, Object> {
    private static final String USER_ID_KEY = "X-User-Id";
    private static final String USER_NAME_KEY = "X-User-Name";
    private static final String USER_ROLE_KEY = "X-User-Role";

    private static Header toHeader(String key, String value){
        return new RecordHeader(key, value.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> producerRecord) {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return producerRecord;
        }
        HttpServletRequest req = attrs.getRequest();

        String userId = req.getHeader(USER_ID_KEY);
        String userName = req.getHeader(USER_NAME_KEY);
        String userRole = req.getHeader(USER_ROLE_KEY);

        if (userId != null) producerRecord.headers().add(toHeader(USER_ID_KEY, userId));
        if (userName != null) producerRecord.headers().add(toHeader(USER_NAME_KEY, userName));
        if (userRole != null) producerRecord.headers().add(toHeader(USER_ROLE_KEY, userRole));

        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}