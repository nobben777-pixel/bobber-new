# 🎣 Separate Bobbers Mod

[![Build Status](https://github.com/nobben777-pixel/bobber/workflows/Build%20Separate%20Bobbers%20Mod/badge.svg)](https://github.com/nobben777-pixel/bobber/actions)
[![Latest Release](https://img.shields.io/github/v/release/nobben777-pixel/bobber?include_prereleases)](https://github.com/nobben777-pixel/bobber/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Minecraft 1.21.11](https://img.shields.io/badge/Minecraft-1.21.11-brightgreen)](https://www.minecraft.net)
[![Fabric](https://img.shields.io/badge/Modloader-Fabric-darkgreen)](https://fabricmc.net)
[![Java 21+](https://img.shields.io/badge/Java-21+-orange)](https://www.oracle.com/java/)

> У каждой удочки свой отдельный поплавок! 🎣 И они **сохраняются** после перезагрузки сервера!

---

## ✨ Возможности

✅ **Отдельный поплавок для каждой удочки**
- Брось удочку #1 → появится поплавок #1
- Возьми удочку #2 → появится поплавок #2
- Каждой удочке свой!

✅ **Поплавки сохраняются после перезагрузки**
- Бросишь поплавок на нажимную плиту
- Выйдешь из игры / перезагрузится сервер
- Поплавок остаётся на месте! 🎉

✅ **Беспроводной редстоун**
- Используй поплавок как датчик на нажимной плите
- Работает даже в других измерениях!
- Вечный сигнал без проводов

✅ **Автоматическая сборка на GitHub**
- При каждом push в main → автоматическая сборка
- При создании тега → автоматический релиз с JAR
- Всегда актуальный готовый мод

---

## 📥 Установка

### Требования
- **Minecraft**: 1.21.11
- **Fabric Loader**: 0.16.9+
- **Fabric API**: 0.107.0+
- **Java**: 21+

### Способ 1: Скачать готовый мод
1. Перейди на [GitHub Releases](https://github.com/nobben777-pixel/bobber/releases)
2. Скачай `separatebobbers-2.0.0.jar`
3. Положи в папку `mods`
4. Запусти Minecraft с Fabric

### Способ 2: Собрать из исходников
```bash
git clone https://github.com/nobben777-pixel/bobber.git
cd bobber
./gradlew build
# JAR будет в build/libs/separatebobbers-2.0.0.jar
```

---

## 🎮 Как использовать

### Базовое использование
1. Возьми удочку
2. Нажми ПКМ чтобы бросить поплавок
3. Возьми другую удочку → новый поплавок
4. У каждой удочки свой! ✅

### Беспроводной редстоун
1. Бросишь удочку так, чтобы поплавок встал на нажимную плиту
2. Плита активируется и создаёт сигнал редстоуна
3. Поплавок остаётся на плите даже если:
   - Ушёл в другое измерение
   - Перезагрузился сервер
   - Вышел и зашёл в игру
4. Вечный беспроводной датчик! 🎉

---

## 🏗️ Архитектура

### Ключевые компоненты

```
┌─────────────────────────────────────────┐
│      SEPARATE BOBBERS v2.0.0            │
└─────────────────────────────────────────┘
       ↓              ↓              ↓
   ┌────────┐   ┌──────────┐   ┌──────────┐
   │RodIds  │   │BobberData│   │Persistence
   │(ID)    │   │(Память)  │   │(Сохранение
   └────────┘   └──────────┘   └──────────┘
       ↓              ↓              ↓
   Удочка      Поплавок в      NBT файл
   (NBT)       памяти сервера   (world/)
```

### Система сохранения

```
ЗАПУСК СЕРВЕРА
    ↓
BobberPersistence.loadBobbers()
    ├─ Читает world/separatebobbers_data.nbt
    ├─ Восстанавливает в памяти BobberTracker
    └─ Пересоздаёт поплавки в мире
    ↓
СЕРВЕР РАБОТАЕТ
    ├─ Каждые 5 минут: автосохранение
    ├─ При создании поплавка: регистрация
    └─ При удалении: удаление из памяти
    ↓
ОСТАНОВКА СЕРВЕРА
    ↓
BobberPersistence.saveBobbers()
    ├─ Сохраняет все поплавки
    └─ Записывает в world/separatebobbers_data.nbt
```

### Файловая структура

```
src/main/java/com/example/separatebobbers/
├── SeparateBobbers.java              ← Главный класс
├── BobberTracker.java                ← Отслеживание
├── BobberPersistence.java ⭐         ← Сохранение/загрузка (НОВОЕ!)
├── RodIds.java                       ← ID удочек
└── mixin/
    ├── FishingRodItemMixin.java      ← Перехват использования
    └── FishingBobberEntityMixin.java ← Регистрация поплавков
```

---

## 🔄 GitHub Actions (CI/CD)

### Автоматическая сборка

Этот проект использует GitHub Actions для автоматической сборки:

#### `.github/workflows/build.yml`
- Собирает мод при каждом push в `main` или `develop`
- Загружает артефакты (JAR файлы)
- Проверяет что JAR создан успешно

#### `.github/workflows/release.yml`
- Срабатывает при создании тега (`v2.0.0`, `release-2.0.1`)
- Автоматически создаёт GitHub Release
- Загружает JAR файл в релиз
- Использует CHANGELOG.md для описания

### Как создать релиз

```bash
# 1. Обнови версию в gradle.properties
# 2. Закоммити изменения
git add .
git commit -m "Release v2.0.0"

# 3. Создай тег
git tag v2.0.0

# 4. Загрузи на GitHub
git push origin main
git push origin v2.0.0

# 5. GitHub Actions автоматически:
#    ✅ Соберёт мод
#    ✅ Создаст Release
#    ✅ Загрузит JAR
```

---

## 📊 Версии

| Версия | Дата | Статус | Скачать |
|--------|------|--------|---------|
| 2.0.0 | 10.10.2024 | ✅ Стабильна | [Release](https://github.com/nobben777-pixel/bobber/releases/tag/v2.0.0) |
| 1.0.0 | 09.10.2024 | ✅ Архив | [Release](https://github.com/nobben777-pixel/bobber/releases/tag/v1.0.0) |

---

## 🔧 Разработка

### Требования для разработки
- JDK 21+
- Git
- Gradle (входит в проект)

### Локальная сборка
```bash
# Клонировать репозиторий
git clone https://github.com/nobben777-pixel/bobber.git
cd bobber

# Собрать
./gradlew build

# Готовый JAR
build/libs/separatebobbers-2.0.0.jar
```

### Структура проекта
```
bobber/
├── src/main/
│   ├── java/               ← Java код
│   └── resources/          ← JSON конфиги, текстуры
├── build.gradle            ← Конфигурация сборки
├── gradle.properties       ← Версии зависимостей
├── settings.gradle         ← Настройки Gradle
├── .github/workflows/      ← GitHub Actions
├── CHANGELOG.md            ← История версий
├── README.md               ← Документация (в архиве)
└── README_GITHUB.md        ← Этот файл
```

---

## 🐛 Отчёты об ошибках

Нашёл баг? Создай [Issue](https://github.com/nobben777-pixel/bobber/issues)!

Пожалуйста, предоставь:
- Версию мода
- Версию Minecraft
- Версию Fabric Loader
- Описание проблемы
- Логи сервера (если есть)

---

## 💡 Предложения по улучшению

Есть идея как улучшить мод? Создай [Discussion](https://github.com/nobben777-pixel/bobber/discussions)!

Или форкни репозиторий и создай Pull Request.

---

## 📝 Лицензия

Этот проект лицензирован под MIT License - смотри файл [LICENSE](LICENSE) для деталей.

MIT License позволяет:
- ✅ Коммерческое использование
- ✅ Модификация
- ✅ Распространение
- ✅ Приватное использование

При условии:
- ⚠️ Указание лицензии
- ⚠️ Указание авторства

---

## 👨‍💻 Автор

**nobben777-pixel** - разработчик мода

Благодарности:
- Fabric Team - за модлоадер
- Minecraft Community - за вдохновение

---

## 📚 Документация

В архиве мода ты найдёшь подробные документы:
- `ЧИТАЙ_ПЕРВЫМ.txt` - быстрый старт
- `ИНСТРУКЦИЯ_СБОРКА.md` - пошаговая сборка
- `КАК_РАБОТАЕТ_СИСТЕМА.md` - техническое объяснение
- `ФИНАЛЬНЫЙ_ОТЧЕТ.md` - полный отчет

---

## 🎯 Статус проекта

- ✅ **Основное сохранение поплавков** - ГОТОВО
- ✅ **GitHub Actions** - ГОТОВО
- ✅ **Документация** - ГОТОВО
- 🔄 **Дополнительные команды** - планируется
- 🔄 **Конфиг файл** - планируется
- 🔄 **WebUI** - планируется

---

## 📞 Контакты и поддержка

- **GitHub Issues**: [Создать issue](https://github.com/nobben777-pixel/bobber/issues)
- **Discussions**: [Обсудить идеи](https://github.com/nobben777-pixel/bobber/discussions)
- **Pull Requests**: [Отправить улучшения](https://github.com/nobben777-pixel/bobber/pulls)

---

## 🎉 Приятной рыбалки!

Спасибо что используешь Separate Bobbers! 🎣✨

Если мод понравился - оставь ⭐ Star на GitHub! 😊

---

**Последнее обновление:** 10 октября 2024  
**Версия:** 2.0.0  
**Статус:** ✅ Готово к использованию
