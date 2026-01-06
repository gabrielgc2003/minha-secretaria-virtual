-- Habilita a extensão para UUIDs
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. Tabela TENANTS (Deve ser a primeira)
CREATE TABLE tenants (
                         id UUID NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         instance_name VARCHAR(255) NOT NULL, -- Identificador único na Evolution API
                         api_key_evolution VARCHAR(255),
                         system_prompt TEXT,                  -- Prompt personalizado da IA para este cliente
                         instruction TEXT,                  -- Instrução personalizado para a IA
                         active BOOLEAN DEFAULT TRUE,

                         CONSTRAINT pk_tenants PRIMARY KEY (id),
                         CONSTRAINT uc_tenants_instance_name UNIQUE (instance_name)
);


-- 2. Tabela CONTACTS
CREATE TABLE contacts (
                          id UUID NOT NULL,
                          tenant_id UUID NOT NULL,
                          created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

                          remote_jid VARCHAR(255) NOT NULL,
                          push_name VARCHAR(255),
                          conversation_state VARCHAR(50),

                          CONSTRAINT pk_contacts PRIMARY KEY (id),
    -- Garante que o tenant_id exista na tabela tenants
                          CONSTRAINT fk_contacts_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- Índices
CREATE INDEX idx_contact_remote_jid ON contacts(remote_jid);
CREATE INDEX idx_contact_tenant ON contacts(tenant_id);


-- 3. Tabela MESSAGES
CREATE TABLE messages (
                          id UUID NOT NULL,
                          tenant_id UUID NOT NULL,
                          created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

                          content TEXT NOT NULL,
                          role VARCHAR(20) NOT NULL,
                          type VARCHAR(20) DEFAULT 'text',

                          contact_id UUID NOT NULL,

                          CONSTRAINT pk_messages PRIMARY KEY (id),
    -- Relacionamentos
                          CONSTRAINT fk_messages_contact FOREIGN KEY (contact_id) REFERENCES contacts(id),
                          CONSTRAINT fk_messages_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- Índices
CREATE INDEX idx_message_contact ON messages(contact_id);
CREATE INDEX idx_message_tenant ON messages(tenant_id);
CREATE INDEX idx_message_created_at ON messages(created_at);