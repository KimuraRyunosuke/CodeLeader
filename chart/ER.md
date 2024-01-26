```mermaid
erDiagram
    user {
        string id PK
        string name
        int lv
        int exp
        int point
        string grade
    }

    post {
        bigint id PK
        string user_id FK "user"
        string title
        string comment
        string lang
        timestamp posted_at
    }

    code {
        bigint id PK
        bigint post_id FK "code"
        string url
        string file_name
        string lang
        int loc
        int point
        int reader_count
    }

    access {
        bigint id PK
        string user_id FK "user"
        bigint code_id
        timestamp accessed_at
    }

    finished_reading {
        bigint id PK
        string user_id FK "user"
        bigint code_id
        timestamp finished_at
    }

    bookmark {
        bigint id PK
        string user_id FK "user"
        bigint code_id
    }

    memo {
        bigint id PK
        string user_id FK "user"
        bigint code_id FK "user"
        string text
        int add_point
    }

    grade {
        string id PK
        int value
    }

    user ||--o{ post : ""
    user ||--o{ access : ""
    user ||--o{ finished_reading : ""
    user ||--o{ bookmark : ""
    user ||--o{ memo : ""
    post ||--|{ code : ""
```
