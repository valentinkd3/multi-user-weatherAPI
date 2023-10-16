# trainee-weather-rest
Веб-приложение на сервере Tomcat для получения данных о погоде через REST запрос и сохранении их в базу данных PostgreSQL. Выводит на html-страницу данные о погоде для текущей локации, а также
информацию, содержащуюся в базе данных с предоставлением сведений о самом теплом и холодном городе на сегодняшний день.
<br> Укажите локацию в качестве параметра в адресной строке (/get_weather?location=) для получения следующей актуальной информации: 
- погода на текущий момент;
- прогноз погоды на 3 дня;
- температура воздуха на весь день по часам.
<p> Для получения информации из базы данных о погоде на сегодняший день, а также о городах с самыми низкими и высокими температурами воздуха, необходимо сделать запрос (/get_data).
<p> Cведения о погоде предоставляет сервис https://www.weatherapi.com/.
Для использования приложения необходимо использовать Ваш API-ключ для взаимодействия с сервисом в файле resources/config.properies.
Для подключения к базе данных в файле resources/database.properies необходимо указать JDBC-драйвер для PostgreSQL, url для подключения к Вашей базе данных, имя пользователя и пароль.

# Документация
[Документация javadoc](https://valentinkd3.github.io/trainee-weather-rest/)

# Примеры выполнения запросов:
- При передаче параметра
<br><br> ![Ответ с параментрами](https://github.com/valentinkd3/trainee_weather_restapi/blob/a7b58ba088e8e5cac262a5b134bc0003d3ea4837/img/WithParam.png)
- При получении информации из базы данных
<br><br> ![Ответ из базы данных](https://github.com/valentinkd3/trainee-weather-rest/blob/6131b115e18b522859a86066221c590980176ef1/img/getData.png)
- При отсуствии данных в базе данных
<br><br> ![Ответ из базы данных](https://github.com/valentinkd3/trainee-weather-rest/blob/ebba1498efb6f6e47d4839931a53ed69d71da377/img/noData.png)
- При отсутсвии параметра
<br><br> ![Ответ без параметра](https://github.com/valentinkd3/trainee-weather-rest/blob/dcedd798a651742c7ae2cfb5adf9e58511d9c785/img/WithoutParam.png)
- При некорректном введении параметра
<br><br> ![Ответ с параментрами](https://github.com/valentinkd3/trainee-weather-rest/blob/dcedd798a651742c7ae2cfb5adf9e58511d9c785/img/Incorrect.png)



