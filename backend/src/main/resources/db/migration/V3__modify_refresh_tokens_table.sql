ALTER TABLE refresh_tokens
DROP COLUMN token;

ALTER TABLE refresh_tokens
ADD COLUMN previous_token VARCHAR;

ALTER TABLE refresh_tokens
RENAME COLUMN id to token;

ALTER TABLE refresh_tokens
ALTER COLUMN token TYPE VARCHAR;