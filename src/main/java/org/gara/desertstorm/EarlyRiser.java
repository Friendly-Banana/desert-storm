package org.gara.desertstorm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.util.Formatting;

public class EarlyRiser implements Runnable {
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();

        String formattingClass = remapper.mapClassName("intermediary", "net.minecraft.class_124");
        String bossbarColorClass = remapper.mapClassName("intermediary", "net.minecraft.class_1259$class_1260");
        ClassTinkerers.enumBuilder(bossbarColorClass, String.class, "L" + formattingClass + ";").addEnum("GOLD", () -> new Object[]{"gold", Formatting.GOLD}).build();
    }
}
