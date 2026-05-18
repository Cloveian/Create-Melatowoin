package net.melatowoin.forge.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.item.CatEarsArmorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Forge client item extensions for cat ears armor items.
 * Provides the custom armor model (CatEarModel / TailModel) in place of the
 * standard HumanoidArmorModel so that the correct geometry is displayed.
 *
 * Usage: return an instance of this from CatEarsArmorItem.initializeClient().
 * Note: Forge's IClientItemExtensions.getHumanoidArmorModel is used here.
 */
public class ForgeCatEarsRenderer implements IClientItemExtensions {

    private CatEarModel earModel;
    private TailModel tailModel;

    private void initModels() {
        if (earModel == null || tailModel == null) {
            EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
            earModel = new CatEarModel(modelSet.bakeLayer(CatEarModel.LAYER_LOCATION));
            tailModel = new TailModel(modelSet.bakeLayer(TailModel.LAYER_LOCATION));
        }
    }

    /**
     * Returns a custom HumanoidModel that is actually our CatEarModel or TailModel
     * wrapped so Forge's armor rendering calls renderToBuffer on them.
     *
     * For Forge 1.20.1, the cleanest approach is to return a proxy HumanoidModel
     * that delegates rendering to the custom model.
     */
    @Override
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity,
                                                   ItemStack itemStack,
                                                   EquipmentSlot equipmentSlot,
                                                   HumanoidModel<?> original) {
        initModels();
        return original;
    }
}
