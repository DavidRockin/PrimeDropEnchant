# Configuration File

The configuration file is pretty simple and straightforward. It is self-documenting.

An reference to material types, are the **EXACT** Bukkit API material types: \
https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html

The `enchantmentContainer` section defined the result item the player will
receive when they drop enchantments. It accepts a custom name and lore.

The `enchantWhitelist` list defines which items can be enchanted via the
use of enchanted books.

The `transferWhitelist` list defines all items players can drop enchantments
from, into a book.

The `enchantments` section defines ALL available enchantments.

The `id` field refers to Bukkit's internal enchantment ID
If `allow` is set to `true` then players can enchant or remove this enchantment.
If it's false, players will not be able to touch it.

```yaml
- id: <enchantment id>
  allow: true
  cost:
    amount: <integer or decimal>
    perLevel: true
    currency: <money|experience>
```

### Placeholder Tags

Placeholder tags are available in item lores, names, and in locale files for
applicable sections.

`%player%` the player's username

`%cost%` the net cost of a transaction, OR the cost of an enchantment

`%name%` an enchantment's name

`%level%` an enchantment's level


## Default Configuration

The configuration file has been shortened to reduce space. View the original
config file for the rest of the contents.

```yaml
# PrimeDropEnchant
#
# Developed by PrimeUX, (c) 2019
# For assistance please visit our website: https://primeux.net
#
# For item types: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html



# the language to use
locale: en


# the item to contain all transferred enchantments
enchantmentContainer:
  type: ENCHANTED_BOOK
  name: '&b%player%''s book of Enchanting'
  lore:
  - '&bTransferred enchantments!'


# defines which items can be enchanted via books
enchantWhitelist:
- DIAMOND_AXE
- DIAMOND_BOOTS
- DIAMOND_CHESTPLATE
- DIAMOND_HELMET
- DIAMOND_HOE
....

# defines which items can have enchanments transferred from
transferWhitelist:
- DIAMOND_AXE
- DIAMOND_BOOTS
- DIAMOND_CHESTPLATE
- DIAMOND_HELMET
- DIAMOND_HOE
.....


# All available enchantments
#
# You can configure which enchantments players can drop or enchant.
# For the currency, you can use either: money or experience
# If money, the `cost.amount` can be a decimal, and will charge players'
# economy balance (requires Vault). If its `experience` then it will take the
# player's XP.
#
# The `id` field refers to the internal Bukkit enchantment API. Seek the resource below:
#     https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html
#
# Note: For modded servers (ie: Tekkit), you can use custom enchantments as long as they extend
# the vanilla enchantment system. Contact your mod pack's developer for help.
enchantments:
- id: ARROW_DAMAGE
  allow: true
  cost:
    amount: 10
    perLevel: true
    currency: money
- id: ARROW_FIRE
  allow: true
  cost:
    amount: 10
    perLevel: true
    currency: experience
- id: VANISHING_CURSE
	allow: false

....


```