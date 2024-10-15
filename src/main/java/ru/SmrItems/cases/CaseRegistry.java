package ru.SmrItems.cases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class CaseRegistry {
    private static Map<String, List<CaseItem>> cases;

    // Инициализация кейсов, загрузка данных из JSON файла
    public static void init() {
        loadCasesFromFile("config/smr_items/cases.json");
    }

    private static void loadCasesFromFile(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<Map<String, List<CaseItem>>>() {}.getType();
            cases = gson.fromJson(reader, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Получение случайного предмета из кейса
    public static ItemStack getRandomItemFromCase(String caseName) {
        if (cases != null && cases.containsKey(caseName)) {
            List<CaseItem> items = cases.get(caseName);
            int randomIndex = (int) (Math.random() * items.size());
            CaseItem item = items.get(randomIndex);
            return new ItemStack(GameRegistry.findItem(item.modid, item.item), item.amount);
        }
        return null;
    }

    // Новый метод для получения всех кейсов
    public static Map<String, List<CaseItem>> getCases() {
        return cases;
    }
}

class CaseItem {
    String modid;
    String item;
    int amount;
}
