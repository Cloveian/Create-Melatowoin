# Create: MelatOwOin — Wiki

The wiki is structured as a **GitHub wiki repository** — one page per file in [wiki/](wiki/), with `[[Page-Name]]` cross-links, an `_Sidebar.md` for navigation, and a `_Footer.md`.

**Start here → [wiki/Home.md](wiki/Home.md)**

## Pages

### Gameplay
- [Getting Started](wiki/Getting-Started.md)
- [Wearable Cat Pieces](wiki/Wearable-Cat-Pieces.md)
- [Dyeing System](wiki/Dyeing-System.md)
- [Pills and Throwables](wiki/Pills-and-Throwables.md)
- [Status Effects](wiki/Status-Effects.md)

### Reference
- [Items](wiki/Items.md)
- [Fluids and Buckets](wiki/Fluids-and-Buckets.md)
- [Recipes — Crafting Table](wiki/Recipes-Crafting-Table.md)
- [Recipes — Create Machines](wiki/Recipes-Create-Machines.md)
- [Chloroform Chain](wiki/Chloroform-Chain.md)
- [Furrycore Chain](wiki/Furrycore-Chain.md)
- [Tags and Game Rules](wiki/Tags-and-Game-Rules.md)

### Compatibility
- [Accessories Integration](wiki/Accessories-Integration.md)

### Contributors
- [Architecture](wiki/Architecture.md)

## Publishing to GitHub Wiki

To publish this as the actual repo wiki on GitHub:

```bash
# After creating an empty wiki on github.com (just initialise it with a stub page first)
git clone https://github.com/<user>/<repo>.wiki.git
cp wiki/*.md <repo>.wiki/
cd <repo>.wiki
git add . && git commit -m "Publish wiki" && git push
```

The page filenames already use `Dashed-Names.md` and link via `[[Page-Name]]`, both of which GitHub Wiki accepts natively.
