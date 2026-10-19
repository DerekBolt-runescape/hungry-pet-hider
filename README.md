# Hungry Pet Hider

Hides your follower while it is fed, and shows it again when it gets hungry.

By default it only hides followers whose name contains "Labrador puppy", so a
fully grown dog or any other pet always stays visible.

## How it works

- When you use an item on your follower (or a game message matches the optional
  "Fed message" setting), your follower is hidden.
- When "Your puppy is very hungry." appears, your follower is shown again and you
  get an optional notification.
- After logging in or hopping, your follower is always visible until it is fed.

## Settings

- **Only hide followers named**: name filter, default `Labrador puppy`. Blank hides any follower.
- **Hungry message (regex)**: default `Your puppy is very hungry.`
- **Fed message (regex)**: optional words in the message shown when you feed your pet. Blank by default.
- **Item used on pet = fed**: treat using any item on your follower as feeding it. On by default.
- **Notify when hungry**: RuneLite notification when your pet gets hungry.
- **Debug chat messages**: prints a chat line whenever the state changes.
