---
target: index.html
total_score: 27
max_score: 40
na_heuristics: 
p0_count: 0
p1_count: 1
target_identity: "file:C:\\Users\\PC\\Documents\\Proyecto_Marcos\\Proyecto_Biblioteca_Marcos_Web\\src\\main\\resources\\templates\\index.html"
target_fingerprint: "sha256:6a9b3ac58b2ed29220d46497136237a592aa64bdc6be415241f8603a78330435"
target_path: "C:\\Users\\PC\\Documents\\Proyecto_Marcos\\Proyecto_Biblioteca_Marcos_Web\\src\\main\\resources\\templates\\index.html"
timestamp: 2026-10-27T05-33-30Z
slug: src-main-resources-templates-index-html
---
## Design Health Score
| # | Heuristic | Score | Key issue |
|---|---|---:|---|
| 1 | Visibility of System Status | 3/4 | API failure text exists. |
| 2 | Match System / Real World | 4/4 | Catalog concepts and labels are familiar. |
| 3 | User Control and Freedom | 2/4 | The open-menu control appears inert. |
| 4 | Consistency and Standards | 3/4 | Tokens and Bootstrap utilities align better; search shadow remains strong. |
| 5 | Error Prevention | 2/4 | Routes and search flow were not executable in this review. |
| 6 | Recognition Rather Than Recall | 4/4 | Category labels and book covers support recognition. |
| 7 | Flexibility and Efficiency | 2/4 | Horizontal rails may hide additional choices. |
| 8 | Aesthetic and Minimalist Design | 3/4 | Hero competes with the discovery action. |
| 9 | Error Recovery | 2/4 | Error state has no retry action. |
| 10 | Help and Documentation | 2/4 | Help is linked but not visible from all mobile positions. |
| **Total** | | **27/40** | Source-only assessment. |

## Design Specificity Verdict
The page feels authored for Bibliot.: book covers, a warm paper surface, and the editorial serif fit the documented system. Category chips and cover borders now follow the Bootstrap treatment in `ayuda.html`. The scan reported two likely false positives: a black brand-mark color (the project CSS defines its ink color) and Material Symbols as an undocumented text font (it is used as an icon font). It also could not resolve `./css/bibliot.css` relative to the template directory; the root `<base th:href="@{/}">` likely makes the served path valid.

## Priority Issues
- **[P1] Inert “Abrir menú” control:** no handler was found. Connect it to an actual menu action or remove the misleading control if mobile navigation remains visible.
- **[P2] Heavy search shadow:** CSS applies `0 4px 10px #0003`, stronger than the documented soft card shadows and elevating a control. Use Bootstrap `shadow-sm` or leave it flat.
- **[P2] Horizontal rail discoverability:** category and book rows scroll horizontally. Keep that layout but make overflow discoverable and keyboard/touch operable.
- **[P2] Hero/search hierarchy:** the large hero and overlapping, shadowed search compete with book discovery. Keep the image but clarify the search-to-featured-books hierarchy.
- **[P3] Brand scale exception:** sidebar brand is 2.05rem against the documented 2.7rem display size; confirm this smaller navigation treatment is consistent across catalog views.

## Persona Red Flags
- **Alex (power user):** may activate a menu button with no action.
- **Sam (keyboard / low vision):** focus contrast and category-rail keyboard scrolling require rendered verification.
- **Casey (mobile):** may miss content beyond the horizontal category and book rails.

## Minor Observations
Material Symbols is an icon font and does not need to match the text typography scale. Browser rendering was unavailable, so hero balance, breakpoint behavior and actual contrast remain unverified.

## Questions to Consider
Should the menu control open a panel, or should the mobile navigation remain directly visible? Should the category and book rails show an explicit scroll cue?
