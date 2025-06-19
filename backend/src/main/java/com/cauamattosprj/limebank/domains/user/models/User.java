package com.cauamattosprj.limebank.domains.user.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class User {
    private UUID id;
    private UUID customer_id;

    @NonNull
    private String email;

    @NonNull
    private String password;

    private String role;
    private Instant created_at;
    private Instant updated_at;
    private Instant deleted_at;
    private boolean is_deleted;
}
