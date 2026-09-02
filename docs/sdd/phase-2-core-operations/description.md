# Phase 2 — Core Operations (عملیات اصلی)

## Purpose

This phase implements the actual warehouse transactions: goods receipt,
goods issue, internal transfer, real-time stock visibility, and the
transaction history log. This is the operational heart of the MVP — the
"main warehouse cycle" (تعریف کالا → ورود → خروج → کنترل موجودی → گزارش)
described in the source spec becomes usable at the end of this phase.

## Prerequisites from Phase 1

This phase assumes Phase 1 is complete: users/roles exist, items exist,
categories exist, and the warehouse/location hierarchy exists, all with the
audit (`created_by`/timestamps) and soft-delete (`is_active`) patterns
already in place. Every transaction built here must record the acting user
via that same audit pattern — do not invent a new one.

## Scope of this phase

### 1. Goods Receipt (ثبت ورود کالا)

Base workflow:

1. Select supplier.
2. Select item.
3. Enter quantity.
4. Select destination location.
5. Enter date and notes.
6. Increase stock.

Required fields on the receipt document:

- Receipt number
- Supplier
- Receipt date
- Items
- Quantity per item
- Destination location
- Recording user
- Notes

A receipt document supports multiple line items (multiple item/quantity/
location combinations under one receipt number), not just a single item —
build the line-item structure now so later reporting can group by receipt.

### 2. Goods Issue (ثبت خروج کالا)

Base workflow:

1. Select item.
2. Specify quantity.
3. Select source location (pick location).
4. Check stock availability.
5. Record the reason for issue / destination.
6. Decrease stock.

Required fields on the issue document:

- Issue number
- Issue date
- Destination / requester
- Items
- Quantity per item
- Pick location
- Recording user
- Notes

**Hard constraint: the system must never allow an issue quantity greater
than the available stock at the selected location.** This check happens at
submission time, not just at the UI level — enforce it server-side.

### 3. Internal Transfer (انتقال داخلی کالا)

Required for any medium or large warehouse. Must support:

- Selecting the item at the source location.
- Specifying the destination location.
- Entering the transfer quantity.
- Recording the transfer.
- Decreasing stock at the source.
- Increasing stock at the destination.

Example: Shelf A-01 → Shelf B-03, item: network cable, quantity: 20.

A transfer is logically one document that both decrements source and
increments destination in a single atomic operation — never allow a state
where only one side of the transfer has been applied.

### 4. Real-Time Inventory (موجودی لحظه‌ای)

The stock view must support filtering by:

- Item name
- Item code
- Barcode
- Category
- Warehouse
- Location
- Low-stock items
- Out-of-stock items

For each item, display:

- Total stock
- Stock per warehouse
- Stock per location
- Available (pickable) stock
- Last receipt date
- Last issue date

### 5. Transaction History (تاریخچه گردش کالا)

For every item, the full event history must be viewable, including:

- Receipt
- Issue
- Transfer
- Stock adjustment (built in Phase 3, but the history log's schema must
  already be able to represent this event type)
- Return

Example table shape:

| Date | Operation | Source | Destination | Quantity | User |
|---|---|---|---|---:|---|
| 1405/06/01 | Receipt | Supplier | A-01 | 50 | Admin |
| 1405/06/02 | Transfer | A-01 | B-02 | 10 | Operator |
| 1405/06/03 | Issue | B-02 | Customer | 5 | Operator |

This log is the primary tool for tracing errors, so every stock-changing
operation in this phase (and later phases) must write one row here — treat
it as a single shared "stock ledger" table that receipt, issue, and transfer
all append to, rather than three separate logs.

## Explicitly out of scope for this phase

Same exclusion list as Phase 1 (RFID, AI/forecasting, robots, complex
picking-path optimization, full accounting/ERP, SMS/email, multi-company,
full purchase/sales system, advanced transport management, complex
dashboards, in-app chat, full offline support). Also out of scope here
specifically: stock counting/adjustment workflows, low-stock alerting UI,
dashboards, and Excel reports — those belong to Phase 3. Barcode scanning
input belongs to Phase 4; build the receipt/issue forms to accept a barcode
lookup as a normal text/manual entry for now.

## Acceptance criteria

- A receipt with multiple line items can be created and correctly
  increases stock at each specified location.
- An issue is rejected if the requested quantity exceeds available stock at
  the chosen location.
- A transfer atomically decreases source and increases destination stock.
- The real-time inventory view supports every filter listed above and shows
  every field listed above.
- Every receipt, issue, and transfer produces a row in the shared
  transaction history log, and that log can be filtered per item to show a
  correct chronological trail.

## Dependencies

Requires Phase 1 (users, items, categories, warehouse/location hierarchy,
audit + soft-delete patterns) to be complete.
