```mermaid　
erDiagram
    entity user {
        bigint id PK
        String name
        int lv
        int exp
        int point
        int rank
    }

    entity post {
        bigint id PK
        bigint user_id FK "user"
        string title
        string comment
        string lang
        timestamp posted_at
    }

    entity code {
        bigint id PK
        bigint code_id FK "code"
        string url
        int nol
        int point
        int nor
    }

    entity access {
        bigint id PK
        bigint user_id FK "user"
        bigint code_id
        timestamp accessed_at
    }

    entity finished_reading {
        bigint id PK
        bigint user_id FK "user"
        bigint code_id
        timestamp finished_at
    }

    entity bookmark {
        bigint id PK
        bigint user_id FK "user"
        bigint code_id
    }

    user ||--o{ post : ""
    user ||--o{ access : ""
    user ||--o{ finished_reading : ""
    user ||--o{ bookmark : ""
    post ||--|{ code : ""
```