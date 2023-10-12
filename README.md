# trainee-weather-rest
Веб-приложение на сервере Tomcat для получения данных о погоде через REST запрос и их выводе на html-страницу.
<br> Укажите локацию в качестве параметра в адресной строке (/get_weather?location=) для получения следующей актуальной информации: 
- погода на текущий момент;
- прогноз погоды на 3 дня;
- температура воздуха на весь день по часам.
<p> Cведения о погоде предоставляет сервис https://www.weatherapi.com/.
Для использования приложения необходимо использовать Ваш API-ключ для взаимодействия с сервисом в файле resources/config.properies.

# Документация
[Документация javadoc](https://valentinkd3.github.io/trainee-weather-rest/)

# Примеры выполнения запросов:
- При передаче параметра
<br><br> ![Ответ с параментрами](https://github.com/valentinkd3/trainee_weather_restapi/blob/a7b58ba088e8e5cac262a5b134bc0003d3ea4837/img/WithParam.png)
- При отсутсвии параметра
<br><br> ![Ответ без параметра](https://github.com/valentinkd3/trainee-weather-rest/blob/dcedd798a651742c7ae2cfb5adf9e58511d9c785/img/WithoutParam.png)
- При некорректном введении параметра
<br><br> ![Ответ с параментрами](https://github.com/valentinkd3/trainee-weather-rest/blob/dcedd798a651742c7ae2cfb5adf9e58511d9c785/img/Incorrect.png)



