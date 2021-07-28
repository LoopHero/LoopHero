## Combat
* The damage point directly affects the health point, meaning 1 damage point will decrease 1 health point of the object which receives the attack.
* The damage and support radiuses are calculated by the number of cell surrounding by the object.
* When killing an enemy, the Character will have the following chance to receive different things:
    * 10 gold: 20% 
    * 5 experience: 100% 
    * Health potion: 10% 
    * Equipment except The One Ring, Health potion: 50% (Each type distributes to the same dropping rate)
    * Card: 40% (Each type distributes to the same dropping rate)
    * The One Ring: 0.2%

## Character
* Default Battle radius: 1  
   * Can be increased by equipping range weapons
* Default attack speed: one attack per second
* Default Damage per second: 5
    * Can be increased by equipping weapons
* Default Critical Damage Rate in percentage: 0
    * Can be increased by equipping specific weapons:  
        New_damage = Current_damage(1 + Critical Damage Rate %)
* Default health point is 1000.
    * Can be recovered by consuming Potion (or The One Ring).
* Default starting experience is 0.
    * Can be increased by attacking enemies and card conversion.
* Default starting gold is 0.
    * Can be increased by selling items at shop Potion, attacking enemies and card conversion.
* The damage to enemies will be doubled with Campfire buff.

## Cards
* Number of cards available is 8.
* The oldness of the cards is sorted from left to right. The right card will always be newer than the left one.
* Will be replaced by the new card, the old card will be randomly converted to gold, experience.
    * The gold is set to 20 and experience is set to 50 by default.


## Enemies
* Slug
    * Health point: 10
    * Damage per attack: 5
    * Battle radius: 1
    * Support radius: 1
    * Default attack speed: one attack per second
    * Has a chance of 30% to appear in the path per 2 seconds
* Zombie
    * Health point: 15
    * Damage per attack: 10
    * Battle radius: 2
    * Support radius: 1
    * Default attack speed: one attack per two seconds
    * Have 15% chance of transforming an allied soldier to a zombie per attack.
    * Only appear when there is a zombie pit
* Vampire
    * Health point: 30
    * Damage per attack: 15
    * Battle radius: 3
    * Support radius: 5
    * Can't have a campfire in the battle radius
    * Default attack speed: one attack per second
    * Have 20% chance of critical damage per attack: 20
    * Only appear when there is a vampire castle

## Buildings
* Buildings can't be placed in the same position.
* Buildings can't be placed in the path tile.
* Vampire castle
    * Produces 5 vampires every 5 cycles of the path completed by the Character, spawning nearby on the path
* Zombie pit
    * Produces 5 zombies every cycle of the path completed by the Character, spawning nearby on the path
* Tower
    * Shooting radius: 3
    * Damage per attack: 2
    * Default attack speed: one attack per second
* Village
    * Character regains 5 health points when passing through
* Barracks
    * Produces one allied soldier to join Character when passes through
    * Each allied soldier has the following stats:
        * Default Battle radius: 1 
        * Default Support radius: 1 
        * Default attack speed: one attack per second
        * Default Damage per second: 2
* Trap
    * When an enemy steps on a trap, the enemy receives 5 damage points.
* Campfire
    * Helping radius: 3
    
## Basic Items
* In the Milestone 2, only three melee weapons are provided:
    * Sword
        * Increase Character's damage per second to 5
        * Price: randomly generated
    * Stake:  
        * Increase Character's damage per second to 2 to the enemies excluding vampires 
        * When battling with vampires, Character's damage per attack will be increased by 10
        * Price: randomly generated
    * Staff
        * Increase Character's damage per second to 1
        * Have the 20% chance of inflicting a trance, which transforms the attacked enemy in the current battle into an allied soldier temporarily
        * The transformation is instant.
        * The transformed allied soldier will have the same stats of its previous type. 
        * The transformed allied soldier will last for 5 its own attacks to the enemies.
        * Price: randomly generated
    * Fist
        * Character's damage per second is 1 when no weapon is equipped.
* In the Milestone 2, only three defending items are provided:
    * Armour
        * Half enemy attack
        * Price: randomly generated
    * Shield
        * Have 30% chance to block an attack from enemies.
        * Critical vampire attacks have a 60% lower chance of occurring
        * Price: randomly generated
    * Helmet
        * Decrease enemy attack by 5
        * Have 15% decrease of Character's damage per attack. (0.95 * current_damage_per_attack)
        * Price: randomly generated
* In the Milestone 2, only a basic health potion are provided:
    * Health potion
        * Increased Character's health point to full
        * Price: randomly generated

* Battle radius can be increased by equipping range weapons

## Rare Items
* The One Ring
    * If the Character is killed, it respawns with full health up to a single time.
    * The item is consumable.
    * The item will be automatically consumed.
    * Price: randomly generated

## Unequipped Item Inventory
* Sword, Stake, Staff, Armour, Shield, Helmet, Potion and The One Ring
* Potion can be used by pressing H key.
* The One Ring will be auto-used when character dies.
* When too many items is received, the oldest piece of equipment is lost, and the Character receives additional gold and experience
    * The gold is set to 20 and experience is set to 50 by default.

## Equipped Item Inventory
* Sword, Stake, Staff, Armour, Shield, Helmet
* Only Sword, Stake, Staff, Armour, Shield, Helmet can be equipped.
* Different types of items must be equipped to corresponding item slot.
* Once a item is equipped, it can be dropped by replacing it with a new item.
* Once a item is equipped, it can be dropped by dragging the item to unequipped item inventory.

## Shopping
* Every 1, 2, 3, 4, 5, ... cycles, the shop menu will pop out at the Hero Castle.
* The price of buying is higher than selling items.
* The buying and selling price are radomly genrated.
* If character can't afford a item, the buying button of that item will be unclickable.
* If character doesn't have a item, the selling button of that item will be unclickable.

## Gameover
* If the character dies and no One Ring could be used, lose window will pop out and guide to Main Menu.
* If the character completes goals, win window will pop out and guide to Main Menu.

## Pop Window Operations
* For pop windows, the user should click the button instead of directly closing it.
