package br.com.minhasecretariavirtual.core.config;

import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantFilterAspect {
    @AfterReturning(pointcut = "execution(* jakarta.persistence.EntityManagerFactory.createEntityManager(..))", returning = "entityManager")
    public void enableTenantFilter(Object entityManager) {
        if (entityManager instanceof EntityManager em) {
            Session session = em.unwrap(Session.class);
            if (TenantContext.getTenant() != null) {
                session.enableFilter("tenantFilter")
                        .setParameter("tenantId", TenantContext.getTenant());
            }
        }
    }
}
