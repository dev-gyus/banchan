package com.devgyu.banchan.account;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression =
        "#this == 'anonymousUser' ? null : #this.customer != null ? customer : #this.storeOwner != null ? storeOwner : " +
                "#this.rider != null ? rider : #this.counselor != null ? counselor : #this.admin != null ? admin : null")
public @interface CurrentUser {
}
