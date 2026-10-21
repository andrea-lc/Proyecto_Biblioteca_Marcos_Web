---
target: todos los archivos HTML del proyecto (src/main/resources/templates)
total_score: 13
max_score: 40
na_heuristics: 
p0_count: 0
p1_count: 3
target_identity: "file:C:\\Users\\Angelica\\Documents\\marcos\\Proyecto_Biblioteca_Marcos_Web\\src\\main\\resources\\templates"
timestamp: 2026-10-19T17-45-47Z
slug: src-main-resources-templates
---
# Critique: `src/main/resources/templates` (11 HTML templates)

Method: dual-agent (A: `/root/critique_all_design_review` · B: `/root/critique_all_detector`)

## Design Health Score

| # | Heuristic | Score | Key issue |
|---|---|---:|---|
| 1 | Visibility of System Status | 1/4 | Controls rarely confirm results; data lacks freshness/context. |
| 2 | Match System / Real World | 2/4 | Library terms are familiar, but some labels and data lack context. |
| 3 | User Control and Freedom | 1/4 | Many apparent controls have no clear outcome. |
| 4 | Consistency and Standards | 2/4 | Shared brand, with inconsistent glyphs, sizes, and behavior. |
| 5 | Error Prevention | 1/4 | Forms and consequential actions offer little guidance or recovery. |
| 6 | Recognition Rather Than Recall | 2/4 | Labels exist, but small type and unexplained states slow scanning. |
| 7 | Flexibility and Efficiency | 1/4 | Search and filters do not form consistent workflows. |
| 8 | Aesthetic and Minimalist Design | 2/4 | Grouping helps, though dense controls and weak hierarchy reduce clarity. |
| 9 | Error Recovery | 1/4 | Few visible paths to recover from an action or failure. |
| 10 | Help and Documentation | 0/4 | No contextual help for states, filters, or library rules. |
| **Total** | | **13/40** | **Poor (32.5%)** |

This score summarizes the weakest recurring patterns across a mixed set of reader, staff, authentication, and form screens, not one user journey. Review was source-only; no pages were rendered.

## Design Specificity Verdict

The product reads as a Spanish-language library and separates public discovery from staff administration. However, very small typography, placeholder destinations, and mixed icon glyphs make parts of it feel like generic Bootstrap scaffolding.

The deterministic detector reported 8 findings in 7 templates:

- `flat-type-hierarchy` warning in `dashboard.html`, `detalle-fisico.html`, `detalle-usuario.html`, `detalle-virtual.html`, `login.html`, and `registro.html` (line 0 in each; it calculated 16px headings and body text, maximum step 1.00:1 against a 1.25:1 target).
- `broken-image` warning in `nuevo-libro.html` (line 0): an image has no `src`.
- `monotonous-spacing` warning in `registro.html` (line 0): an approximately 8px spacing value occurs 10 of 15 times (67%).

No findings were reported in `explorar.html`, `index.html`, `libros.html`, or `usuarios.html`. The detector could not resolve `./css/bibliot.css` from template paths, so color and custom-property checks may be incomplete. Browser visualization was skipped because automation was not available; no overlay or screenshots were produced.

## Overall Impression

Bibliot. has recognizable Spanish library content and separate reader and staff surfaces. Dense controls, undersized type, and actions without clear outcomes make several screens feel unfinished. The biggest opportunity is consistent, readable task feedback without implying that scaffolded backend actions succeeded.

## What's Working

- Spanish UI and recognizable titles, authors, and categories ground the screens in the library domain.
- Forms and tables often include labels, table headers, and alt text for book covers.
- Public discovery and staff management have distinct surfaces; physical and digital books have separate detail templates.

## Priority Issues

### [P1] Controls look operable but do not consistently work

**Why it matters:** Menus, filters, view toggles, pagination, row menus, export, reservation, and read actions can promise outcomes users do not receive.

**Fix:** Connect controls where a frontend flow exists, or clearly mark unavailable prototype actions without suggesting successful persistence.

**Suggested command:** `/impeccable harden`

### [P1] Readability and accessibility vary across screens

**Why it matters:** Shared CSS uses very small text and controls on book details, exploration, and forms; some placeholders and status colors are faint.

**Fix:** Raise body and control sizes, improve contrast, enlarge touch targets, and provide visible keyboard focus.

**Suggested command:** `/impeccable audit`

### [P1] Navigation contains incomplete destinations

**Why it matters:** Links for Roles, Settings, Configuration, and Help point to fragments without matching destinations, which makes the library shell feel unfinished.

**Fix:** Link to real destinations or clearly mark those items unavailable; make active navigation consistent.

**Suggested command:** `/impeccable clarify`

### [P2] Reports lack temporal context

**Why it matters:** Dashboard metrics and charts look authoritative but have no reporting period, source, or last-updated date.

**Fix:** Add that context or label the values as sample data.

**Suggested command:** `/impeccable clarify`

### [P2] Forms need clearer instructions and states

**Why it matters:** The new-book form labels a file input “Archivo URL” and uses a decorative plus sign; registration includes a terms link without a destination.

**Fix:** Clarify accepted files, required fields, and the terms destination; provide helpful validation and submission states.

**Suggested command:** `/impeccable harden`

## Persona Red Flags

- **New reader:** May try to reserve or read without knowing whether the action succeeded.
- **Low-vision or mobile user:** Small type and controls, faint placeholders, and horizontal lists make scanning and tapping harder.
- **Library staff:** Static figures and inactive menus can be mistaken for current data or working tools.

## Cognitive Load

Grouping in forms and profile panels helps. Hierarchy, explanations, and filter flow fail: exploration shows five selects beside advanced search, and multiple controls have no clear outcome. More than four visible choices appear in several places: six homepage categories, five admin-nav links, exploration filters plus genre choices, and roughly eight new-book fields.

## Emotional Journey

Discovery starts calmly. Uncertainty appears when filters or detail actions do not confirm what happened. In administration, metrics appear authoritative without freshness context. Form submission can end without a clear next step.

## Minor Observations

- Some public pages use mixed Unicode and Material Symbols icon families.
- Static dashboard figures lack a date range.
- Remote image/CDN assets require network access.
- Category-card labels need a reliable text overlay for contrast.
- Sample content and cover imagery may not match consistently.

## Questions to Consider

- Which visible controls are intended to work in the prototype, and should unavailable ones be explicitly marked?
- What should staff trust before acting on a loan state: its date range, last update, or source record?
- Should reader discovery and staff management keep a shared shell, or have a stronger visual distinction?
