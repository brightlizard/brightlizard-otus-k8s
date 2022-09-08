###### Тесты связаны постман-переменными. Руками подставлять ничего не надо между запросами/ответами.

<br>

**1.Создать пользователя.** 

Должен создаться аккаунт в биллинге.
Получаем список покупателей. Видим что один уже есть - Awesome customer
```
GET http://arch.homework/billing/customer
```

Создаем нового.
```
POST http://arch.homework/billing/customer
Content-Type: application/json
```

В тестах уже есть тело:
```
{
    "name": "Otus customer"
}
```

Проверяем что он появился.
```
GET http://arch.homework/billing/customer
```

Проверим аккаунт. Видим что денег 0.
```
GET http://arch.homework/billing/customer/{{customer_id}}/account
```
<br>

**2.Кладем 1000 на счет пользователя через сервис биллинга.**
```
PUT http://arch.homework/billing/customer/{{customer_id}}/account
{
    "deposit_value": 1000.00
}
```

Убеждаемся что деньги начислены.
```
GET http://arch.homework/billing/customer/{{customer_id}}/account
```
<br>

**3.Сделать заказ, на который хватает денег.**

Запускаем
```
POST http://arch.homework/order
```
как он есть в коллекции.

Посмотреть на статусы ответа и убедиться что заказ совершен успешно.
<br>
<br>

**4.Посмотреть деньги на счету пользователя и убедиться, что их сняли.**

Проверяем что деньги списались с аккаунта. Должно остаться 485.0
```
GET http://arch.homework/billing/customer/{{customer_id}}/account
```
<br>

**5. Посмотреть в сервисе нотификаций отправленные сообщения и убедиться, что сообщение отправилось**

Убедиться что есть запись об уведомлении для id заказа со статусом EVERYTHING_GOOD
```
GET http://arch.homework/notification
```
<br>

**6.Сделать заказ, на который не хватает денег.**

Изменяем в 
POST http://arch.homework/order заголовок X-Request-Id
и ставим в теле запроса другое время доставки, отличное от ("deliveryTime": "2022-08-17T21:00:00" плюс один час)
как он есть в коллекции.

Запускаем
```
POST http://arch.homework/order
```
и получаем ошибку создания заказа
<br>
<br>

**7.Посмотреть деньги на счету пользователя и убедиться, что их количество не поменялось.**

Проверяем что сумма на аккаунте неизменна 485.0
```
GET http://arch.homework/billing/customer/{{customer_id}}/account
```
<br>

**8.Посмотреть в сервисе нотификаций отправленные сообщения и убедиться, что сообщение отправилось.**

Убедиться что есть запись об уведомлении для id неудачного заказа со статусом ALL_BAD
```
GET http://arch.homework/notification
```