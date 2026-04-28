# SmartSpend Merge Notes

This consolidated project shell was built by using the Hayden contribution branch as the stable base, then folding in safe, presentation-ready ideas from the other uploaded branches.

## Included in this merge
- Hayden backend/database work
- Open Food Facts product search/import flow
- Consistent high-fidelity JavaFX UI shell
- Separate screens for Login, Dashboard, Shopping List, Price Comparison, Budget, AI Assistant, Prices, Pantry, and Settings
- Shared styling across all screens for a cleaner checkpoint/demo build

## Intentionally not merged directly
Some uploaded branches contained partial or conflicting experimental classes. Those were not merged blindly where they would likely break compilation or create duplicate/conflicting models.

Examples include:
- duplicate DAO scaffolds
- experimental AI/model classes without full integration
- alternate UI branches with overlapping page names and inconsistent styling

## Why this approach was used
The goal of this merged version is to give the team a single project that:
1. looks polished and consistent
2. demonstrates the team's main features and theme clearly
3. can be extended safely after the checkpoint
