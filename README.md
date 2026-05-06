# SmartSpend

SmartSpend is a JavaFX grocery budgeting application for university students. It helps users manage a shopping list, compare mock prices across stores, track spending against a weekly budget, and receive smart recommendations about the cheapest basket and better-value purchase options.

## Requirements
- compare against identical items against different grocery chains
- Get the location between the user and the grocery store 
- See the past price info for any product
- Accurate, live data for current and historic price info
- AI agent feature for a more personalized experience

## Tech Stack
- Java 21 (Amazon Corretto recommended)
- JavaFX
- SQLite
- Maven
- JUnit 5
- IntelliJ IDEA

## Core Features
- Shopping list CRUD
- SQLite persistence
- Price comparison across stores
- Weekly budget tracking
- Frugal recommendation logic

## OOP Structure
- `model` - domain objects
- `dao` - database access layer
- `service` - business logic
- `controller` - JavaFX controllers
- `util` - helpers and shared utilities

## How to Run in IntelliJ
1. Open the project folder in IntelliJ.
2. Set the Project SDK to **Amazon Corretto 21**.
3. Let Maven import the project.
4. Run `com.smartspend.MainApp`.

## Database Files
- `database/schema.sql`
- `database/seed.sql`
- `src/main/resources/db/schema.sql`
- `src/main/resources/db/seed.sql`

You can create the SQLite database in DB Browser using the schema and seed files.

## Documentation

A lot of info is further documented in the docs folder

the following are some topics expanded upon in the documentation 

-  UML diagram
- user stories
- wireframes for UI
- setup process for Intellij
