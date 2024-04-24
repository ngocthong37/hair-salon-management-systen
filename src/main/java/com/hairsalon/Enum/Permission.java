package com.hairsalon.Enum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    CUSTOMER_READ("customer:read"),
    CUSTOMER_UPDATE("customer:update"),
    CUSTOMER_CREATE("customer:create"),
    CUSTOMER_DELETE("customer:delete"),

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    EMPLOYEE_READ("management:read"),
    EMPLOYEE_UPDATE("management:update"),
    EMPLOYEE_CREATE("management:create"),
    EMPLOYEE_DELETE("management:delete")

    ;

    @Getter
    private final String permission;
}