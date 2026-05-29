# Минимальный технический дизайн: Case GUI + Shop + Economy (Minecraft 1.7.10, Forge)

## 1) Цель
Сделать расширение системы кейсов с тремя блоками:
1. **Case GUI** — красивое окно открытия кейса с предпросмотром содержимого.
2. **Shop** — покупка кейсов за валюту.
3. **Economy** — абстракция экономики, чтобы:
   - работать на чистом Forge;
   - опционально подключать Bukkit/Vault на гибридных серверах.

## 2) Что уже есть в моде (точки опоры)
- `ItemCase` уже умеет выдавать случайный предмет на сервере (правый клик).
- В проекте есть готовая инфраструктура GUI/Container/Packet на примере якоря мира (`GuiWorldAnchor`, `ContainerWorldAnchor`, `PacketClickButton`, `GuiHandler`).

Это значит, что кейсовый GUI логично внедрять в существующий паттерн (GUI id + container + packet -> серверная логика).

## 3) Минимальная архитектура

### 3.1 Пакет `ru.SmrItems.cases`

#### `CaseDefinition`
DTO кейса (id, displayName, price, list rewards).

#### `CaseReward`
DTO награды:
- `modid`
- `itemName`
- `maxAmount`
- `weight` (новое поле для редкости)

#### `CaseManager`
Единая точка работы с кейсами:
- загрузка `config/smritems/cases.json`
- валидация
- выдача случайной награды (`rollReward(caseId)`)
- кэш
- `reload()` для админ-команды

> Важно: убрать чтение json из `ItemCase` и делегировать в `CaseManager`.

### 3.2 Пакет `ru.SmrItems.common.economy`

#### `EconomyService` (интерфейс)
- `boolean has(EntityPlayerMP player, int amount)`
- `boolean withdraw(EntityPlayerMP player, int amount)`
- `int getBalance(EntityPlayerMP player)`

#### `ForgeEconomyService`
- хранение валюты в `ExtendedEntityProperties`/NBT для 1.7.10.

#### `VaultBridgeEconomyService` (опционально)
- включается только если найден Bukkit/Vault на гибридном сервере.
- если недоступен — fallback на `ForgeEconomyService`.

#### `EconomyProvider`
Фабрика/резолвер активной реализации экономики.

### 3.3 GUI слой

#### `GuiHandler`
Добавить новый ID (например `1`) для кейсового GUI.

#### `ContainerCase`
Минимальный контейнер для открытия (можно без физического инвентаря кейса, но с validation для игрока).

#### `GuiCase`
Отрисовать:
- название кейса;
- список возможных наград (иконка + название + редкость);
- цену;
- кнопки:
  - `Купить`;
  - `Открыть` (если кейс есть в инвентаре).

### 3.4 Сетевой слой

#### `PacketCaseAction`
Один пакет с actionType:
- `BUY_CASE`
- `OPEN_CASE`

На сервере:
1. повторная валидация входных данных;
2. денежная операция (`withdraw`) и/или проверка наличия кейса в инвентаре;
3. roll награды только на сервере;
4. выдача награды;
5. отправка result-пакета клиенту для обновления GUI/сообщений.

#### `PacketCaseResult`
Передаёт клиенту результат (успех/ошибка, награда, новый баланс).

## 4) Формат `cases.json` (минимум)
```json
{
  "vanilla": {
    "displayName": "Vanilla Case",
    "price": 250,
    "rewards": [
      {"modid":"minecraft","itemName":"diamond","maxAmount":8,"weight":60},
      {"modid":"minecraft","itemName":"nether_star","maxAmount":1,"weight":3}
    ]
  }
}
```

Правила:
- `maxAmount >= 1`
- `weight >= 1` (если нет — считать `1`)
- при невалидной записи — лог + пропуск записи

## 5) Алгоритм выбора награды (weighted random)
1. Сумма всех `weight`.
2. Рандом в диапазоне `[1..sum]`.
3. Проход по списку с накоплением веса до попадания в диапазон.
4. Количество предмета: `1..maxAmount`.

## 6) Безопасность и антидюп
- Никаких выдач на клиенте — только сервер.
- Перед открытием повторно проверить:
  - игрок online;
  - кейс в инвентаре/баланс;
  - caseId существует в кэше.
- Транзакционный порядок:
  - сначала списание валюты/кейса,
  - затем roll,
  - затем выдача.
- Если инвентарь заполнен — drop в мир рядом с игроком + лог.

## 7) Минимальный UX
- Цвет редкости (common/rare/epic/legendary) на основе weight.
- Tooltip с шансом (приблизительный `%`).
- Кнопка `Обновить`/`Перезагрузить` только для админа (или отдельная команда).

## 8) План внедрения (MVP, 4 шага)
1. **Refactor backend**:
   - вынести `CaseManager`;
   - добавить weight + валидацию;
   - оставить старое открытие по ПКМ как fallback.
2. **Economy abstraction**:
   - ввести `EconomyService` + `ForgeEconomyService`.
3. **GUI + packets**:
   - `GuiCase`, `ContainerCase`, `PacketCaseAction`, `PacketCaseResult`.
4. **Shop flow**:
   - покупка кейса за валюту + сообщения/локализация.

## 9) Совместимость с Bukkit
- На чистом Forge: работает через внутреннюю экономику.
- На Cauldron/Thermos: можно включить bridge к Vault.
- Рекомендация: bridge должен быть **опциональным модулем**, чтобы основной мод не падал без Bukkit.

## 10) Definition of Done (минимум)
- Игрок открывает GUI кейса и видит содержимое.
- Игрок может купить кейс за валюту.
- Открытие кейса выдаёт награду по weight-логике.
- Все критические действия проходят серверную валидацию.
- Конфиг кейсов можно перезагрузить без рестарта сервера.
