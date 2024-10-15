package ru.SmrItems.items;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import ru.SmrItems.SmMain;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ItemCase extends Item {
    private final String caseType;

    public ItemCase(String name, String texture, int maxStackSize, String caseType) {
        this.caseType = caseType;
        this.setUnlocalizedName(name);
        this.setTextureName("SmrItems:" + texture);
        this.setCreativeTab(SmMain.tabsmritems);
        this.maxStackSize = maxStackSize;
        GameRegistry.registerItem(this, name);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            ItemStack reward = getRandomItemFromCase(caseType);
            if (reward != null) {
                player.inventory.addItemStackToInventory(reward);
                player.addChatComponentMessage(new ChatComponentText("Вы получили: " + reward.getDisplayName()));
                stack.stackSize--; // Уменьшаем количество кейсов на 1
            } else {
                //player.addChatComponentMessage(new ChatComponentText("The case is empty or an error occurred."));
            }
        }
        return stack;
    }

    private static class CaseItem {
        String modid;
        String itemName;
        int maxAmount;
    }

    private ItemStack getRandomItemFromCase(String caseType) {
        // Загрузка предметов из JSON
        Map<String, List<CaseItem>> cases = loadCasesFromFile("config/smritems/cases.json");
        if (cases != null && cases.containsKey(caseType)) {
            List<CaseItem> items = cases.get(caseType);
            Random random = new Random();

            // Случайный выбор предмета из списка
            CaseItem selectedItem = items.get(random.nextInt(items.size()));

            // Логирование для отладки
            System.out.println("Выбранный предмет: " + selectedItem.modid + ":" + selectedItem.itemName);

            // Проверка на наличие предмета в игре
            Item item = GameRegistry.findItem(selectedItem.modid, selectedItem.itemName);
            if (item != null) {
                // Случайное количество от 1 до maxAmount
                int randomAmount = ThreadLocalRandom.current().nextInt(1, selectedItem.maxAmount + 1);
                return new ItemStack(item, randomAmount);
            } else {
                System.out.println("Не удалось найти предмет: " + selectedItem.modid + ":" + selectedItem.itemName);
            }
        } else {
            System.out.println("Кейс типа " + caseType + " не найден или пуст.");
        }
        return null;
    }






    private Map<String, List<CaseItem>> loadCasesFromFile(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<Map<String, List<CaseItem>>>() {}.getType();
            Map<String, List<CaseItem>> cases = gson.fromJson(reader, type);

            // Логирование успешной загрузки
            if (cases != null) {
                System.out.println("Файл JSON загружен успешно: " + cases.keySet());
            } else {
                System.out.println("Файл JSON пуст или не может быть прочитан.");
            }

            return cases;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
