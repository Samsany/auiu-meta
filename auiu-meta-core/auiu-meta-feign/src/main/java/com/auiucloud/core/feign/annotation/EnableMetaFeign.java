package com.auiucloud.core.feign.annotation;

import com.auiucloud.core.feign.config.FeignInterceptorConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.MetaFeignClientsRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author dries
 * @createDate 2022-06-10 16-38
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
@Import({FeignInterceptorConfiguration.class, MetaFeignClientsRegistrar.class})
public @interface EnableMetaFeign {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
     * declarations e.g.: {@code @ComponentScan("com.auiucloud")} instead of
     * {@code @ComponentScan(basePackages="com.auiucloud")}.
     *
     * @return the array of 'basePackages'.
     */
    @AliasFor(annotation = EnableFeignClients.class, value = "value")
    String[] value() default {};

    /**
     * Base packages to scan for annotated components.
     * <p>
     * {@link #value()} is an alias for (and mutually exclusive with) this attribute.
     * <p>
     * Use {@link #basePackageClasses()} for a type-safe alternative to String-based
     * package names.
     *
     * @return the array of 'basePackages'.
     */
    @AliasFor(annotation = EnableFeignClients.class)
    String[] basePackages() default {"com.auiucloud.*"};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to
     * scan for annotated components. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that
     * serves no purpose other than being referenced by this attribute.
     *
     * @return the array of 'basePackageClasses'.
     */
    @AliasFor(annotation = EnableFeignClients.class)
    Class<?>[] basePackageClasses() default {};

    /**
     * A custom <code>@Configuration</code> for all feign clients. Can contain override
     * <code>@Bean</code> definition for the pieces that make up the client, for instance
     * {@link feign.codec.Decoder}, {@link feign.codec.Encoder}, {@link feign.Contract}.
     *
     * @see FeignClientsConfiguration for the defaults
     */
    @AliasFor(annotation = EnableFeignClients.class)
    Class<?>[] defaultConfiguration() default {};

    /**
     * List of classes annotated with @FeignClient. If not empty, disables classpath scanning.
     *
     * @return
     */
    @AliasFor(annotation = EnableFeignClients.class)
    Class<?>[] clients() default {};

}
