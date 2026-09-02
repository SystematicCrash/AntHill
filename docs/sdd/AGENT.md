# AGENT.md — Instructions for the Implementing Agent

This file governs how you (the agent) work across every phase in this `sdd`
directory. Read this file in full before starting **any** phase, and
re-check it if you're ever unsure how to proceed. Phase-level
`description.md` files tell you *what* to build; this file tells you *how*
to work.

---

## 1. How work is organized

- Each phase lives in `sdd/<phase-name>/` and contains a `description.md`
  (written for you, do not edit it) and a `plan.md` (written *by* you).
- Before touching any code for a phase: read that phase's `description.md`
  completely, then write your own `plan.md` breaking the phase into ordered,
  concrete steps. Check boxes off only once a step is fully done and
  verified — never check a box in advance of doing the work, and never
  batch-check multiple steps at once to "catch up."
- Work phases in order (1 → 2 → 3 → 4). Do not start a later phase's
  implementation work until the previous phase's "Phase completion
  checklist" is fully satisfied. If you believe parallelizing part of a
  later phase is genuinely necessary, that's a critical decision — raise it
  (see §5) instead of deciding unilaterally.
- If something in a `description.md` is ambiguous, missing, or conflicts
  with an earlier phase's deliverables, do not guess. Log it under that
  phase's "Open questions / blockers" section and raise it (see §5).

---

## 2. Architecture: MVVM

This project uses **MVVM (Model–View–ViewModel)** throughout. This applies
regardless of which specific framework/platform ends up being used:

- **Model** — data entities, repositories, and business/domain logic. No
  knowledge of the View. This is where the audit pattern
  (`created_by`/`updated_by`/timestamps) and soft-delete pattern
  (`is_active`) from Phase 1 live, and where the shared transaction-history
  ledger logic from Phase 2 lives.
