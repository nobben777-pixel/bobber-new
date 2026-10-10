# Руководство по участию в проекте (Contributing)

Спасибо что захотел помочь проекту Separate Bobbers! 🎉

Этот документ содержит рекомендации для участников проекта.

---

## 📋 Как помочь проекту

### 1. Отчёты об ошибках (Bug Reports)

Нашёл баг? Создай [Issue](https://github.com/nobben777-pixel/bobber/issues)!

**Пожалуйста, предоставь:**
- Версию мода
- Версию Minecraft (1.21.11)
- Версию Fabric Loader
- Описание проблемы (что делал, что произошло)
- Логи сервера (`logs/latest.log`)
- Способ воспроизведения (как повторить ошибку)

**Пример:**
```
Версия: 2.0.0
Minecraft: 1.21.11
Fabric Loader: 0.16.9

Проблема:
Поплавки не сохраняются после выхода из игры.

Как воспроизвести:
1. Бросить удочку на земле
2. Выйти из игры
3. Войти обратно
4. Поплавка нет ❌

Логи прилагаются.
```

---

### 2. Предложения по улучшению (Feature Requests)

Есть идея как улучшить мод? Создай [Discussion](https://github.com/nobben777-pixel/bobber/discussions)!

**Пожалуйста, опиши:**
- Что хочешь добавить
- Почему это нужно
- Как это может выглядеть

**Пример:**
```
Название: Команда для управления поплавками

Описание:
Было бы круто добавить команду /bobber list
чтобы видеть все активные поплавки.

Вариант использования:
/bobber list - показать все поплавки
/bobber clear - удалить все поплавки
/bobber save - сохранить сейчас
```

---

### 3. Код (Pull Requests)

Хочешь добавить свой код? Создай Pull Request!

#### Требования к коду:

**Стиль кода:**
```java
// ✅ Хорошо
public static void registerBobber(FishingBobberEntity bobber, 
                                  int rodId, 
                                  String playerUUID) {
    // Комментарии объясняют ЧТО и ПОЧЕМУ
    int bobberId = bobber.getId();
    BobberData data = new BobberData(...);
    BOBBER_MAP.put(bobberId, data);
}

// ❌ Плохо
public static void rb(FishingBobberEntity b, int r, String p) {
    int bi = b.getId();
    BobberData d = new BobberData(...);
    m.put(bi, d);
}
```

**Правила:**
- Используй осмысленные названия переменных
- Добавляй комментарии для сложного кода
- Одна функция = одна задача
- Максимум 100 строк в методе
- Используй Java 21 синтаксис

**Комментарии:**
```java
/**
 * Регистрируем новый поплавок с информацией о удочке
 * 
 * @param bobber FishingBobberEntity - поплавок для регистрации
 * @param rodId int - ID удочки которой принадлежит поплавок
 * @param playerUUID String - UUID игрока который бросил удочку
 */
public static void registerBobber(FishingBobberEntity bobber, 
                                  int rodId, 
                                  String playerUUID) {
    // ... реализация ...
}
```

#### Процесс создания PR:

1. **Форкни репозиторий**
   ```bash
   # На GitHub нажми "Fork"
   ```

2. **Клонируй свой форк**
   ```bash
   git clone https://github.com/ТВОЙ_НИК/bobber.git
   cd bobber
   ```

3. **Создай ветку для своей фишки**
   ```bash
   git checkout -b feature/твоя-фишка
   # Например: feature/add-bobber-commands
   ```

4. **Сделай свои изменения**
   ```bash
   # Редактируй файлы
   # Тестируй локально
   ```

5. **Закоммити с хорошим сообщением**
   ```bash
   git add .
   git commit -m "Add bobber commands feature

   - Добавлена команда /bobber list
   - Добавлена команда /bobber clear
   - Добавлены тесты для команд"
   ```

6. **Загрузи на свой форк**
   ```bash
   git push origin feature/твоя-фишка
   ```

7. **Создай Pull Request на GitHub**
   - Описание что делает PR
   - Ссылки на связанные issues
   - Скриншоты если нужно

---

## 📝 Правила коммитов (Commit Messages)

Используй формат **Conventional Commits**:

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Типы коммитов:
- `feat` - новая функция
- `fix` - исправление ошибки
- `docs` - изменение документации
- `style` - исправление стиля кода
- `refactor` - рефакторинг без изменения функции
- `test` - добавление тестов
- `chore` - обновление зависимостей

### Примеры:

```
feat(persistence): add bobber persistence system
fix(tracker): prevent bobber data duplication
docs(readme): update installation instructions
refactor(mixin): simplify FishingBobberEntityMixin
```

---

## 🔄 Процесс Code Review

Когда ты создаш PR:

1. **GitHub Actions автоматически**
   - ✅ Собирает код
   - ✅ Проверяет что JAR создан
   - ✅ Загружает артефакты

2. **Владелец репозитория**
   - 👀 Смотрит код
   - 💬 Оставляет комментарии
   - ✅ Одобряет или просит изменения

3. **Если нужны изменения**
   - Делаешь изменения в своей ветке
   - Коммитишь: `git commit -m "Review fixes"`
   - Загружаешь: `git push`
   - PR обновляется автоматически

4. **После одобрения**
   - Твой код мержится в main
   - GitHub Actions создаёт новую сборку
   - Твой код в мире! 🎉

---

## 🧪 Локальное тестирование

Перед тем как создавать PR, протестируй локально:

```bash
# 1. Собери мод
./gradlew build

# 2. Проверь что JAR создан
ls -la build/libs/separatebobbers-*.jar

# 3. Скопируй в мод-папку Minecraft
cp build/libs/separatebobbers-*.jar ~/.minecraft/mods/

# 4. Запусти Minecraft с Fabric
# 5. Протестируй свои изменения
# 6. Проверь что:
#    ✅ Мод загружается
#    ✅ Нет ошибок в логах
#    ✅ Функции работают как ожидается
```

---

## 📚 Документация кода

Все новые классы и методы должны иметь JavaDoc:

```java
/**
 * Максимально подробное описание что делает этот класс/метод.
 * 
 * Описание работы (если нужно).
 * Пример использования (если сложно).
 * 
 * @param параметр1 Описание
 * @param параметр2 Описание
 * @return Описание возвращаемого значения
 * @throws ИсключениеТип Когда это выбрасывается
 */
```

---

## 🎯 Направления разработки

### Приоритет 1 (Высокий)
- Исправление критических ошибок
- Улучшения производительности
- Улучшения документации

### Приоритет 2 (Средний)
- Новые функции (команды, конфиги)
- Улучшение пользовательского опыта
- Дополнительные тесты

### Приоритет 3 (Низкий)
- Косметические улучшения
- Рефакторинг кода
- Удаление технического долга

---

## ❓ Вопросы?

Если что-то непонятно:

1. Открой [Discussion](https://github.com/nobben777-pixel/bobber/discussions)
2. Напиши в Issues
3. Спроси в комментариях к PR

---

## 🎓 Полезные ссылки

- [Fabric Wiki](https://fabricmc.net/wiki/)
- [Minecraft Wiki](https://minecraft.fandom.com/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)

---

## 🙏 Спасибо!

Спасибо что помогаешь развивать Separate Bobbers! 

Твой вклад очень важен для проекта! 💪

---

**Happy coding!** 🚀✨
