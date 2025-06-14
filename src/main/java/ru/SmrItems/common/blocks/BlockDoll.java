package ru.SmrItems.common.blocks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ru.SmrItems.common.tileentity.TileEntityDoll;
import ru.SmrItems.items.ItemBlockDoll;
import ru.SmrItems.util.regisrty.IHasItemBlock;
import ru.SmrItems.util.regisrty.IHasNamedTileEntity;

public final class BlockDoll extends BlockBaseContainer<TileEntityDoll> implements IHasNamedTileEntity<TileEntityDoll>, IHasItemBlock {
   public final int startTypeIndex;

   public BlockDoll(String name, int startTypeIndex) {
      super(Material.cloth, name);
      this.startTypeIndex = startTypeIndex;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.25F, 1.0F);
      this.setLightOpacity(0);
      this.setHardness(0.1F);
      this.setResistance(6.0F);
      this.setBlockTextureName("SmrItems:doll");
   }

   public int damageDropped(int meta) {
      return meta;
   }

   public int getRenderType() {
      return -1;
   }

   public Class<? extends ItemBlock> getItemBlockClass() {
      return ItemBlockDoll.class;
   }

   public Class<TileEntityDoll> getTileEntityClass() {
      return TileEntityDoll.class;
   }

   public String getTileEntityName() {
      return "Doll";
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
      int meta = this.startTypeIndex;

      for(int endTypeIndex = Math.min(this.startTypeIndex + 16, DollType.values().length); meta < endTypeIndex; ++meta) {
         list.add(new ItemStack(item, 1, meta - this.startTypeIndex));
      }

   }

   public TileEntity createNewTileEntity(World world, int meta) {
      return new TileEntityDoll();
   }

   public static enum DollType {
      MAID("Maid", (String)null, ""),
      ENDER_GIRL("Ender_Girl", (String)null, ""),
      CREEPER_GIRL("Creeper_Girl", (String)null, ""),
      AKAME("Akame", "TaintedMagic", "Akame ga Kill!", false),
      BELL_CRANEL("Bell_Cranel", "Thaumcraft", "Dungeon ni Deai wo Motomeru no wa Machigatteiru Darou ka", false),
      HESTIA("Hestia", "Thaumcraft", "Dungeon ni Deai wo Motomeru no wa Machigatteiru Darou ka", false),
      HITAGI_SENJOUGAHARA("Hitagi_Senjougahara", (String)null, "Bakemonogatari"),
      KURISU_MAKISE("Kurisu_Makise", "appliedenergistics2", "Steins;Gate", false),
      KYSYGAKI("Kysygaki", "this", "", false),
      MAIKA_SAKURANOMIYA("Maika_Sakuranomiya", "ThaumicTinkerer", "Blend S"),
      MATSURIKA_SHINOUJI("Matsurika_Shinouji", "EMT", "Maria†Holic"),
      RIN_SHIMA("Rin_Shima", (String)null, "Yuru Camp"),
      SHINO_AMAKUSA("Shino_Amakusa", "eplus", "Seitokai Yakuindomo"),
      YUKINO_YUKINOSHITA("Yukino_Yukinoshita", (String)null, "Yahari Ore no Seishun Love Comedy wa Machigatteiru."),
      AQUA("Aqua", "DraconicEvolution", "Kono Subarashii Sekai ni Shukufuku wo!"),
      LALATINA_DUSTINESS_FORD("Lalatina_Dustiness_Ford", (String)null, "Kono Subarashii Sekai ni Shukufuku wo!"),
      MEGUMIN("Megumin", "DraconicEvolution", "Kono Subarashii Sekai ni Shukufuku wo!"),
      REI_AYANAMI("Rei_Ayanami", (String)null, "Neon Genesis Evangelion"),
      RIN_TOHSAKA("Rin_Tohsaka", "this", "Fate/stay night", false),
      SHINOBU_OSHINO("Shinobu_Oshino", "witchery", "Bakemonogatari"),
      ALBEDO("Albedo", "this", "Overlord"),
      ASUKA_LANGLEY_SOURYUU("Asuka_Langley_Souryuu", (String)null, "Neon Genesis Evangelion"),
      MASHIRO_SHIINA("Mashiro_Shiina", (String)null, "Sakura-sou no Pet na Kanojo"),
      SORA("Sora", "this", "No Game No Life", true),
      TAIGA_AISAKA("Taiga_Aisaka", (String)null, "Toradora!"),
      YUMEKO_JABAMI("Yumeko_Jabami", (String)null, "Kakegurui"),
      AIKA_FUWA("Aika_Fuwa", (String)null, "Zetsuen no Tempest"),
      ANNTEKAMAKI("Ann_Takamaki", "this", "Persona 5"),
      AOBA("Aoba_Suzukaze", (String)null, "New Game!"),
      CHISA("Chisa_Kotegawa", (String)null, "Grand Blue"),
      REMGALLEU("Rem_Galleu", "Forestry", "Isekai Maou to Shoukan Shoujo no Dorei Majutsu", false),
      SHERAL("Shera_L_Greenwood", "this", "Isekai Maou to Shoukan Shoujo no Dorei Majutsu"),
      TANYADEG("Tanya_Degurechaff", "TwilightForest", "Youjo Senki"),
      ZERO("Zero", "witchery", "Zero kara Hajimeru Mahou no Sho"),
      MAKINAMI("Mari_Illustrious_Makinami", (String)null, "Neon Genesis Evangelion"),
      SHINJI("Shinji_Ikari", (String)null, "Neon Genesis Evangelion"),
      MARY_SAOTOME("Mary_Saotome", (String)null, "Kakegurui"),
      KOKO("Koko_Hekmatyar", "this", "Jormungand", false),
      MISATO("Misato_Katsuragi", (String)null, "Neon Genesis Evangelion"),
      AKATSUKI("Akatsuki", (String)null, "Log Horizon"),
      OKABE("Rintarou_Okabe", "appliedenergistics2", "Steins;Gate", false),
      KIRUKIRU_AMOU("Kirukiru_Amou", (String)null, "Busou Shoujo Machiavellianism"),
      RIMURU_TEMPEST("Rimuru_Tempest", (String)null, "Tensei shitara Slime Datta Ken"),
      SUZUNE("Suzune_Horikita", (String)null, "Youkoso Jitsuryoku Shijou Shugi no Kyoushitsu e"),
      MISHA_TAKANASHI("Misha_Takanashi", (String)null, "Uchi no Maid ga Uzasugiru!"),
      TOUKA_KIRISHIMA("Touka_Kirishima", (String)null, "Tokyo Ghoul"),
      YUKINA_HIMERAGI("Yukina_Himeragi", (String)null, "Strike the Blood"),
      RISTARTE("Ristarte", "this", "Shinchou Yuusha: ..."),
      ALICE_KISARAGI("Alice_Kisaragi", "Botania", "Sentouin, Hakenshimasu!", false),
      HAYASE_NAGATORO("Hayase_Nagatoro", (String)null, "Ijiranaide, Nagatoro-san"),
      SISTINE_FIBEL("Sistine_Fibel", "Botania", "Rokudenashi Majutsu Koushi to Akashic Records", false),
      WIZ("Wiz", "this", "Kono Subarashii Sekai ni Shukufuku wo!", false),
      IRINA_LUMINESK("Irina_Luminesk", "GalacticraftCore", "Tsuki to Laika to Nosferatu", false),
      CHRIS("Chris", (String)null, "Kono Subarashii Sekai ni Shukufuku wo!"),
      MELIDA_ANGEL("Melida_Angel", (String)null, "Assassins Pride"),
      KILLUA_ZOLDYCK("Killua_Zoldyck", "this", "Hunter x Hunter", false),
      MAHA_TUATHA_DE("Maha_Tuatha_De", (String)null, "Sekai Saikou no Ansatsusha, Isekai Kizoku ni Tensei suru"),
      KONATA_IZUMI("Konata_Izumi", (String)null, "Lucky☆Star"),
      TSUBAME_MIZUSAKI("Tsubame_Mizusaki", (String)null, "Eizouken ni wa Te wo Dasu na!"),
      LOLISA("Lolisa", "this", "Kono Subarashii Sekai ni Shukufuku wo!"),
      SENA("Sena", (String)null, "Kono Subarashii Sekai ni Shukufuku wo!"),
      MARIN_KITAGAWA("Marin_Kitagawa", (String)null, "Sono Bisque Doll wa Koi wo Suru"),
      ALTAIR("Altair", (String)null, "Re:Creators"),
      KURO("Kuro", (String)null, "Isekai Shokudou"),
      ADMIN("Administrator", (String)null, ""),
      RYUUKO_MATOI("Ryuuko_Matoi", (String)null, "Kill la Kill"),
      SOUTA_MIZUSHINO("Souta_Mizushino", (String)null, "Re:Creators");

      private final ResourceLocation textureLocation;
      private final String loreText;
      private final String whatAnime;
      private final String itemName;
      private final boolean isGui;

      private DollType(String textureLocation, String lore, String whatAnime) {
         this(textureLocation, lore, whatAnime, false);
      }

      private DollType(String textureLocation, String lore, String whatAnime, boolean gui) {
         this.itemName = this.name().toLowerCase();
         this.textureLocation = new ResourceLocation("smritems", "textures/models/dolls/" + textureLocation + ".png");
         this.loreText = lore;
         this.whatAnime = whatAnime;
         this.isGui = gui;
      }

      public ResourceLocation getTextureLocation() {
         return this.textureLocation;
      }

      public boolean getLoreOnItemDoll() {
         return this.loreText != null && (this.loreText.equals("this") || Loader.isModLoaded(this.loreText));
      }

      public String getAnimeOnItemDoll() {
         return this.whatAnime;
      }

      public String getItemName() {
         return this.itemName;
      }

      public boolean isGui() {
         return this.isGui && this.getLoreOnItemDoll();
      }

      public static DollType getTypeFromStack(ItemStack stack) {
         DollType[] values = values();
         if (stack != null) {
            Item item = stack.getItem();
            if (item instanceof ItemBlock && ((ItemBlock)item).field_150939_a instanceof BlockDoll) {
               int index = ((BlockDoll)((ItemBlock)item).field_150939_a).startTypeIndex + stack.getItemDamage();
               return getTypeFromIndex(index);
            }
         }

         return values[0];
      }

      public static DollType getTypeFromIndex(int index) {
         DollType[] values = values();
         return values[MathHelper.clamp_int(index, 0, values.length - 1)];
      }
   }
}
