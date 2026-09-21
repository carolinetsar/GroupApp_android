package com.groupapp.data

/** Seed content so the app is not empty on first launch. */
object DemoData {

    private const val HOUR = 3_600_000L
    private const val DAY = 24 * HOUR

    fun initial(): AppData {
        val now = System.currentTimeMillis()

        val users = listOf(
            User("me", "Анна", "Москва", "Люблю кофе, настолки и совместные закупки", "🙂"),
            User("u1", "Дмитрий", "Москва", "Организую кофейные встречи с 2019 года", "🧔"),
            User("u2", "Мария", "Москва", "Дизайнер, ищу компанию для прогулок", "👩‍🎨"),
            User("u3", "Игорь", "Москва", "Велосипеды, походы, аренда снаряжения", "🚴"),
            User("u4", "Ольга", "Санкт-Петербург", "Обожаю уютные кафе и выставки", "🌷"),
            User("u5", "Сергей", "Санкт-Петербург", "Совместные закупки чая и специй", "🍵"),
            User("u6", "Катя", "Москва", "Настольные игры каждую неделю", "🎲")
        )

        val gatherings = listOf(
            Gathering(
                id = "g1",
                title = "Кофе на Патриарших",
                description = "Собираемся на утренний кофе и болтовню. Возьму настолку на случай дождя.",
                category = Category.CAFE,
                city = "Москва",
                place = "Патриаршие пруды, кофейня «Ласточка»",
                dateTime = now + DAY + 9 * HOUR,
                price = 600.0,
                discountPercent = 20,
                peopleNeeded = 4,
                participantIds = listOf("u1", "me"),
                creatorId = "u1",
                createdAt = now - 5 * HOUR
            ),
            Gathering(
                id = "g2",
                title = "Совместная закупка кофе из Эфиопии",
                description = "Оптом дешевле: 1 кг зерна выходит почти вдвое дешевле розницы. Забираем со склада.",
                category = Category.GROUP_PURCHASE,
                city = "Москва",
                place = "Склад на Дмитровской, ул. Новодмитровская 2",
                dateTime = now + 3 * DAY + 12 * HOUR,
                price = 1200.0,
                discountPercent = 35,
                peopleNeeded = 10,
                participantIds = listOf("u1", "u2", "u6", "u3"),
                creatorId = "u1",
                createdAt = now - 30 * HOUR
            ),
            Gathering(
                id = "g3",
                title = "Аренда лофта на вечеринку",
                description = "Ищу 8 человек, чтобы разделить стоимость лофта. Музыка, настолки, приносим еду с собой.",
                category = Category.RENT,
                city = "Москва",
                place = "Лофт «Красный Октябрь», Болотный остров",
                dateTime = now + 5 * DAY + 18 * HOUR,
                price = 1500.0,
                discountPercent = 0,
                peopleNeeded = 8,
                participantIds = listOf("me", "u6"),
                creatorId = "me",
                createdAt = now - 2 * DAY
            ),
            Gathering(
                id = "g4",
                title = "Утренняя прогулка по ВДНХ",
                description = "Неспешные 5 км, кофе в конце. Присоединяйтесь, если любите гулять рано.",
                category = Category.WALK,
                city = "Москва",
                place = "ВДНХ, главный вход у арки",
                dateTime = now + 2 * DAY + 9 * HOUR,
                price = 0.0,
                discountPercent = 0,
                peopleNeeded = 6,
                participantIds = listOf("u2", "me", "u3"),
                creatorId = "u2",
                createdAt = now - 12 * HOUR
            ),
            Gathering(
                id = "g5",
                title = "Настолки в антикафе",
                description = "Играем в «Каркассон» и «Кодовые имена». Новичкам объясним правила.",
                category = Category.OTHER,
                city = "Москва",
                place = "Антикафе «Циферблат», Покровка 12",
                dateTime = now + 2 * DAY + 20 * HOUR,
                price = 400.0,
                discountPercent = 10,
                peopleNeeded = 5,
                participantIds = listOf("u6", "u2"),
                creatorId = "u6",
                createdAt = now - 8 * HOUR
            ),
            Gathering(
                id = "g6",
                title = "Пицца-вечер после работы",
                description = "Большая пицца на всех дешевле, если нас шестеро. Обсуждаем планы на выходные.",
                category = Category.CAFE,
                city = "Москва",
                place = "Пиццерия «Форно», Большая Дмитровка 9",
                dateTime = now + 4 * DAY + 19 * HOUR,
                price = 750.0,
                discountPercent = 15,
                peopleNeeded = 6,
                participantIds = listOf("u1"),
                creatorId = "u1",
                createdAt = now - 3 * HOUR
            ),
            Gathering(
                id = "g7",
                title = "Кофе и круассаны на Рубинштейна",
                description = "Уютное место, мало людей по утрам. Идеально для знакомства.",
                category = Category.CAFE,
                city = "Санкт-Петербург",
                place = "ул. Рубинштейна 15, кофейня «Утро»",
                dateTime = now + DAY + 10 * HOUR,
                price = 550.0,
                discountPercent = 25,
                peopleNeeded = 4,
                participantIds = listOf("u4"),
                creatorId = "u4",
                createdAt = now - 6 * HOUR
            ),
            Gathering(
                id = "g8",
                title = "Аренда велосипедов в Парке 300-летия",
                description = "Берём велики на 3 часа, катаемся вдоль залива. Нужно минимум 5 человек для скидки.",
                category = Category.RENT,
                city = "Санкт-Петербург",
                place = "Парк 300-летия Санкт-Петербурга, прокат у входа",
                dateTime = now + 2 * DAY + 13 * HOUR,
                price = 900.0,
                discountPercent = 30,
                peopleNeeded = 5,
                participantIds = listOf("u4", "u5"),
                creatorId = "u5",
                createdAt = now - 20 * HOUR
            ),
            Gathering(
                id = "g9",
                title = "Совместная закупка чая и специй",
                description = "Заказываем напрямую у поставщика. Минимум 8 участников, тогда доставка бесплатная.",
                category = Category.GROUP_PURCHASE,
                city = "Санкт-Петербург",
                place = "Самовывоз с Лиговского проспекта 30",
                dateTime = now + 6 * DAY + 15 * HOUR,
                price = 1000.0,
                discountPercent = 40,
                peopleNeeded = 8,
                participantIds = listOf("u5", "u4"),
                creatorId = "u5",
                createdAt = now - 40 * HOUR
            ),
            Gathering(
                id = "g10",
                title = "Прогулка по набережной Фонтанки",
                description = "Вечерняя прогулка с фотографиями. Возьму камеру, покажу красивые ракурсы.",
                category = Category.WALK,
                city = "Санкт-Петербург",
                place = "Набережная реки Фонтанки, у Аничкова моста",
                dateTime = now + 3 * DAY + 18 * HOUR,
                price = 0.0,
                discountPercent = 0,
                peopleNeeded = 7,
                participantIds = listOf("u4"),
                creatorId = "u4",
                createdAt = now - 15 * HOUR
            )
        )

        val reviews = listOf(
            Review("r1", "u1", "u6", "Катя", 5, "Отличный организатор, всё чётко по времени.", now - 6 * DAY),
            Review("r2", "u1", "u2", "Мария", 4, "Хорошая компания, кофе действительно вкусный.", now - 12 * DAY),
            Review("r3", "u1", "me", "Анна", 5, "Второй раз прихожу, всегда приятно.", now - 20 * DAY),
            Review("r4", "u2", "u4", "Ольга", 5, "Мария очень внимательная, спасибо за прогулку!", now - 4 * DAY),
            Review("r5", "u2", "u1", "Дмитрий", 4, "Всё понравилось, чуть задержались со стартом.", now - 9 * DAY),
            Review("r6", "u3", "u5", "Сергей", 5, "Помог с велосипедом, всё рассказал.", now - 3 * DAY),
            Review("r7", "u4", "u5", "Сергей", 5, "Ольга знает город лучше любого гида.", now - 2 * DAY),
            Review("r8", "u4", "me", "Анна", 4, "Приятная встреча, обязательно приеду ещё.", now - 8 * DAY),
            Review("r9", "u5", "u4", "Ольга", 5, "Чай был отличный, доставка быстрая.", now - 5 * DAY),
            Review("r10", "u6", "u2", "Мария", 5, "Катя объяснила правила новичкам, было весело.", now - DAY),
            Review("r11", "u6", "u3", "Игорь", 4, "Хороший вечер, но место шумновато.", now - 11 * DAY),
            Review("r12", "me", "u1", "Дмитрий", 5, "Анна собрала классную компанию на лофт.", now - 2 * DAY)
        )

        return AppData(
            users = users,
            gatherings = gatherings,
            reviews = reviews,
            currentUserId = "me",
            selectedCity = "Москва"
        )
    }
}
