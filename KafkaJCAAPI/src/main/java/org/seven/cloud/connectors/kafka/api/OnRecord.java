package org.seven.cloud.connectors.kafka.api;

import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface OnRecord {
    @Nonbinding String[] topics() default {};    
    @Nonbinding boolean matchOtherMethods() default false;
}
