```mermaid
erDiagram
    user {
        bigint id PK
        String name
        int lv
        int exp
        int point
        int grade
    }

    post {
        bigint id PK
        bigint user_id FK "user"
        string title
        string comment
        string lang
        timestamp posted_at
    }

    code {
        bigint id PK
        bigint post_id FK "code"
        string url
        int loc
        int point
        int reader_count
    }

    access {
        bigint id PK
        bigint user_id FK "user"
        bigint code_id
        timestamp accessed_at
    }

    finished_reading {
        bigint id PK
        bigint user_id FK "user"
        bigint code_id
        timestamp finished_at
    }

    bookmark {
        bigint id PK
        bigint user_id FK "user"
        bigint code_id
    }

    title {
        bigint id PK
        bigint user_id FK "user"
        int title_list_id
    }

    grade {
        int id PK
        int value
    }

    title_list {
        int id PK
        string title
    }

    user ||--o{ post : ""
    user ||--o{ access : ""
    user ||--o{ finished_reading : ""
    user ||--o{ bookmark : ""
    user ||--o{ title : ""
    post ||--|{ code : ""
```
