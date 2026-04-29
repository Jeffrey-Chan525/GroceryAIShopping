PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS price_history;
DROP TABLE IF EXISTS prices;
DROP TABLE IF EXISTS shopping_list_items;
DROP TABLE IF EXISTS user_preferences;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    email TEXT UNIQUE,
    hashedPassword TEXT,
    salt TEXT
);

CREATE TABLE items (
    item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    brand TEXT,
    default_unit TEXT NOT NULL
);

CREATE TABLE shopping_list_items (
    list_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    quantity REAL NOT NULL DEFAULT 1,
    unit TEXT NOT NULL,
    is_completed INTEGER NOT NULL DEFAULT 0 CHECK (is_completed IN (0,1)),
    added_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
);

CREATE TABLE prices (
    price_id INTEGER PRIMARY KEY AUTOINCREMENT,
    item_id INTEGER NOT NULL,
    store_name TEXT NOT NULL,
    price REAL NOT NULL CHECK (price >= 0),
    package_quantity REAL NOT NULL CHECK (package_quantity > 0),
    package_unit TEXT NOT NULL,
    last_updated TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_on_sale INTEGER NOT NULL DEFAULT 0 CHECK (is_on_sale IN (0,1)),
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
);

CREATE TABLE price_history (
    history_id INTEGER PRIMARY KEY AUTOINCREMENT,
    item_id INTEGER NOT NULL,
    store_name TEXT NOT NULL,
    price REAL NOT NULL CHECK (price >= 0),
    recorded_date TEXT NOT NULL,
    is_on_sale INTEGER NOT NULL DEFAULT 0 CHECK (is_on_sale IN (0,1)),
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
);

CREATE TABLE user_preferences (
    preference_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL UNIQUE,
    weekly_budget REAL NOT NULL DEFAULT 100 CHECK (weekly_budget >= 0),
    primary_store TEXT,
    show_sale_predictions INTEGER NOT NULL DEFAULT 1 CHECK (show_sale_predictions IN (0,1)),
    show_value_suggestions INTEGER NOT NULL DEFAULT 1 CHECK (show_value_suggestions IN (0,1)),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
