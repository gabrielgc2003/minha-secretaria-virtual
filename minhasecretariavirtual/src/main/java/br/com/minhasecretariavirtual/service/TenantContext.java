package br.com.minhasecretariavirtual.service;

import java.util.UUID;

public class TenantContext {

    private static final ThreadLocal<UUID> TENANT = new ThreadLocal<>();

    public static void setTenant(UUID tenantId) {
        TENANT.set(tenantId);
    }

    public static UUID getTenant() {
        return TENANT.get();
    }

    public static void clear() {
        TENANT.remove();
    }
}