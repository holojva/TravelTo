import telebot
from telebot.types import InlineKeyboardMarkup, InlineKeyboardButton
import random
import requests
from flask import Flask, request, jsonify

TOKEN = 'bot-token-i-wont-share'
bot = telebot.TeleBot(TOKEN)
app = Flask(__name__)

REVIEWS = [
    {"id": 1, "location": "Эйфелева башня", "review": "Мне не понравилось, там нехорошо. Одни багеты.", "rating": 3},
    {"id": 2, "location": "Колизей", "review": "Великолепное место! Ощущаешь дух истории.", "rating": 5},
    {"id": 3, "location": "Статуя Свободы", "review": "Ожидал большего. Очереди огромные.", "rating": 2},
    {"id": 4, "location": "Пирамиды Гизы", "review": "Невероятное зрелище! Особенно на закате.", "rating": 5},
    {"id": 5, "location": "Тадж-Махал", "review": "Красиво, но слишком много туристов.", "rating": 4},
    {"id": 6, "location": "Сиди-Бу-Саид", "review": "Как будто попал в сказку! Голубые дома потрясающие.", "rating": 5},
    {"id": 7, "location": "Бурдж-Халифа", "review": "Высота впечатляет, но билеты слишком дорогие.", "rating": 3},
    {"id": 8, "location": "Озеро Байкал", "review": "Самое чистое озеро в мире. Обязательно к посещению!", "rating": 5},
    {"id": 9, "location": "Диснейленд в Париже", "review": "Дети были в восторге, но цены кусаются.", "rating": 4},
    {"id": 10, "location": "Ангкор-Ват", "review": "Уникальное место, но очень жарко и влажно.", "rating": 4},
]

# Location cards
LOCATIONS = [
    {
        "name": "Великая Китайская Стена",
        "description": "Одна из самых больших достопримечательностей Китая и Мира. Ее видно из космоса",
        "coordinates": "40.4319° N, 116.5704° E"
    },
    {
        "name": "Эйфелева башня",
        "description": "Символ Парижа и всей Франции. Построена в 1889 году.",
        "coordinates": "48.8584° N, 2.2945° E"
    },
    {
        "name": "Колизей",
        "description": "Амфитеатр в Риме, где проходили гладиаторские бои. Символ Римской империи.",
        "coordinates": "41.8902° N, 12.4924° E"
    },
    {
        "name": "Тадж-Махал",
        "description": "Мавзолей-мечеть в Индии, построенный из белого мрамора. Шедевр архитектуры.",
        "coordinates": "27.1751° N, 78.0421° E"
    },
    {
        "name": "Мачу-Пикчу",
        "description": "Древний город инков в Перу, расположенный высоко в горах.",
        "coordinates": "13.1631° S, 72.5450° W"
    },
]

user_states = {}
MODERATION_SERVER = "https://travelto.com/api" #пока в разработке


def format_review(review):
    return f"""Точка: {review['location']}
ID: {review['id']}
Отзыв: "{review['review']}"
Оценка: {review['rating']}"""


def format_location(location):
    return f"""Место: {location['name']}
Описание: {location['description']}
Координаты: {location['coordinates']}"""


def send_next_item(chat_id):
    user_state = user_states.get(chat_id, {'review_index': 0, 'location_index': 0, 'last_sent': None})

    if user_state['last_sent'] != 'review' and user_state['review_index'] < len(REVIEWS):
        review = REVIEWS[user_state['review_index']]
        keyboard = InlineKeyboardMarkup()
        keyboard.add(
            InlineKeyboardButton("👍 Принять (+)", callback_data=f"accept_{review['id']}"),
            InlineKeyboardButton("👎 Отклонить (-)", callback_data=f"reject_{review['id']}")
        )
        bot.send_message(chat_id, format_review(review), reply_markup=keyboard)
        user_state['review_index'] += 1
        user_state['last_sent'] = 'review'
    elif user_state['location_index'] < len(LOCATIONS):
        location = LOCATIONS[user_state['location_index']]
        keyboard = InlineKeyboardMarkup()
        keyboard.add(
            InlineKeyboardButton("👍 Принять (+)", callback_data=f"accept_{location['name']}"),
            InlineKeyboardButton("👎 Отклонить (-)", callback_data=f"reject_{location['name']}")
        )
        bot.send_message(chat_id, format_location(location))
        user_state['location_index'] += 1
        user_state['last_sent'] = 'location'
    else:
        keyboard = InlineKeyboardMarkup()
        keyboard.add(InlineKeyboardButton("Да", callback_data="restart_yes"))
        bot.send_message(chat_id, "Все места отмодерированы. Перезагрузить демо-версию?", reply_markup=keyboard)
        return

    user_states[chat_id] = user_state


@app.route('/add_review', methods=['POST'])
def add_review():
    data = request.json
    new_id = max([r['id'] for r in REVIEWS], default=0) + 1
    REVIEWS.append({
        "id": new_id,
        "location": data.get('location', ''),
        "review": data.get('review', ''),
        "rating": data.get('rating', 0)
    })
    return jsonify({"status": "success", "id": new_id})


@app.route('/add_location', methods=['POST'])
def add_location():
    data = request.json
    LOCATIONS.append({
        "name": data.get('name', ''),
        "description": data.get('description', ''),
        "coordinates": data.get('coordinates', '')
    })
    return jsonify({"status": "success"})


def send_to_moderation_server(data):
    try:
        requests.post(f"{MODERATION_SERVER}/moderation_result", json=data)
    except:
        pass


@bot.message_handler(commands=['start'])
def start(message):
    chat_id = message.chat.id
    user_states[chat_id] = {'review_index': 0, 'location_index': 0, 'last_sent': None}
    bot.send_message(chat_id, "Добро пожаловать в бот для модерации отзывов!")
    send_next_item(chat_id)


@bot.callback_query_handler(func=lambda call: True)
def handle_query(call):
    chat_id = call.message.chat.id
    data = call.data

    if data.startswith("accept_"):
        review_id = int(data.split('_')[1])
        review = next((r for r in REVIEWS if r['id'] == review_id), None)
        if review:
            send_to_moderation_server({
                "action": "accept",
                "review_id": review_id,
                "review_data": review
            })
        bot.answer_callback_query(call.id, "Отзыв принят.")
        bot.send_message(chat_id, "Отзыв принят.")
        send_next_item(chat_id)
    elif data.startswith("reject_"):
        review_id = int(data.split('_')[1])
        review = next((r for r in REVIEWS if r['id'] == review_id), None)
        if review:
            send_to_moderation_server({
                "action": "reject",
                "review_id": review_id,
                "review_data": review
            })
        bot.answer_callback_query(call.id, "Отзыв не принят.")
        bot.send_message(chat_id, "Отзыв не принят.")
        send_next_item(chat_id)
    elif data == "restart_yes":
        user_states[chat_id] = {'review_index': 0, 'location_index': 0, 'last_sent': None}
        bot.send_message(chat_id, "Демо-версия перезагружена!")
        send_next_item(chat_id)


if __name__ == '__main__':
    import threading

    threading.Thread(target=app.run, kwargs={'host': '0.0.0.0', 'port': 5000}).start()
    bot.infinity_polling()