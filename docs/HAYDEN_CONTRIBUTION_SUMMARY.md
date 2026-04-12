# Hayden Contribution Summary - SmartSpend

## Main contribution areas
- SQLite database design and integration
- Real product data import using Open Food Facts
- JavaFX UI templates for Dashboard, Shopping List, Settings, and Price Trends
- Project-ready artefacts for the Week 7 checkpoint

## What was implemented
### 1. Database work
- Added working SQLite connection and startup initialisation through `DatabaseManager`
- Added schema and seed support using `src/main/resources/db/schema.sql` and `seed.sql`
- Implemented `ItemDao` methods to insert and fetch items from the `items` table
- Confirmed that the schema supports SmartSpend's core features:
  - item storage
  - shopping list entries
  - prices by store
  - price history for later AI/trend features
  - user preferences

### 2. Real data integration
- Added Open Food Facts integration classes:
  - `ApiProduct`
  - `OpenFoodFactsService`
  - `ProductImportService`
- Users can search real product data and import selected products into the local SQLite database
- This supports the project theme "Living, Learning, Working with AI" by using AI-style recommendations built on real product information rather than hardcoded examples

### 3. UI templates / design contribution
- Created and updated JavaFX template screens:
  - `dashboard-view.fxml`
  - `list-view.fxml`
  - `settings-view.fxml`
  - `trends-view.fxml`
- These screens are layout templates that can be refined later without rebuilding the page structure from scratch
- The dashboard includes placeholders and working sections for:
  - quick add
  - budget box
  - basket total
  - frugal briefing
  - real product search/import
  - local SQLite items list

## How to demo this to the tutor
1. Open the project in IntelliJ with Corretto 21
2. Run using `mvn clean javafx:run`
3. Show the dashboard screen
4. Search a real product such as `milk`
5. Import a result into SQLite
6. Show the imported item appearing in the local list
7. Open the Shopping List, Settings, and Price Trends templates to show the UI design work
8. Open `schema.sql` and explain how the tables support SmartSpend

## Personal explanation to say in the checkpoint
I handled the SQLite database side and the initial UI template design work. I created the database integration for storing items and seeded data, and I also added a real API import flow using Open Food Facts so the project uses external product data rather than only mock data. On the UI side, I created editable JavaFX template screens for the dashboard, shopping list, settings, and trends pages so the team can refine the layouts later while keeping the project structure consistent.
