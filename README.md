# Hungry Pet Hider

Hides your follower while it is fed, and shows it again when it gets hungry.

By default it only hides followers whose name contains "puppy" (for example the
Labrador puppy), so a fully grown dog or any other pet always stays visible.

## How it works

- When a game message matches the "Fed message" setting, your follower is hidden.
- When "Your puppy is very hungry." appears, your follower is shown again and you
  get an optional notification.
- After logging in or hopping, your follower is always visible until it is fed.

## Settings

- **Only hide followers named**: name filter, default `puppy`. Blank hides any follower.
- **Hungry message (regex)**: default `Your puppy is very hungry`.
- **Fed message (regex)**: words in the message shown when you feed your pet.
- **Item used on pet = fed**: treat using any item on your follower as feeding it.
- **Notify when hungry**: RuneLite notification when your pet gets hungry.
- **Debug chat messages**: prints a chat line whenever the state changes.
