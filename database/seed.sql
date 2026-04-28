INSERT INTO users (username, email) VALUES
('haiden', 'haiden@example.com');

INSERT INTO items (name, category, brand, default_unit) VALUES
('Milk', 'Dairy', 'Coles Brand', 'L'),
('Bread', 'Bakery', 'Wonder White', 'loaf'),
('Eggs', 'Dairy', 'Farm Fresh', 'dozen'),
('Rice', 'Pantry', 'SunRice', 'kg'),
('Chicken Breast', 'Meat', 'Woolworths', 'kg');

INSERT INTO shopping_list_items (user_id, item_id, quantity, unit, is_completed) VALUES
(1, 1, 2, 'L', 0),
(1, 2, 1, 'loaf', 0),
(1, 3, 1, 'dozen', 0);

INSERT INTO prices (item_id, store_name, price, package_quantity, package_unit, is_on_sale) VALUES
(1, 'Coles', 3.20, 2, 'L', 0),
(1, 'Woolworths', 3.00, 2, 'L', 0),
(1, 'Aldi', 2.85, 2, 'L', 0),
(2, 'Coles', 2.90, 1, 'loaf', 0),
(2, 'Woolworths', 2.70, 1, 'loaf', 0),
(2, 'Aldi', 2.50, 1, 'loaf', 0),
(3, 'Coles', 5.40, 1, 'dozen', 0),
(3, 'Woolworths', 5.20, 1, 'dozen', 0),
(3, 'Aldi', 4.90, 1, 'dozen', 0);

INSERT INTO price_history (item_id, store_name, price, recorded_date, is_on_sale) VALUES
(1, 'Coles', 3.50, '2026-03-01', 0),
(1, 'Coles', 3.20, '2026-03-08', 0),
(1, 'Coles', 2.50, '2026-03-15', 1),
(1, 'Woolworths', 3.30, '2026-03-01', 0),
(1, 'Woolworths', 3.10, '2026-03-08', 0),
(1, 'Woolworths', 3.00, '2026-03-15', 0),
(1, 'Aldi', 2.95, '2026-03-01', 0),
(1, 'Aldi', 2.90, '2026-03-08', 0),
(1, 'Aldi', 2.85, '2026-03-15', 0);

INSERT INTO user_preferences (user_id, weekly_budget, primary_store, show_sale_predictions, show_value_suggestions) VALUES
(1, 100.00, 'Aldi', 1, 1);
