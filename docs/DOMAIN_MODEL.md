# AntHill Domain Model

The following diagram illustrates the domain model of the AntHill Inventory Management System, following the MVVM architecture and the audit/soft-delete patterns established in Phase 1.

```mermaid
erDiagram
    USER ||--o{ USER_ROLE : has
    ROLE ||--o{ USER_ROLE : assigned_to
    ROLE ||--o{ PERMISSION : includes
    
    ITEM }o--|| CATEGORY : belongs_to
    ITEM }o--|| LOCATION : stored_in
    
    LOCATION ||--o{ LOCATION : nested_within

    USER {
        long id PK
        string username
        string passwordHash
        string fullName
        boolean is_active
        string created_by
        string updated_by
        long created_at
        long updated_at
    }

    ROLE {
        long id PK
        string name
        boolean is_active
        string created_by
        string updated_by
        long created_at
        long updated_at
    }

    PERMISSION {
        long id PK
        string name
        string description
    }

    CATEGORY {
        long id PK
        string name
        boolean is_active
        string created_by
        string updated_by
        long created_at
        long updated_at
    }

    ITEM {
        long id PK
        string name
        string sku
        string barcode
        long category_id FK
        string unit_of_measure
        int min_stock_level
        int max_stock_level
        boolean is_active
        string created_by
        string updated_by
        long created_at
        long updated_at
    }

    LOCATION {
        long id PK
        string location_code
        string name
        long parent_id FK
        string location_type
        double capacity
        boolean is_active
        string created_by
        string updated_by
        long created_at
        long updated_at
    }
```
