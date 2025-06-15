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
import net.minecraft.client.resources.I18n;

public class ItemCase extends Item {
    private final String caseType;
    private static Map<String, List<CaseItem>> allCases;
    private static boolean hasAttemptedLoad = false;
    private final Random random = new Random();

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
            if (allCases == null && !hasAttemptedLoad) {
                allCases = loadCasesFromFile("config/smritems/cases.json");
                hasAttemptedLoad = true;
            }
            ItemStack reward = getRandomItemFromCase(caseType);
            if (reward != null) {
                player.inventory.addItemStackToInventory(reward);
                player.addChatComponentMessage(new ChatComponentText(I18n.format("chat.case.received", reward.getDisplayName())));
                stack.stackSize--; // Уменьшаем количество кейсов на 1
            } else {
                player.addChatComponentMessage(new ChatComponentText(I18n.format("chat.case.error.empty_or_not_found")));
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
        Map<String, List<CaseItem>> cases = allCases;
        if (cases != null && cases.containsKey(caseType)) {
            List<CaseItem> items = cases.get(caseType);
            if (items == null || items.isEmpty()) {
                System.out.println("Кейс типа " + caseType + " не содержит предметов.");
                return null;
            }

            // Случайный выбор предмета из списка
            CaseItem selectedItem = items.get(this.random.nextInt(items.size()));

            // Логирование для отладки
            System.out.println("Выбранный предмет: " + selectedItem.modid + ":" + selectedItem.itemName);

            // Проверка на наличие предмета в игре
            Item item = GameRegistry.findItem(selectedItem.modid, selectedItem.itemName);
            if (item != null) {
                // Случайное количество от 1 до maxAmount
                int maxAmount = selectedItem.maxAmount;
                if (maxAmount < 1) maxAmount = 1;
                int randomAmount = 1 + random.nextInt(maxAmount);
                return new ItemStack(item, randomAmount);
            } else {
                System.out.println("Не удалось найти предмет: " + selectedItem.modid + ":" + selectedItem.itemName);
            }
        } else {
            System.out.println("Кейс типа " + caseType + " не найден или пуст.");
        }
        return null;
    }






    private static Map<String, List<CaseItem>> loadCasesFromFile(String filePath) {
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
            System.err.println("Ошибка при загрузке файла cases.json из " + filePath + ": " + e.getMessage());
            e.printStackTrace(); // Keep stack trace for full details
        }
        return null;
    }
}
