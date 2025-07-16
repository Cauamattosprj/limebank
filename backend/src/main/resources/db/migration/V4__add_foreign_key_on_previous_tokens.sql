ALTER TABLE refresh_tokens
ADD FOREIGN KEY (previous_token) REFERENCES refresh_tokens(token)
ON DELETE restrict;