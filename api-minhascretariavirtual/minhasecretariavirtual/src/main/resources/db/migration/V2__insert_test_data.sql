-- 1. Inserir um TENANT de teste (Padaria do João)
-- ID fixo para facilitar seus testes na URL
INSERT INTO tenants (
    id,
    name,
    instance_name,
    api_key_evolution,
    system_prompt,
    instruction,
    active
) VALUES (
             '11111111-1111-1111-1111-111111111111',
             'Padaria do João',
             'padaria-joao',
             'apikey-secreta-123',
             'Você é uma assistente virtual da Padaria do João. Seja educada e foque em vender pães e doces. Nossos horários: Seg-Sex das 07h às 20h.',
             'Para o dia 05/06/2024, temos uma promoção especial: compre 5 pães de queijo e ganhe 1 grátis!;Para entregas, o valor mínimo é R$30,00.; No dia 06/06/2024, estaremos fechados para manutenção.',
            true
         );

-- 2. Inserir um CONTATO de teste (Cliente Maria) vinculado ao Tenant acima
INSERT INTO contacts (
    id,
    tenant_id,
    created_at,
    updated_at,
    remote_jid,
    push_name,
    conversation_state
) VALUES (
             '22222222-2222-2222-2222-222222222222', -- ID do Contato
             '11111111-1111-1111-1111-111111111111', -- ID do Tenant (Link)
             NOW(),
             NOW(),
             '5541999998888@s.whatsapp.net',
             'Maria Cliente',
             'INICIAL'
         );

-- 3. Inserir Histórico de Mensagens (Para testar se a IA recebe o contexto)

-- Mensagem 1: Cliente perguntando
INSERT INTO messages (
    id, tenant_id, contact_id, created_at, updated_at,
    content, role, type
) VALUES (
             gen_random_uuid(),
             '11111111-1111-1111-1111-111111111111',
             '22222222-2222-2222-2222-222222222222',
             NOW() - INTERVAL '5 minutes',
             NOW() - INTERVAL '5 minutes',
             'Olá, tem pão de queijo quentinho?',
             'USER',
             'text'
         );

-- Mensagem 2: Bot respondendo (Simulação)
INSERT INTO messages (
    id, tenant_id, contact_id, created_at, updated_at,
    content, role, type
) VALUES (
             gen_random_uuid(),
             '11111111-1111-1111-1111-111111111111',
             '22222222-2222-2222-2222-222222222222',
             NOW() - INTERVAL '4 minutes',
             NOW() - INTERVAL '4 minutes',
             'Olá Maria! Temos sim, acabou de sair uma fornada agora às 16h.',
             'ASSISTANT',
             'text'
         );