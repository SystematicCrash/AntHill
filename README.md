# AntHill Inventory Management System

AntHill is a robust, high-performance inventory management system designed for physical warehouse operations. Built with a modular architecture, it provides comprehensive tracking, hierarchical location management, and role-based access control to streamline warehouse workflows from receipt to dispatch.

## Core Features

- **Hierarchical Warehouse Management**: Supports arbitrarily nested physical structures (Warehouses → Halls → Aisles → Racks → Shelves).
- **Role-Based Access Control (RBAC)**: Distinct roles for System Admins, Warehouse Supervisors, and Operators to ensure operational security.
- **Full Inventory Lifecycle**: Manages the complete lifecycle of items, including receipts, issues, transfers, and inventory adjustments.
- **Audit & Accountability**: Every write operation is fully audited, recording the user and timestamp for complete traceability.
- **Soft-Delete System**: Maintains historical integrity by using soft-deletion for all entities, ensuring warehouse records remain immutable.
- **Optimized Inventory Tracking**: Real-time stock level monitoring with configurable minimum/maximum threshold alerts.
- **Reporting & Analytics**: Comprehensive reporting suite for stock movement, inventory audits, and operational efficiency.

## Architecture

AntHill is built using the **MVVM (Model–View–ViewModel)** pattern, ensuring a clean separation of concerns and high testability across the codebase:

- **Model**: Domain entities, repositories, and business rules, including shared transaction-history ledger logic.
- **ViewModel**: Manages state, handles business rule enforcement (e.g., stock availability checks), and processes user commands.
- **View**: A thin, reactive layer that binds directly to ViewModel state for an efficient and responsive UI.

## Technical Stack

- **Platform**: Android
- **Language**: Kotlin
- **Architecture**: MVVM
- **Database**: Room Persistence Library
- **Concurrency**: Kotlin Coroutines & Flow
- **Dependency Injection**: Hilt

## Getting Started

*(Instructions for cloning, building, and running the project would go here once deployment is finalized.)*

---

### Phase Roadmap

- **Phase 1: Foundation** — User/Role/Permission management, item master data, and hierarchical location modeling.
- **Phase 2: Core Operations** — Receipt, issue, and transfer workflows with a shared transaction-history ledger.
- **Phase 3: Control & Reporting** — Inventory adjustments (count), reporting, and export capabilities.
- **Phase 4: Fast Tools** — Barcode scanning and rapid entry tools for operational speed.