- **ViewModel** — exposes state and commands/actions to the View, calls into
  Models/repositories, contains presentation logic (validation, formatting,
  filter state, loading/error state). No direct references to View/UI
  framework types. This is the layer that should hold almost all business
  rules that acceptance criteria describe (e.g. "issue must not exceed
  available stock" is a ViewModel/Model-level rule, not something enforced
  only in a button's click handler).
- **View** — as thin as possible. Binds to ViewModel state, forwards user
  actions to ViewModel commands. Do not put validation, stock-quantity
  checks, filtering logic, or any of the business rules from the
  `description.md` files directly in View code — if you find yourself
  writing an `if` statement in a View that enforces a business rule from a
  phase's acceptance criteria, move it to the ViewModel.

Reuse shared patterns across phases rather than reinventing them per
feature: the audit/soft-delete pattern (Phase 1), the shared transaction
ledger (Phase 2), and the shared report filter/export/print component
(Phase 3) should each be built once and consumed everywhere they're needed.

---

## 3. Testing requirements

Automated tests are **mandatory for every new feature**, written as part of
the same unit of work as the feature — not deferred to a later cleanup pass,
and not deferred to a later phase.

- **ViewModels and Model/business logic must have high coverage.** Target
  at least ~85% line coverage on these layers. Every acceptance-criterion
  bullet listed in a phase's `description.md` should be traceable to at
  least one test that would fail if that criterion were violated.
- **Explicitly required test cases, at minimum, for every feature that
  touches stock quantities:**
  - The "happy path" (valid input, expected result).
  - The boundary/rejection path (e.g. issuing more than available stock
    must be rejected — this exact rule needs its own test, not just
    incidental coverage).
  - Concurrent/atomicity behavior for transfers (source decrement and
    destination increment either both happen or neither does).
- **Views** get lighter coverage (smoke/interaction tests where the
  platform supports them) since business logic shouldn't live there in the
  first place under MVVM.
- **Integration tests** are required wherever a feature crosses a boundary
  that unit tests can't safely fake — e.g. a receipt/issue/transfer
  actually writing correctly to the shared transaction history ledger, or
  an approved adjustment actually updating stock.
- A step in `plan.md` is not "done" until its tests exist, pass, and are
  committed alongside the feature code (see §6). Do not check off a plan.md
  box for a feature with no accompanying tests.
- If you cannot reasonably write a meaningful test for something (rare —
  flag it, don't silently skip it), note why in that phase's "Open
  questions / blockers" section.

---

## 4. Scope discipline

- Every phase's `description.md` has an "explicitly out of scope" list.
  Treat it as a hard boundary, not a suggestion — do not build any of those
  items even if it seems easy or "while you're in there anyway."
- If implementing a required feature seems to genuinely require touching
  something on the out-of-scope list, stop — this is a critical decision
  (see §5), not something to resolve by quietly expanding scope.
- Don't add fields, screens, or tables that aren't called for by the
  current phase's `description.md`, even if you can anticipate a later
  phase needing something similar. Build it in the phase that actually
  calls for it.

---

## 5. Permission to fail, and when to stop and ask

You have permission to fail. It is better to attempt a step, discover it
was wrong, and revert or redo it than to freeze up. Most implementation
decisions (naming, file layout, exact test structure, internal function
decomposition, styling details) are yours to make without asking — don't
seek approval for routine work.

However, you must **stop and explicitly ask for a decision** — rather than
picking an option yourself — when a choice is genuinely arguable and
consequential. This includes, at minimum:

- Any schema/data-model decision that later phases will depend on and that
  isn't fully specified by a `description.md` (e.g. how to structure a
  table that isn't described in enough detail).
- Any conflict between two phases' `description.md` files, or between a
  `description.md` and something already built.
- Any case where satisfying a requirement seems to require touching an
  out-of-scope item (§4).
- Security- or permissions-model decisions not already settled by Phase 1
  (e.g. anything affecting who can approve adjustments, who can see what
  data).
- Irreversible actions — schema migrations that would be destructive to
  redo, deleting data, or anything not easily undone.
- Anything where two reasonable implementations would produce materially
  different user-facing behavior (not just different code structure).

When you hit one of these, do not guess and move on. Log the question
clearly under the relevant phase's `plan.md` → "Open questions / blockers,"
state the options you see and your recommendation if you have one, and
pause that line of work until it's resolved. Routine implementation
judgment calls do not need this treatment — reserve it for things a
reasonable engineer would actually want to weigh in on.

---

## 6. Git & version control workflow

- One branch per phase, named to match the phase directory exactly:
  `phase-1-foundation`, `phase-2-core-operations`,
  `phase-3-control-reporting`, `phase-4-fast-tools`.
- Branch each phase off the previous phase's branch (stacked), not off
  `main` independently — Phase 2 depends on Phase 1's schema, Phase 3 on
  Phase 2's ledger, and so on, so history should reflect that dependency
  order. Merge sequentially into `main` (or your integration branch) once a
  phase is complete, then branch the next phase from the updated base.
- Commit at the granularity of a single `plan.md` step (or a natural
  sub-unit of one, if a step is large) — not one giant commit per phase.
  Each commit should include the feature code and its tests together, per
  §3.
- Only open a PR / merge a phase branch once that phase's "Phase completion
  checklist" in `plan.md` is fully checked, tests pass, and no open
  questions from §5 remain unresolved for that phase.
- Write commit messages that reference the plan.md step they complete, so
  history is traceable back to the plan.

---

## 7. Definition of done (applies at both step and phase level)

A `plan.md` step is done when:
1. The functionality matches what `description.md` describes for it.
2. Tests exist per §3, are committed with the code, and pass.
3. It doesn't include anything from the out-of-scope list.
4. It reuses shared patterns (§2) rather than duplicating logic.

A phase is done when:
1. Every step in its `plan.md` is checked and its "Phase completion
   checklist" is fully satisfied.
2. Every acceptance criterion in its `description.md` is met and covered by
   at least one test.
3. All open questions raised during the phase have been resolved (not left
   pending).
4. The phase branch is merged per §6.

---

## 8. General expectations

- Prefer clarity and correctness over cleverness — this is warehouse
  inventory software; incorrect stock math is worse than slow code.
- Keep documentation (`plan.md` updates, code comments where genuinely
  useful) up to date as you go, not as an afterthought.
- If you change your mind about part of a plan mid-phase, update `plan.md`
  to reflect the actual plan rather than leaving stale, unchecked steps
  that no longer apply.
