---
target: todos los archivos HTML del proyecto (src/main/resources/templates)
total_score: 13
max_score: 40
na_heuristics: 
p0_count: 0
p1_count: 3
target_identity: "file:C:\\Users\\Angelica\\Documents\\marcos\\Proyecto_Biblioteca_Marcos_Web\\src\\main\\resources\\templates"
timestamp: 2026-10-19T17-51-26Z
slug: src-main-resources-templates
---
# Critique: all 11 HTML templates (`src/main/resources/templates`)

Method: dual-agent (A: `/root/critique_current_design` · B: `/root/critique_current_detector`)

## Design Health Score

| # | Heuristic | Score | Key issue |
|---|---|---:|---|
| 1 | Visibility of System Status | 1/4 | Many controls give no result; reports lack freshness. |
| 2 | Match System / Real World | 2/4 | Library terms are familiar, but metrics and example states lack context. |
| 3 | User Control and Freedom | 1/4 | Menus, filters, pagination, and buttons lack clear frontend outcomes. |
| 4 | Consistency and Standards | 2/4 | Brand remains, but type scale, icons, and behavior vary by surface. |
| 5 | Error Prevention | 1/4 | Required fields exist, but guidance and consequence context are limited. |
| 6 | Recognition Rather Than Recall | 2/4 | Labels help; tiny text and ambiguous icons make options harder to recognize. |
| 7 | Flexibility and Efficiency | 1/4 | Search and filters lack a consistent apply, reset, and confirm flow. |
| 8 | Aesthetic and Minimalist Design | 2/4 | Warm identity is recognizable, but density and small type weaken hierarchy. |
| 9 | Error Recovery | 1/4 | Some forms show conditional alerts; recovery from actions is unclear. |
| 10 | Help and Documentation | 0/4 | No contextual help for filters, states, lending rules, or fields. |
| **Total** | | **13/40** | **Poor (32.5%)** |

The set spans different pages and tasks, so this is an orientative cross-screen score. The total is the sum of the individual ratings above.

## Design Specificity Verdict

The library identity appears in the public catalog and admin panel. `detalle-usuario.html` is now clearer and more spacious than the rest, but that improvement has not carried through to the dashboard, tables, book details, authentication, and book entry form.

The detector reported 8 warnings in 7 templates:

- `flat-type-hierarchy` in `dashboard.html`, `detalle-fisico.html`, `detalle-usuario.html`, `detalle-virtual.html`, `login.html`, and `registro.html`.
- `broken-image` in `nuevo-libro.html`: the preview image has no `src`. This appears to be a hidden preview slot until a cover is selected.
- `monotonous-spacing` in `registro.html`: approximate 8px spacing occurs 10 of 15 times.

The detector could not resolve the linked CSS from any template; color and custom-property checks may be incomplete. Browser visualization was skipped because automation was unavailable; no overlay or screenshots were produced. This review is source-only.

## Overall Impression

The brand and library subject are clear, and the public discovery flow is recognizable. The user-profile page improved, but small type and controls without clear outcomes make much of the rest feel unfinished.

## What's Working

- Home, explore, and book detail form a recognizable discovery journey.
- Forms and tables have a useful semantic foundation: labels, headers, and image alt text.
- The user-detail page has improved type, focus visibility, mobile layout, and unavailable-action messaging.

## Priority Issues

### [P1] Controls do not consistently match their outcomes

**Why it matters:** Filters, view toggles, export, and row menus in explore, users, and books look operational without a clear flow.

**Fix:** Define which prototype controls work in the frontend and mark the rest as demonstrative or unavailable.

**Suggested command:** `/impeccable harden`

### [P1] Carry readable type and focus through the rest of the site

**Why it matters:** Dashboard, explore, book details, and tables have especially small controls and type in filters, metadata, and data rows.

**Fix:** Apply the improved user-profile scale to the other screens, increase touch areas, improve contrast, and keep keyboard focus visible.

**Suggested command:** `/impeccable audit`

### [P1] Give figures and states context

**Why it matters:** Reports lack a period or last-updated date, and some loan statuses do not explain their meaning.

**Fix:** Add a time frame and clear status descriptions, or identify values as sample data.

**Suggested command:** `/impeccable clarify`

### [P2] Complete navigation destinations

**Why it matters:** Several pages link to fragments such as `#roles`, `#ajustes`, `#configuracion`, and `#ayuda` without visible matching sections. The mobile menu also needs a clear response.

**Fix:** Link to real destinations or mark these items unavailable; give the mobile menu a visible state.

**Suggested command:** `/impeccable clarify`

### [P2] Clarify form fields and actions

**Why it matters:** “Archivo URL” describes a file input, the `+` is decorative, and the registration terms link has no visible destination.

**Fix:** Name the file field accurately, explain accepted formats, remove or wire the decorative control, and resolve the terms destination.

**Suggested command:** `/impeccable harden`

## Persona Red Flags

- **New reader:** May reserve, read, or activate filters without knowing whether they worked.
- **Library staff:** May mistake undated metrics for current data and inactive table menus for available actions.
- **Mobile or low-vision user:** Small controls, horizontal scrolling, and faint contrast slow down consultation.

## Cognitive Load

Grouping helps, but hierarchy, filter application, and control purpose remain unclear. More than four choices appear in the admin menu, six home categories, five explore filters, and the new-book form with roughly eight fields.

## Emotional Journey

The home page invites discovery, but controls sometimes fail to confirm outcomes. Admin metrics appear current without freshness information. The new-book form's file label and decorative plus sign create doubt.

## Minor Observations

- Some pages mix Unicode glyphs and Material Symbols.
- Book-detail category and options controls link to fragments or lack a clear path.
- The new-book form has required fields but little expected-format guidance.
- Source-only review cannot confirm runtime behavior or contrast in every state.

## Questions to Consider

- Which controls should be operational in the frontend prototype and which should be marked demonstrative?
- What date range and source should dashboard metrics show?
- Should reader discovery or staff administration take priority in the shared visual system?
