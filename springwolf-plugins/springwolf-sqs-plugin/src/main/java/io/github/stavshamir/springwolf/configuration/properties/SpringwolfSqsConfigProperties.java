// SPDX-License-Identifier: Apache-2.0
package io.github.stavshamir.springwolf.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

/**
 * This class is used to create metadata for auto-completion in spring configuration properties/yaml by using
 * the spring-boot-configuration-processor.
 */
@ConfigurationProperties(prefix = SpringwolfSqsConfigConstants.SPRINGWOLF_SQS_CONFIG_PREFIX)
@Getter
@Setter
public class SpringwolfSqsConfigProperties {

    @Nullable
    private Publishing publishing;

    @Nullable
    private Scanner scanner;

    @Getter
    @Setter
    public static class Publishing {

        /**
         * Enables/Disables the possibility to publish messages through springwolf on the configured sqs instance.
         */
        private boolean enabled = false;
    }

    @Getter
    @Setter
    public static class Scanner {

        private static SqsListener sqsListener;

        @Getter
        @Setter
        public static class SqsListener {

            /**
             * This mirrors the ConfigConstant {@see SpringwolfSqsConfigConstants#SPRINGWOLF_SCANNER_SQS_LISTENER_ENABLED}
             */
            private boolean enabled = true;
        }
    }
}
