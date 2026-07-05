package net.melatowoin.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Bare-bones criterion trigger: fires on a given player with no extra conditions,
 * granting any advancement that lists this trigger.
 */
public class SimplePlayerTrigger extends SimpleCriterionTrigger<SimplePlayerTrigger.Instance> {

    private final ResourceLocation id;

    public SimplePlayerTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    protected Instance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext ctx) {
        return new Instance(this.id, predicate);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ResourceLocation id, ContextAwarePredicate predicate) {
            super(id, predicate);
        }
    }
}
