package btw.block.tileentity.dispenser;

import btw.item.BTWItems;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BlockDispenser;
import net.minecraft.src.DispenserBehaviorEmptyBucket;
import net.minecraft.src.DispenserBehaviorFilledBucket;
import net.minecraft.src.DispenserBehaviorPotion;
import net.minecraft.src.Item;

public class BTWDispenserBehaviorManager {
   public static void initDispenserBehaviors() {
      BlockDispenser.dispenseBehaviorRegistry.putObject(BTWItems.broadheadArrow, new BroadheadArrowDispenserBehavior());
      BlockDispenser.dispenseBehaviorRegistry.putObject(BTWItems.rottenArrow, new RottedArrowDispenserBehavior());
      BlockDispenser.dispenseBehaviorRegistry.putObject(BTWItems.soulUrn, new SoulUrnDispenserBehavior());
      BlockDispenser.dispenseBehaviorRegistry.putObject(BTWItems.dynamite, new DynamiteDispenserBehavior());
      BlockDispenser.dispenseBehaviorRegistry.putObject(BTWItems.netherSludge, new MortarApplicationDispenserBehavior());
      BlockDispenser.dispenseBehaviorRegistry.putObject(Item.clay, new MortarApplicationDispenserBehavior());
      BlockDispenser.dispenseBehaviorRegistry.putObject(Item.slimeBall, new MortarApplicationDispenserBehavior());
      if (MinecraftServer.getServer() != null) {
         BlockDispenser.dispenseBehaviorRegistry.putObject(Item.potion, new DispenserBehaviorPotion());
         DispenserBehaviorFilledBucket filledBucketBehavior = new DispenserBehaviorFilledBucket();
         BlockDispenser.dispenseBehaviorRegistry.putObject(Item.bucketLava, filledBucketBehavior);
         BlockDispenser.dispenseBehaviorRegistry.putObject(Item.bucketWater, filledBucketBehavior);
         BlockDispenser.dispenseBehaviorRegistry.putObject(Item.bucketEmpty, new DispenserBehaviorEmptyBucket());
      }
   }
}
