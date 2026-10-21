---
target: active IDE file detalle-usuario.html
total_score: 13
max_score: 40
na_heuristics: 
p0_count: 0
p1_count: 3
target_identity: "file:C:\\Users\\Angelica\\Documents\\marcos\\Proyecto_Biblioteca_Marcos_Web\\src\\main\\resources\\templates\\detalle-usuario.html"
target_fingerprint: "sha256:1820821d7648eefac6188aa39c16cdcd830c5966f45efda0d0b6870214cef333"
target_path: "C:\\Users\\Angelica\\Documents\\marcos\\Proyecto_Biblioteca_Marcos_Web\\src\\main\\resources\\templates\\detalle-usuario.html"
timestamp: 2026-10-19T17-31-43Z
slug: src-main-resources-templates-detalle-usuario-html
---
# Critique: `src/main/resources/templates/detalle-usuario.html`

## Design health score

| # | Heuristic | Score | Key issue |
|---|---|---:|---|
| 1 | Visibility of System Status | 1/4 | Buttons do not confirm results; statuses have no explanation. |
| 2 | Match System / Real World | 2/4 | Familiar lending terms, but the activity heading is English and dates lack context. |
| 3 | User Control and Freedom | 1/4 | Edit, status, export, and row-menu controls do not operate. |
| 4 | Consistency and Standards | 2/4 | Shared admin shell, but mixed icon styles and language weaken consistency. |
| 5 | Error Prevention | 1/4 | Status change has no explanation or confirmation. |
| 6 | Recognition Rather Than Recall | 2/4 | Profile and loan fields are visible, but record identity/currentness are unclear. |
| 7 | Flexibility and Efficiency | 1/4 | Search exists, but no useful row actions or batch workflow. |
| 8 | Aesthetic and Minimalist Design | 2/4 | Clear grouping, but small type and muted hierarchy reduce scanability. |
| 9 | Error Recovery | 1/4 | No success, failure, or undo path is visible. |
| 10 | Help and Documentation | 0/4 | No contextual help for state or lending terms. |
| **Total** | | **13/40** | **Poor (32.5%)** |

## Design specificity verdict

The page reads like a category-interchangeable admin template with a few Bibliot. cues: the wordmark, warm sidebar, and book-loan table. Its core visual language remains generic. The English “User Activity Timeline” breaks the Spanish interface.

The bundled detector returned one warning: `flat-type-hierarchy` in `src/main/resources/templates/detalle-usuario.html` (line 0), reporting body/headings all at 16px and a 1.00:1 largest type step against a 1.25:1 target. The detector exited with code 1 because it could not resolve `./css/bibliot.css?v=3` relative to the template path; color and custom-property checks are incomplete. Browser visualization was skipped because browser automation is unavailable. No overlay was produced. The review is source-based, not a live visual inspection.

## Overall impression

The identity and loan information are grouped sensibly, but the profile suggests staff can act when its controls are inert. The largest opportunity is to make this a trustworthy operational record: identify the member clearly, explain account state, and give accurate feedback for actions.

## What’s working

- The split between identity details and loans/activity follows a staff member’s likely scan path.
- The loan table labels book, loan date, due date, and state, making the central record easy to locate.
- The CSS stacks the profile columns on narrow screens and allows timeline rows to wrap.

## Priority issues

### [P1] Controls promise actions that do not work

**Why it matters:** Staff can’t complete visible tasks, and keyboard/screen-reader users will waste time reaching inert controls. The row `⋮` is plain text, not a control.

**Fix:** Connect Edit, Change status, Export, notifications/profile, and row actions to real behavior; where a capability is not implemented, render it as clearly unavailable text rather than an active-looking button.

**Suggested command:** `/impeccable harden`

### [P1] Account state has no meaning or safe transition

**Why it matters:** “Desactivado” and “Cambiar estado” imply consequential access changes without saying what changes, asking for confirmation, or providing a result or recovery path.

**Fix:** Clarify whether the state concerns login access, membership, or both; explain impact before the change and show a confirmed success/failure state. Do not imply persistence until connected, consistent with PRODUCT.md.

**Suggested command:** `/impeccable clarify`

### [P1] The user record is ambiguous

**Why it matters:** The card says “Emeto Mamani” while the full name is “Emeto Winner Montes”; no stable identifier or currentness cue anchors the overdue loan and account state.

**Fix:** Label display name versus full/legal name if both are needed, and include a stable member identifier and clear state definition.

**Suggested command:** `/impeccable clarify`

### [P2] Language and dates are inconsistent

**Why it matters:** An English timeline heading interrupts the Spanish interface, and dates such as 12/05/2026 can be misread across locales; relative times omit an anchor.

**Fix:** Localize the timeline heading and event copy; use an unambiguous date format and semantic timestamps with a full date/time.

**Suggested command:** `/impeccable clarify`

### [P2] Loan actions and status are hard to read and operate

**Why it matters:** The table has an empty final header, unlabelled `⋮`, pastel status colors, and very small text (~0.58–0.68rem). Meaning may be missed by low-vision users and assistive technology.

**Fix:** Add a named actions column and real labeled buttons, improve text contrast and size, use semantic time values, and provide a deliberate narrow-screen table treatment.

**Suggested command:** `/impeccable audit`

## Persona red flags

- **Alex (power user):** Repeated member/loan work is one record at a time; no useful per-loan menu or bulk path is available.
- **Jordan (first-timer):** “Cliente,” “Plazo vencido,” and the consequence of changing status are unexplained; there is no visible help or action feedback.
- **Sam (accessibility-dependent):** Sidebar glyphs may be announced as symbols; the final table column is unnamed, `⋮` cannot receive focus, status relies on pale color, and small text harms readability.

## Cognitive load

Single focus, grouping, one-thing-at-a-time presentation, working-memory support, and progressive disclosure pass. Chunking, visual hierarchy, and minimal choices fail: five sidebar links, tiny profile copy, mixed-language timeline, two profile actions plus export, and an apparent row menu contribute to moderate load. Each loan row visually suggests an additional decision, but its menu is not interactive.

## Emotional journey

The record starts calmly, but disabled membership and an overdue loan are high-stakes states without explanation or a clear next step. The risky-looking status action has no confirmation or feedback, and the timeline ends without reassurance that the record is current or any action succeeded.

## Minor observations

- The email capitalization is inconsistent with typical display formatting.
- `<time>` elements lack `datetime` values.
- Search is labelled only “Buscar.”
- The info icon has no linked explanation.
- “Roles” and “Ajustes” fragment links have no corresponding targets here.
- The active “Usuarios” link lacks `aria-current="page"`.
- On mobile, the profile grid stacks, but the loan table still requires horizontal scrolling.

## Questions to consider

- Does “Desactivado” refer to login access, membership, or both?
- Should staff change status without seeing the consequences first?
- Is the activity timeline an operational audit log or a recent-events summary?
