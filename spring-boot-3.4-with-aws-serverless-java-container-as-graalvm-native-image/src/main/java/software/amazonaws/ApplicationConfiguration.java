package software.amazonaws;

import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_METHODS;
import static org.springframework.aot.hint.MemberCategory.PUBLIC_FIELDS;

import java.util.HashSet;

import org.joda.time.DateTime;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import software.amazonaws.example.product.entity.Product;
import software.amazonaws.example.product.entity.Products;

@Configuration
@RegisterReflectionForBinding({DateTime.class, APIGatewayProxyRequestEvent.class, HashSet.class, 
	APIGatewayProxyRequestEvent.ProxyRequestContext.class, APIGatewayProxyRequestEvent.RequestIdentity.class,
	Product.class, Products.class})

@ImportRuntimeHints(ApplicationConfiguration.ApplicationRuntimeHintsRegistrar.class)

public class ApplicationConfiguration {
	
	public static class ApplicationRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection()
                   .registerType(
                            Product.class,
                            PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS
                    ).registerType(
                            Products.class,
                            PUBLIC_FIELDS, INVOKE_PUBLIC_METHODS, INVOKE_PUBLIC_CONSTRUCTORS
                    );
        }
    }

}
