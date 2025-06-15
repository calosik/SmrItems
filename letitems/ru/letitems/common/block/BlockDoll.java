package ru.letitems.common.block;

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
import ru.letitems.common.items.blocks.ItemBlockDoll;
import ru.letitems.common.tile.TileEntityDoll;
import ru.letitems.common.util.registry.IHasItemBlock;
import ru.letitems.common.util.registry.IHasNamedTileEntity;

public final class BlockDoll extends BlockBaseContainer<TileEntityDoll> implements IHasNamedTileEntity<TileEntityDoll>, IHasItemBlock {
   public final int startTypeIndex;

   public BlockDoll(String name, int startTypeIndex) {
      super(Material.cloth, name);
      this.startTypeIndex = startTypeIndex;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.25F, 1.0F);
      this.setLightOpacity(0);
      this.setHardness(0.1F);
      this.setResistance(6.0F);
      this.setBlockTextureName("letitems:doll");
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

   public TileEntity createNewTileEntity(World world, int meta) {
      return new TileEntityDoll();
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

      for(int endTypeIndex = Math.min(this.startTypeIndex + 16, BlockDoll.DollType.values().length); meta < endTypeIndex; ++meta) {
         list.add(new ItemStack(item, 1, meta - this.startTypeIndex));
      }

   }

