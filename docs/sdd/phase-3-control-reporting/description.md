# Phase 3 — Control & Reporting (کنترل و گزارش)

## Purpose

This phase adds the controls that keep the system trustworthy over time
(physical stock counts and adjustments), the alerting that keeps operators
proactive (low-stock warnings, dashboard), and the reporting that makes the
data useful to management (reports + Excel export). Everything here reads
from the transaction data produced in Phase 2; nothing here changes the
core receipt/issue/transfer workflows.

## Prerequisites from Phase 1 & 2

Requires the item/location/user model from Phase 1 and the transaction
history ledger + real-time inventory view from Phase 2. Stock adjustments
built in this phase must append to the *same* shared transaction history
log defined in Phase 2, using the "Stock adjustment" event type already
reserved for them there.

## Scope of this phase

### 1. Stock Count & Adjustment (انبارگردانی و اصلاح موجودی)

Minimum required for MVP:

- Record the physically counted quantity.
- Compare counted quantity against system quantity.
- Display the shortage or surplus (کسری یا اضافه).
- Record a reason for the discrepancy.
- Require approval of the adjustment by a Supervisor or Admin role before
  it takes effect.

**Adjustments must never write directly to the stock quantity.** First
create an adjustment record (pending state); only once approved does it
apply to stock and get written to the transaction history log. This
preserves a full audit trail of who counted what, who approved it, and why
a discrepancy occurred.

### 2. Low Stock Alerts (هشدار حداقل موجودی)

Uses the minimum-stock field already defined on each item in Phase 1. Items
below their minimum must be surfaced in three places:

- Dashboard (this phase, section 3 below)
- A dedicated alerts page
- The low-stock report (this phase, section 4 below)

SMS and email notifications are **not** part of the MVP — in-app
notification/display is sufficient. Do not build any outbound
notification integration in this phase.

### 3. Management Dashboard (داشبورد مدیریتی)

The initial dashboard should include:

- Total number of items
- Total number of warehouses
- Approximate inventory value (only if purchase price has been recorded —
  if Phase 1 did not implement the purchase-price field, omit this metric
  rather than faking it)
- Number of receipts today
- Number of issues today
- Low-stock items
- Most recent operations performed
- Open discrepancies (unapproved adjustments)
- Stock per warehouse

Keep this dashboard simple — a small set of summary cards/widgets and a
short recent-activity list. Do not build a complex, highly configurable BI
dashboard; that is explicitly out of scope for the MVP.

### 4. Reports & Excel Export (گزارش و خروجی Excel)

Required reports:

- Inventory report
- Goods receipt report
- Goods issue report
- Transfers report
- Single-item movement report (per-item transaction history, using the
  Phase 2 ledger)
- Low-stock report
- Discrepancies report

Required report capabilities, applied consistently across all of the
reports above:

- Filter by date range
- Filter by item
- Filter by warehouse
- Search
- Export to Excel or CSV
- Print

Build the filter/export/print behavior as a shared, reusable report
component/pattern rather than re-implementing it seven separate times.

## Explicitly out of scope for this phase

Same exclusion list as prior phases. Specifically for this phase: no
SMS/email delivery of alerts, no complex/configurable BI-style dashboards,
no automated forecasting of when stock will run out (that's the
AI/forecasting item on the excluded list). Barcode scanning is Phase 4.

## Acceptance criteria

- A stock count can be entered, compared to system quantity, and shows the
  correct shortage/surplus.
- An adjustment is not applied to stock until approved by a Supervisor or
  Admin, and the approval is attributed to that user.
- Approved adjustments appear correctly in the Phase 2 transaction history
  log.
- Items below minimum stock appear on the dashboard, the alerts page, and
  the low-stock report simultaneously and consistently.
- The dashboard shows every metric listed above (or correctly omits
  inventory value if price data doesn't exist).
- Every report listed above supports date/item/warehouse filtering, search,
  Excel/CSV export, and print.

## Dependencies

Requires Phase 1 (items, minimum-stock field, users/roles) and Phase 2
(transaction history ledger, real-time inventory) to be complete.
