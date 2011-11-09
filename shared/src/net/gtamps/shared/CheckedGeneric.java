package net.gtamps.shared;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckedGeneric {
	int typeIndex() default 0;
}
