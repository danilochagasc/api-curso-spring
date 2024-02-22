ALTER TABLE person
    ADD COLUMN enabled BOOLEAN DEFAULT TRUE;

-- Agora, se você quiser definir a posição da nova coluna depois de "gender"
ALTER TABLE person
    ALTER COLUMN enabled SET DEFAULT TRUE;

-- Para garantir que a coluna não aceite valores nulos
ALTER TABLE person
    ALTER COLUMN enabled SET NOT NULL;
