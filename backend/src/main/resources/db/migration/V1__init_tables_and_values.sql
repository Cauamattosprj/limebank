

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    role VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (role)
VALUES ('USER'), ('ADMIN');

CREATE TABLE users (
    id UUID PRIMARY KEY,
    customer_id UUID,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_user_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON DELETE RESTRICT
);

CREATE TABLE account_types (
    id SERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO account_types (type)
VALUES ('CHECKING_ACCOUNT'), ('SAVINGS_ACCOUNT'), ('INVESTMENTS_ACCOUNT');


CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    account_type_id INTEGER NOT NULL,
    balance INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_account_account_type
        FOREIGN KEY (account_type_id)
        REFERENCES account_types(id)
        ON DELETE RESTRICT
);

CREATE TABLE customers (
    id UUID PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) NOT NULL UNIQUE CHECK (cpf ~ '^\d{11}$'),
    birth_date DATE NOT NULL,
    account_id UUID NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_customer_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(id)
        ON DELETE SET NULL
);



CREATE TABLE addresses_type (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO addresses_type (type)
VALUES ('BILLING_ADDRESS'), ('HOME_ADDRESS');

CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(255) NOT NULL,
    complement VARCHAR(255),
    district VARCHAR(255),
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL,
    address_type_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_address_address_type
    FOREIGN KEY (address_type_id)
    REFERENCES addresses_type(id),

    CONSTRAINT fk_address_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers(id)
    ON DELETE CASCADE
);

CREATE TABLE addresses_history (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    address_id UUID NOT NULL,

    CONSTRAINT fk_address_history_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers(id),

    CONSTRAINT fk_address_history_address
    FOREIGN KEY (address_id)
    REFERENCES addresses(id)
);

CREATE TABLE payment_card_brands (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO payment_card_brands (brand)
VALUES ('VISA'), ('MASTERCARD'), ('ELO'), ('AMEX');

CREATE TABLE payment_cards (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    credit_amount INTEGER DEFAULT 0,
    credit_used INTEGER DEFAULT 0,
    enable_credit_transactions BOOLEAN DEFAULT FALSE,
    enable_debit_transactions BOOLEAN DEFAULT FALSE,
    is_locked BOOLEAN DEFAULT TRUE,
    card_number VARCHAR(24) NOT NULL CHECK (card_number ~ '^\d{24}$'),
    valid_until DATE NOT NULL,
    cvc VARCHAR(10) NOT NULL CHECK (cvc ~ '^\d{3,4}$'),
    brand_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_payment_card_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers(id),

    CONSTRAINT fk_payment_card_brand
    FOREIGN KEY (brand_id)
    REFERENCES payment_card_brands(id)
);

CREATE TABLE transaction_status (
    id SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO transaction_status (status)
VALUES ('PENDING'), ('REFUSED'), ('FINISHED'), ('CANCELED');

CREATE TABLE transaction_type (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO transaction_type (type)
VALUES ('PIX'), ('TED'), ('CREDIT_CARD'), ('DEBIT_CARD');

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    source_account_id UUID NOT NULL,
    destination_account_id UUID NOT NULL,
    transaction_type_id INTEGER NOT NULL,
    amount INTEGER NOT NULL,
    description VARCHAR,
    transaction_status_id INTEGER NOT NULL,
    finished_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_source_account
    FOREIGN KEY (source_account_id)
    REFERENCES accounts(id),

    CONSTRAINT fk_destination_account
    FOREIGN KEY (destination_account_id)
    REFERENCES accounts(id),

    CONSTRAINT fk_transaction_transaction_status
    FOREIGN KEY (transaction_status_id)
    REFERENCES transaction_status(id),

    CONSTRAINT fk_transaction_transaction_type
    FOREIGN KEY (transaction_type_id)
    REFERENCES transaction_type(id)
);


CREATE TABLE transactions_history (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    transaction_id UUID NOT NULL,
    attempt_at TIMESTAMP,
    success BOOLEAN,
    payment_card_id UUID,
    reason VARCHAR,

    CONSTRAINT fk_transaction_history_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers(id)
    ON DELETE RESTRICT
);

CREATE TABLE login_attempts (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    attempt_at TIMESTAMP NOT NULL,
    success BOOLEAN NOT NULL,
    reason VARCHAR NOT NULL,
    ip_address VARCHAR(255) NOT NULL,
    user_agent VARCHAR NOT NULL,

    CONSTRAINT fk_login_attempts_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE RESTRICT
);

CREATE TABLE credit_proposals_status (
    id SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO credit_proposals_status (status)
VALUES ('REFUSED'), ('ACCEPTED'), ('PENDING');

CREATE TABLE credit_proposals (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    status_id INTEGER NOT NULL,
    score INTEGER,
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    evaluated_at TIMESTAMP,

    CONSTRAINT fk_credit_proposals_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_credit_proposals_status
    FOREIGN KEY (status_id)
    REFERENCES credit_proposals_status(id)
);