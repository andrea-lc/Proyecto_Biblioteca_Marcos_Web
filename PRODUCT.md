# Product

<!-- impeccable:product-schema 1 -->

## Platform

web

## Users

- Readers use the catalog to discover, search for, and review physical and digital books.
- Library staff and administrators manage the catalog and member records and need a foundation for circulation and reporting.
- Readers and library staff are equally important audiences; public discovery and administrative work should remain easy to distinguish.

## Product Purpose

Bibliot. is a Spanish-language web application for discovering books and managing a library. It aims to help readers find books and give library staff a clear place to manage catalog and membership information.

## Operating Context

- People use the product in a web browser. Mobile use is expected as well as desktop use.
- Reader-facing catalog pages and staff-facing administrative pages are separate workflows.
- The project is currently primarily a prototype. Existing APIs and some connected CRUD flows do not mean the full library service is production-ready.

## Capabilities and Constraints

- Readers can browse featured books, search and filter the catalog, and view separate physical and digital book details.
- The application has REST CRUD endpoints for books, users, roles, authors, categories, publishers, copies, loans, reservations, returns, and sanctions. Book and user management and role management have connected views; other workflows remain partial, static, or without dedicated views.
- Login and registration are not connected to authentication. Administrative and personal data routes are not protected by role-based authorization.
- Physical and digital book details are distinct flows. Cover image upload and secure digital file delivery are not implemented.
- Reservation, loan, return, and sanction policies have not been defined by the institution. Do not invent these rules or imply that disabled actions are available.
- The project uses Spring Boot, Thymeleaf, Java 21 as the declared target, REST controllers, and MySQL.
- Preserve the Spanish language of the interface and keep existing page structure and styles when connecting or refining current views.

## Brand Commitments

- Product name: Bibliot. (also written as Bibliot in page titles).
- Product-facing interface copy is in Spanish.

## Evidence on Hand

- Thymeleaf templates in `src/main/resources/templates` cover catalog discovery, book details, login and registration, dashboard, and administrative book and user pages.
- REST controllers and entity-specific DTOs are in `src/main/java/com/bibliot/gestion_biblioteca`.
- The shared stylesheet is `src/main/resources/static/css/bibliot.css`.
- No user research, usage analytics, institutional lending policies, or validated reporting requirements are present in the repository.

## Product Principles

- Serve readers and library staff with equal priority while keeping their workflows distinct.
- Treat the current application as a prototype, and represent connected and unconnected capabilities accurately.
- Do not claim that a record or action succeeded unless the backend confirms it.
- Keep lending and access rules open until the institution defines them.
