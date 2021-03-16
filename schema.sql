--Client (id, nome, cpf,  telefone)
-- Wallet (id, description, balance, client_id)
--      client_id references Client(id)
-- Expense (id, description, value, state, type, wallet_id)
--      wallet_id references Wallet(id)
-- Expense_state (id, state)
-- Expense_type (id, type)

CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON CLIENT
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON WALLET
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON CLIENT
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

CREATE TABLE CLIENT (
	id INTEGER NOT NULL,
	name VARCHAR(100) NOT NULL,
	cpf VARCHAR(15) NOT NULL,
	telefone VARCHAR(15) NOT NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	UNIQUE(cpf),
	CONSTRAINT pk_client PRIMARY KEY (id)
);

CREATE TABLE WALLET (
	id INTEGER NOT NULL,
	description VARCHAR(140) DEFAULT('no description set'),
	client_id INTEGER NOT NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	CONSTRAINT pk_wallet PRIMARY KEY (id)
	CONSTRAINT fk_wallet_client FOREIGN KEY (client_id) REFERENCES CLIENT (id)
);

CREATE TABLE EXPENSE (
	id INTEGER NOT NULL,
	description VARCHAR(140) DEFAULT('no description set'),
	value NUMERIC NOT NULL,
	type SMALLINT NOT NULL,
	state SMALLINT NOT NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	CONSTRAINT fk_expense_type FOREIGN KEY (type) REFERENCES EXPENSE_TYPE (id),
	CONSTRAINT fk_expense_state FOREIGN KEY (state) REFERENCES EXPENSE_STATE (id)
);

CREATE TABLE EXPENSE_TYPE (
	id SMALLINT NOT NULL,
	type VARCHAR(40) NOT NULL,
	CONSTRAINT pk_expense_type PRIMARY KEY (id)
);

CREATE TABLE EXPENSE_STATE (
	id SMALLINT NOT NULL,
	state VARCHAR(40) NOT NULL,
	CONSTRAINT pk_expense_state PRIMARY KEY (id)
);

INSERT INTO public.expense_state(id, state) VALUES (1, 'OPEN');
INSERT INTO public.expense_state(id, state) VALUES (2, 'CLOSED');

INSERT INTO public.expense_type(id, type) VALUES (1, 'HOUSE');
INSERT INTO public.expense_type(id, type) VALUES (2, 'CAR');
INSERT INTO public.expense_type(id, type) VALUES (3, 'WATER');
INSERT INTO public.expense_type(id, type) VALUES (4, 'OTHER');