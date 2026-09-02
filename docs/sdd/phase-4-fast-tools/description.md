# Phase 4 — Fast Tools (ابزارهای سریع‌تر)

## Purpose

This phase makes the day-to-day operator workflows (receipt, issue,
lookups) faster by adding barcode/QR support, in-app notifications, and
mobile UI polish. It is deliberately last because it enhances workflows
that must already exist and be correct (Phases 2 and 3) — barcode scanning
is an alternate *input method* for operations that already work by manual
entry, not a new workflow.

## Prerequisites from Phases 1–3

Requires items with a barcode field (Phase 1), and working receipt/issue
forms (Phase 2) to attach scanning to. Requires the alerts/dashboard from
Phase 3 if in-app notifications are to surface anything meaningful.

## Scope of this phase

### 1. Barcode & QR Code (بارکد و QR Code)

Minimum required for MVP:

- Register a barcode on an item (data model already exists from Phase 1 —
  this is about the UI/workflow to set it, if not already covered).
- Search for an item by barcode.
- Enter receipt quantity via barcode scan.
- Enter issue quantity via barcode scan.
- Support both a mobile camera scanner and a USB barcode scanner as input
  sources.

Barcode scanning must be an additional input path into the *existing*
receipt and issue forms from Phase 2 — do not build a separate,
parallel "scan mode" workflow with its own validation rules. Scanning an
item should simply populate the same item-selection field that manual
search already populates.

Professional label generation and printing (تولید و چاپ لیبل حرفه‌ای) is
explicitly deferred beyond this MVP — do not build a label designer or
print-layout tool.

### 2. In-App Notifications (اعلان‌های داخل برنامه)

In-app only — no SMS, no email (this restriction, set in Phase 3, still
applies here). Notifications should surface things already computed by
Phase 3 (low stock, pending adjustment approvals) inside the app UI (e.g. a
notification bell/list), not introduce new alerting logic.

### 3. Mobile UI Improvements (بهبود رابط کاربری موبایل)

Polish the receipt, issue, and inventory-lookup screens for one-handed,
mobile/warehouse-floor use, since these are the screens operators use most
while walking the floor with a scanner. This is a UI/UX refinement pass on
existing Phase 2 screens, not new functionality.

## Explicitly out of scope for this phase

Same exclusion list as prior phases, plus: professional label
design/printing, RFID (as opposed to barcode/QR), any outbound SMS/email
notification channel.

## Acceptance criteria

- An item can be looked up by scanning its barcode with either a mobile
  camera or a USB scanner.
- Scanning a barcode during receipt or issue correctly populates the same
  item field used by manual entry, and the rest of the Phase 2 workflow
  (quantity, location, stock checks) behaves identically regardless of
  input method.
- Low-stock and pending-approval notifications appear in an in-app
  notification UI, with no outbound SMS/email sent.
- Receipt, issue, and inventory-lookup screens are usable on a small mobile
  viewport.

## Dependencies

Requires Phase 1 (barcode field on items), Phase 2 (receipt/issue forms to
attach scanning to), and Phase 3 (low-stock/approval data to notify about).
