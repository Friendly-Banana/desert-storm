a = {"item.minecraft.tipped_arrow.effect.":"x Arrow",
"item.minecraft.potion.effect.":"x",
"item.minecraft.splash_potion.effect.":"x Splash",
"item.minecraft.lingering_potion.effect.":"Lingering x"}

ef = {"multivitamin": ("Multivitamin", "Feel the power of Vitamins!"),
"healthy_smoothie": ("Healthy Smoothie", "Boosts your health"),
"hot_cocoa": ("Hot Cocoa", "Warms you up."),
"sunrise": ("Sunrise", "Uh, too bright"),
"sunset": ("Sunset", "Relieve yourself and relax"),
"molotov": ("Molotov Cocktail", "Fiery Vodka"),
"midas_special": ("Midas Special", "The dream of every alchemist come true"),
"iced": ("Iced Cocktail", "Makes you feel cold. Winter is coming! (for you, personally)"),
"dislocator": ("Dislocator", "Teleports things and persons around"),
"radioactive": ("Radioactive Cocktail", "Random effect you don't already have. Get them all!")}

for e in ef.keys():
    for b in a.keys():
        print('"' + b + e + '": "'+ a[b].replace('x', ef[e][0]) + '",')
