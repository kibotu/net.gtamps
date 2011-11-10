package net.gtamps.shared;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CheckedShareable {
	int typeIndex() default 0;
}
