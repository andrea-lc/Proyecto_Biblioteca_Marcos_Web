---
target: ayuda.html
total_score: 23
max_score: 40
na_heuristics: 
p0_count: 0
p1_count: 2
target_identity: "file:C:\\Users\\PC\\Documents\\Proyecto_Marcos\\Proyecto_Biblioteca_Marcos_Web\\src\\main\\resources\\templates\\ayuda.html"
target_fingerprint: "sha256:0b56f60834eaebf1ec84cf6365743a38d898c264940625800a70295e6ad9919c"
target_path: "C:\\Users\\PC\\Documents\\Proyecto_Marcos\\Proyecto_Biblioteca_Marcos_Web\\src\\main\\resources\\templates\\ayuda.html"
timestamp: 2026-10-27T05-22-59Z
slug: src-main-resources-templates-ayuda-html
---
Method: degraded single-context; source review completed before detector scan.

## Design Health Score
| # | Heuristic | Score | Finding |
|---|---|---:|---|
| 1 | Visibility of System Status | 2/4 | Accordion exposes expanded/collapsed state; no success feedback for help actions. |
| 2 | Match System / Real World | 3/4 | Questions use familiar library language. |
| 3 | User Control and Freedom | 3/4 | Accordion items can be opened and closed with native Bootstrap behavior. |
| 4 | Consistency and Standards | 2/4 | Bootstrap accordion defaults do not visibly inherit the documented warm card language. |
| 5 | Error Prevention | 2/4 | Loans answer may imply availability although it is conditional on future account connection. |
| 6 | Recognition Rather Than Recall | 3/4 | Topics are phrased as direct questions. |
| 7 | Flexibility and Efficiency | 2/4 | Only three topics; no quick contact or search path. |
| 8 | Aesthetic and Minimalist Design | 3/4 | Page is restrained, though hierarchy and accordion treatment need distinction. |
| 9 | Error Recovery | 1/4 | No actionable recovery/help contact path is supplied. |
| 10 | Help and Documentation | 2/4 | Useful starting answers, but contact text is a placeholder. |
| **Total** | | **23/40** | Source-only evaluation; browser could not be inspected. |

## Design Specificity Verdict
The page uses Bibliot.'s warm background, ink, DM Sans and Playfair Display. Its header and restrained layout fit the system. The accordion remains close to Bootstrap defaults, so it lacks the documented elevated-card language and risks a cooler, flatter visual tone. Detector returned two findings: `flat-type-hierarchy` (likely false positive because it failed to resolve Bootstrap's `.h2` sizing) and an advisory about black text on the brand mark (likely false positive because project CSS sets the brand color). Detector also warned it could not read the linked stylesheet due to resolving the relative path from the template folder.

## Priority Issues
- **[P1] Placeholder help content:** the contact answer says the library must provide its channels. Users cannot act on it. Add the institution's real contact path when known, or remove the question until it exists.
- **[P1] Conditional loans guidance:** “Cuando la cuenta esté conectada” creates uncertainty about a feature whose availability is not confirmed. Clarify the current state and link only to a working route.
- **[P2] Bootstrap accordion drift:** default accordion borders, surface and focus treatment are not specified by the design system. Style its items, buttons, focus state and expanded state with the warm palette and card shadow; retain visible keyboard focus.
- **[P2] Heading scale signal:** the target detector reports a flat hierarchy, but its calculation misses the Bootstrap `.h2` class. Keep the eyebrow secondary and verify rendered heading scale instead of changing it based on this signal alone.

## Persona Red Flags
- **Jordan (first-timer):** the contact answer does not give an actual next step; the loans answer is conditional and may make the reader unsure whether the feature exists.
- **Sam (keyboard / low vision):** Bootstrap's focus treatment may use its default blue rather than the documented palette; confirm focus contrast and visibility after styling.
- **Casey (mobile):** the compact header nav has no explicit mobile layout visible in the template; verify that links remain easy to tap and do not crowd the brand.

## Minor Observations
The first accordion item is open by default. The content is short and the centered column is reasonable for a help page. The stylesheet link detector warning may be a path-resolution false positive because the template sets a root `<base>` URL.

## Questions to Consider
Which help topics are confirmed for this prototype, and what real contact channel should the page provide?
