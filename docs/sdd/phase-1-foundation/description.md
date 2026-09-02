# Phase 1 — Foundation (پایه سیستم)

## Purpose

This phase builds the foundational layer that every later phase depends on:
authentication, users & roles, item master data, categories, and the physical
warehouse/location hierarchy. Nothing in Phase 2, 3, or 4 can be implemented
correctly until the data models and access-control rules defined here exist
and are stable, because every later transaction (receipt, issue, transfer,
count) references a user, an item, and a location.

This is an MVP (نسخه اولیه). Do not add capabilities listed under
"Explicitly out of scope" anywhere in this project — they are deferred on
purpose to keep the first version shippable.

## Scope of this phase

### 1. Authentication & Access Control

Minimum roles (exactly these three, no more, no fewer, for the MVP):

- **System Admin (مدیر سیستم)** — full access, user management, role/permission
  assignment.
- **Warehouse Supervisor (سرپرست انبار)** — manage items, locations, approve
  inventory adjustments (انبارگردانی), view all reports.
- **Warehouse Operator (اپراتور انبار)** — perform receipts, issues,
  transfers; read-only on master data.

Required capabilities:

- Login / logout.
- Change password.
- Activate / deactivate a user (never hard-delete a user).
- Assign permissions per operation (role-based, not per-user ad hoc rules,
  to keep MVP simple).
- Every write operation anywhere in the system must record **who** performed
  it (user id + timestamp). This audit column is a foundational requirement
  that all later phases depend on — build it once, here, as a reusable
  pattern (e.g. `created_by`, `updated_by`, `created_at`, `updated_at`).

**Soft delete rule (applies system-wide, defined here, used everywhere):**
Nothing that has warehouse history is ever hard-deleted. Records get an
`is_active` flag instead. This must be established in Phase 1 because Items,
Categories, Warehouses, and Locations are the first entities that need it.

### 2. Item Management (مدیریت کالا)

Required fields for every item:

- Name
- SKU / item code (unique)
- Barcode
- Category (FK to category table)
- Unit of measure (piece, carton, kg, etc.)
- Minimum stock level
- Maximum stock level
- Active / inactive status

Conditional fields — **only build these if the business actually needs them**;
confirm with the stakeholder before implementing, otherwise skip entirely for
MVP:

- Image
- Brand
- Purchase price
- Description
- Serial number
- Batch / Lot
- Expiry date

If the business's items don't use expiry dates or serial numbers, the
serial/batch/expiry fields and any related logic must be left out of the MVP
entirely — do not build hidden/unused columns "just in case."

### 3. Warehouse Structure & Item Location (ساختار انبار و موقعیت کالا)

Must support an arbitrarily nested physical hierarchy, for example:

```
Main Warehouse
 └── Hall A
      └── Aisle 1
           └── Rack 01
                └── Shelf 01
                └── Shelf 02
```

Minimum fields per location node:

- Location code
- Location name/title
- Parent warehouse reference
- Location type (warehouse / hall / aisle / rack / shelf, etc.)
- Optional capacity
- Active / inactive status

Required capabilities:

- View the stock held at a given location.
- View which item(s) occupy a given location.
- Move an item between locations (the actual move *transaction* is built in
  Phase 2 — this phase only needs the location data model and the read views
  above; do not build the transfer workflow yet).

### 4. Categories

A simple category table (name, active/inactive) that items reference. Keep
this flat or single-level for MVP — no nested category trees unless the
source spec is revisited.

## Explicitly out of scope for this phase (and this MVP in general)

RFID, AI/demand forecasting, warehouse robots, complex picking-path
optimization, full accounting system, ERP integration, SMS/email
notifications, multiple client apps per role, multi-company support, full
purchase/sales system, advanced transportation management, highly complex
dashboards, in-app chat, full offline support. If any task in this phase
seems to require one of these, stop and flag it instead of building it.

## Data model deliverables expected from this phase

- `users`, `roles`, `permissions` (or role→permission mapping)
- `items`
- `categories`
- `warehouses`, `locations` (self-referencing or explicit hierarchy)
- A reusable audit pattern (`created_by`/`updated_by`/timestamps) and a
  reusable soft-delete pattern (`is_active`) applied consistently across all
  of the above tables — every table built in later phases must reuse these
  same two patterns rather than inventing new ones.

## Acceptance criteria

- An admin can create the three roles and assign permissions to them.
- A user can log in, log out, and change their password.
- A user can be deactivated and can no longer log in, but their historical
  actions remain visible/attributed.
- An item can be created with all required fields; conditional fields are
  either fully implemented (if confirmed needed) or fully absent (not
  half-built).
- A multi-level location tree can be created, viewed, and traversed.
- Every table created in this phase has `is_active` and audit columns.

## Dependencies

None — this is the first phase. Everything else depends on it.
