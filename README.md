# Card_transfer_service

Приложение слушает порт 5500, итого имеем 2 эндпоинта вызова:
1. http://localhost:5500/transfer
2. http://localhost:5500/confirmOperation

В конструкторе репозитория создаются две карты и связанные с ними два счета.\
Карта без привязанного к ней счета не существует.\
Для тестирования перевода необходимо передать в теле запроса на эндпоинт http://localhost:5500/transfer информацию:

    {
        "cardFromNumber": "1234123412341234",
        "cardFromValidTill": "12/24",
        "cardFromCVV": "123",
        "cardToNumber": "4321432143214321",
        "amount": {
            "value": 100,
            "currency": "RUR"
        }
    }`

В ответ сервис сгенерирует UUID операции списания денежных средств,
который будет необходимо передать на второй эндпоинт с кодом подтверждения (всегда 0000), например:

    {
        "operationId": "3eff2b83-9ea4-43c0-9a64-b11765106b89",
        "code": "0000"
    }

