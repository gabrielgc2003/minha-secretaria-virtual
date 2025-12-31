CREATE TABLE conversation
(
    id         UUID         NOT NULL,
    tenant_id  UUID         NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    state      VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_conversation PRIMARY KEY (id)
);

CREATE TABLE conversation_data
(
    id               UUID NOT NULL,
    conversation_id  UUID NOT NULL,
    selected_service VARCHAR(255),
    desired_date     date,
    desired_period   VARCHAR(255),
    extra_data       JSONB,
    CONSTRAINT pk_conversation_data PRIMARY KEY (id)
);

CREATE TABLE service_catalog_item
(
    id          UUID         NOT NULL,
    tenant_id   UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    active      BOOLEAN      NOT NULL,
    CONSTRAINT pk_service_catalog_item PRIMARY KEY (id)
);

CREATE TABLE tenant
(
    id             UUID         NOT NULL,
    name           VARCHAR(255) NOT NULL,
    business_type  VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    agenda_enabled BOOLEAN      NOT NULL,
    CONSTRAINT pk_tenant PRIMARY KEY (id)
);

ALTER TABLE conversation_data
    ADD CONSTRAINT uc_conversation_data_conversation UNIQUE (conversation_id);

ALTER TABLE conversation_data
    ADD CONSTRAINT FK_CONVERSATION_DATA_ON_CONVERSATION FOREIGN KEY (conversation_id) REFERENCES conversation (id);