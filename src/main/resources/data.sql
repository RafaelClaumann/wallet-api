INSERT INTO CLIENT(ID, CPF, NAME, TELEPHONE_NUMBER)
    VALUES (1, '000000000-00', 'Rafael Claumann', '48 0 0000-0000');

INSERT INTO WALLET(ID, BALANCE, DESCRIPTION, CLIENT_ID)
    VALUES (1, 8000.0, 'Carteira Principal', 1);

INSERT INTO EXPENSE(ID, DESCRIPTION, EXPENSE_STATE, EXPENSE_TYPE, VALUE, WALLET_ID)
    VALUES (1, 'Gastos com o Carro', 'PAID', 'CAR', 50.0, 1);