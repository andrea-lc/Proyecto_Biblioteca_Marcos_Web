# Product context

## Product

Bibliot. is a Spanish-language web application for discovering books and managing a library.

## Users

- Readers can browse featured books, explore categories, search the catalog, and view physical or digital book details.
- Library staff or administrators can view a dashboard, manage book and user records, and access reporting surfaces.
- The product serves both readers and library staff; keep public discovery and administrative tasks easy to distinguish.

## Purpose

Help readers find books and give library staff a clear place to manage the catalog and membership.

## Current scope and constraints

- The interface and page content are in Spanish; preserve Spanish UI copy and correct accents.
- Keep layouts usable across screen sizes; mobile use is an expected consideration.
- The project is a Spring Boot / Thymeleaf web app with separate reader-facing and administrative page surfaces.
- Current routes and forms are largely presentation scaffolding. Login, registration, and book creation explicitly report that persistence/authentication is not connected; do not imply those actions succeeded.
- Physical and digital book details are distinct product flows.
- No institutional policies or lending rules are documented yet. Keep these as open decisions rather than inventing rules.

## Existing product and visual commitments

- Product name: Bibliot. (also referenced as Bibliot in page titles).
- Existing UI uses Spanish, Bootstrap, DM Sans for interface text, Playfair Display for prominent headings, and a warm cream/taupe/ink palette in `src/main/resources/static./css/bibliot.css`.
- Treat these as the incumbent visual direction when refining existing screens; this file does not establish a new visual system.

## Evidence on hand

- Existing Thymeleaf templates: home, explore, physical/digital book details, login, registration, dashboard, books, new book, users, and user details.
- Existing Java controller routes and shared stylesheet.
- No user research, usage analytics, or validated institutional requirements are present in the repository.