   public static enum DollType {
      MAID(new ResourceLocation("letitems", "models/blocks/Doll_Maid.png"), (String)null, ""),
      ENDER_GIRL(new ResourceLocation("letitems", "models/blocks/Doll_Ender_Girl.png"), (String)null, ""),
      CREEPER_GIRL(new ResourceLocation("letitems", "models/blocks/Doll_Creeper_Girl.png"), (String)null, ""),
      AKAME(new ResourceLocation("letitems", "models/blocks/Akame.png"), "TaintedMagic", "Akame ga Kill!"),
      BELL_CRANEL(new ResourceLocation("letitems", "models/blocks/Bell_Cranel.png"), "Thaumcraft", "Dungeon ni Deai wo Motomeru no wa Machigatteiru Darou ka", true),
      HESTIA(new ResourceLocation("letitems", "models/blocks/Hestia.png"), "Thaumcraft", "Dungeon ni Deai wo Motomeru no wa Machigatteiru Darou ka", true),
      HITAGI_SENJOUGAHARA(new ResourceLocation("letitems", "models/blocks/Hitagi_Senjougahara.png"), (String)null, "Bakemonogatari"),
      KURISU_MAKISE(new ResourceLocation("letitems", "models/blocks/Kurisu_Makise.png"), "appliedenergistics2", "Steins;Gate", true),
      KYSYGAKI(new ResourceLocation("letitems", "models/blocks/Kysygaki.png"), "this", "", true),
      MAIKA_SAKURANOMIYA(new ResourceLocation("letitems", "models/blocks/Maika_Sakuranomiya.png"), "ThaumicTinkerer", "Blend S"),
      MATSURIKA_SHINOUJI(new ResourceLocation("letitems", "models/blocks/Matsurika_Shinouji.png"), (String)null, "Maria†Holic"),
      RIN_SHIMA(new ResourceLocation("letitems", "models/blocks/Rin_Shima.png"), (String)null, "Yuru Camp"),
      SHINO_AMAKUSA(new ResourceLocation("letitems", "models/blocks/Shino_Amakusa.png"), (String)null, "Seitokai Yakuindomo"),
      YUKINO_YUKINOSHITA(new ResourceLocation("letitems", "models/blocks/Yukino_Yukinoshita.png"), (String)null, "Yahari Ore no Seishun Love Comedy wa Machigatteiru."),
      AQUA(new ResourceLocation("letitems", "models/blocks/Aqua.png"), "DraconicEvolution", "Kono Subarashii Sekai ni Shukufuku wo!"),
      LALATINA_DUSTINESS_FORD(new ResourceLocation("letitems", "models/blocks/Lalatina_Dustiness_Ford.png"), (String)null, "Kono Subarashii Sekai ni Shukufuku wo!"),
      MEGUMIN(new ResourceLocation("letitems", "models/blocks/Megumin.png"), "DraconicEvolution", "Kono Subarashii Sekai ni Shukufuku wo!"),
      REI_AYANAMI(new ResourceLocation("letitems", "models/blocks/Rei_Ayanami.png"), (String)null, "Neon Genesis Evangelion"),
      RIN_TOHSAKA(new ResourceLocation("letitems", "models/blocks/Rin_Tohsaka.png"), "this", "Fate/stay night", true),
      SHINOBU_OSHINO(new ResourceLocation("letitems", "models/blocks/Shinobu_Oshino.png"), "witchery", "Bakemonogatari"),
      ALBEDO(new ResourceLocation("letitems", "models/blocks/Albedo.png"), "this", "Overlord"),
      ASUKA_LANGLEY_SOURYUU(new ResourceLocation("letitems", "models/blocks/Asuka_Langley_Souryuu.png"), (String)null, "Neon Genesis Evangelion"),
      MASHIRO_SHIINA(new ResourceLocation("letitems", "models/blocks/Mashiro_Shiina.png"), (String)null, "Sakura-sou no Pet na Kanojo"),
      SORA(new ResourceLocation("letitems", "models/blocks/Sora.png"), "this", "No Game No Life", true),
      TAIGA_AISAKA(new ResourceLocation("letitems", "models/blocks/Taiga_Aisaka.png"), (String)null, "Toradora!"),
      YUMEKO_JABAMI(new ResourceLocation("letitems", "models/blocks/Yumeko_Jabami.png"), (String)null, "Kakegurui"),
      AIKA_FUWA(new ResourceLocation("letitems", "models/blocks/Aika_Fuwa.png"), (String)null, "Zetsuen no Tempest"),
      ANNTEKAMAKI(new ResourceLocation("letitems", "models/blocks/Ann_Takamaki.png"), "this", "Persona 5"),
      AOBA(new ResourceLocation("letitems", "models/blocks/Aoba_Suzukaze.png"), (String)null, "New Game!"),
      CHISA(new ResourceLocation("letitems", "models/blocks/Chisa_Kotegawa.png"), (String)null, "Grand Blue"),
      REMGALLEU(new ResourceLocation("letitems", "models/blocks/Rem_Galleu.png"), "Forestry", "Isekai Maou to Shoukan Shoujo no Dorei Majutsu"),
      SHERAL(new ResourceLocation("letitems", "models/blocks/Shera_L_Greenwood.png"), "this", "Isekai Maou to Shoukan Shoujo no Dorei Majutsu"),
      TANYADEG(new ResourceLocation("letitems", "models/blocks/Tanya_Degurechaff.png"), "TwilightForest", "Youjo Senki"),
      ZERO(new ResourceLocation("letitems", "models/blocks/Zero.png"), "witchery", "Zero kara Hajimeru Mahou no Sho"),
      MAKINAMI(new ResourceLocation("letitems", "models/blocks/Mari_Illustrious_Makinami.png"), (String)null, "Neon Genesis Evangelion"),
      SHINJI(new ResourceLocation("letitems", "models/blocks/Shinji_Ikari.png"), (String)null, "Neon Genesis Evangelion"),
      MARY_SAOTOME(new ResourceLocation("letitems", "models/blocks/Mary_Saotome.png"), (String)null, "Kakegurui"),
      KOKO(new ResourceLocation("letitems", "models/blocks/Koko_Hekmatyar.png"), "this", "Jormungand", true),
      MISATO(new ResourceLocation("letitems", "models/blocks/Misato_Katsuragi.png"), (String)null, "Neon Genesis Evangelion"),
      AKATSUKI(new ResourceLocation("letitems", "models/blocks/Akatsuki.png"), (String)null, "Log Horizon"),
      OKABE(new ResourceLocation("letitems", "models/blocks/Rintarou_Okabe.png"), "appliedenergistics2", "Steins;Gate", true),
      KIRUKIRU_AMOU(new ResourceLocation("letitems", "models/blocks/Kirukiru_Amou.png"), (String)null, "Busou Shoujo Machiavellianism"),
      RIMURU_TEMPEST(new ResourceLocation("letitems", "models/blocks/Rimuru_Tempest.png"), (String)null, "Tensei shitara Slime Datta Ken"),
      SUZUNE(new ResourceLocation("letitems", "models/blocks/Suzune_Horikita.png"), (String)null, "Youkoso Jitsuryoku Shijou Shugi no Kyoushitsu e"),
      MISHA_TAKANASHI(new ResourceLocation("letitems", "models/blocks/Misha_Takanashi.png"), (String)null, "Uchi no Maid ga Uzasugiru!"),
      TOUKA_KIRISHIMA(new ResourceLocation("letitems", "models/blocks/Touka_Kirishima.png"), (String)null, "Tokyo Ghoul"),
      YUKINA_HIMERAGI(new ResourceLocation("letitems", "models/blocks/Yukina_Himeragi.png"), (String)null, "Strike the Blood"),
      RISTARTE(new ResourceLocation("letitems", "models/blocks/Ristarte.png"), "this", "Shinchou Yuusha: ..."),
      ALICE_KISARAGI(new ResourceLocation("letitems", "models/blocks/Alice_Kisaragi.png"), "Botania", "Sentouin, Hakenshimasu!", true),
      HAYASE_NAGATORO(new ResourceLocation("letitems", "models/blocks/Hayase_Nagatoro.png"), (String)null, "Ijiranaide, Nagatoro-san"),
      SISTINE_FIBEL(new ResourceLocation("letitems", "models/blocks/Sistine_Fibel.png"), "Botania", "Rokudenashi Majutsu Koushi to Akashic Records"),
      WIZ(new ResourceLocation("letitems", "models/blocks/Wiz.png"), (String)null, "Kono Subarashii Sekai ni Shukufuku wo!", true),
      IRINA_LUMINESK(new ResourceLocation("letitems", "models/blocks/Irina_Luminesk.png"), "GalacticraftCore", "Tsuki to Laika to Nosferatu", true),
      CHRIS(new ResourceLocation("letitems", "models/blocks/Chris.png"), (String)null, "Kono Subarashii Sekai ni Shukufuku wo!"),
      MELIDA_ANGEL(new ResourceLocation("letitems", "models/blocks/Melida_Angel.png"), (String)null, "Assassins Pride"),
      KILLUA_ZOLDYCK(new ResourceLocation("letitems", "models/blocks/Killua_Zoldyck.png"), "this", "Hunter x Hunter", true),
      MAHA_TUATHA_DE(new ResourceLocation("letitems", "models/blocks/Maha_Tuatha_De.png"), (String)null, "Sekai Saikou no Ansatsusha, Isekai Kizoku ni Tensei suru"),
      KONATA_IZUMI(new ResourceLocation("letitems", "models/blocks/Konata_Izumi.png"), (String)null, "Lucky☆Star"),
      TSUBAME_MIZUSAKI(new ResourceLocation("letitems", "models/blocks/Tsubame_Mizusaki.png"), (String)null, "Eizouken ni wa Te wo Dasu na!");

      private final ResourceLocation textureLocation;
      private final String loreText;
      private final String whatAnime;
      private final String itemName;
      private final boolean isGui;

      private DollType(ResourceLocation textureLocation, String lore, String whatAnime) {
         this(textureLocation, lore, whatAnime, false);
      }

      private DollType(ResourceLocation textureLocation, String lore, String whatAnime, boolean gui) {
         this.itemName = this.name().toLowerCase();
         this.textureLocation = textureLocation;
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
         return this.isGui;
      }

      public static BlockDoll.DollType getTypeFromStack(ItemStack stack) {
         BlockDoll.DollType[] values = values();
         if (stack != null) {
            Item item = stack.getItem();
            if (item instanceof ItemBlock && ((ItemBlock)item).field_150939_a instanceof BlockDoll) {
               int index = ((BlockDoll)((ItemBlock)item).field_150939_a).startTypeIndex + stack.getItemDamage();
               return getTypeFromIndex(index);
            }
         }

         return values[0];
      }

      public static BlockDoll.DollType getTypeFromIndex(int index) {
         BlockDoll.DollType[] values = values();
         return values[MathHelper.clamp_int(index, 0, values.length - 1)];
      }
   }
}
