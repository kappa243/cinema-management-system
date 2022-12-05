# Cinema

> Poniższa dokumentacja jest oparta na ostatnim pełnym stanie projektu **M1**.

# Struktura projektu

Funkcjonalności projektu zostały podzielone na następujące grupy:
- część głównej aplikacji - zarządzająca uruchomieniem aplikacji oraz inicjalizacją Spring'a i JavaFX'a
- część UI - odowiedzialna za zarządzanie widokiem, prezentacją danych i komunikacją z użytkownikiem
- część modeli danych - tworzy modele bazodanowe, zapewnia komunikację z bazą danych poprzez API i udostępnia serwisy
  do swoich modeli

## Główna aplikacja

Jej głównym zadaniem jest inicjalizacja JavaFX i Spring, z odpowiednimi ustawieniami. Proces ten zaczyna się w *CinemaApplication.java*. Uruchamia on event odpowiedzialny za utworzonie głównego okna aplikacji oraz rejestruje beany dające dostęp do aplikacji.

*StageManager.java* jest rdzeniem aplikacji, który może uruchomić i zamknąć program z dowolnego miejsca aplikacji (rejestruje EventListener'y, które odbierają eventy za to odpowiedzialne; np. *CinemaApplication.java* wywołuje event uruchomienia) oraz zapewnia dostęp do głównego widoku.

Zadaniem *ViewManager.java* jest załadowanie widoku FXML i utworzenie controllerów odpowiedzialnych za nie poprzez Spring'a. Dzięki temu obiekty w controllerach zostaną wstrzyknięte przez Spring'a.


## UI

Zawiera controllery widoków.

### *ApplicationController*

Odpowiada za główne okno aplikacji, w którym znajduje się lista osób oraz guziki do zarządzania nimi. Mogą one tworzyć, edytować i usuwać użytkowników. W przypadku tworzenia i edycji otwierany jest osobne okno, w którym można wprowadzić dane.

### *EditUserController*

Okno edycji użytkownika, w którym można zmienić jego informacje. Jednocześnie jest wykorzystywane jako okno tworzenia nowego użytkownika.


## Modele danych

Zawiera modele danych oraz serwisy do ich obsługi. Modele danych są mapowane na tabele w bazie danych przy użyciu JPA. Serwisy udostępniają API do komunikacji z bazą danych.

![Diagram bazodanowy](./assets/database_diagram.png)

## Tabela Person

Zawiera użytkowników aplikacji. Użytkownik posiada imię, nazwisko, adres email oraz funkcję. Na razie przewidziano 3 funkcje użytkowników - sprzedawca biletów, moderator zajmujący się obsługą sal oraz administrator.

## Tabela Movie

Zawiera dostępne filmy. Film posiada tytuł, opis, datę wydania i odniesienie do spektakli, na których jest wyświetlany.

## Tabela Room

Zawiera sale kinowe. Sala posiada nazwę oraz liczbę miejsc.

## Tabela Show

Zawiera spektakle. Spektakl posiada czas rozpoczęcia, czas zakończenia, film, salę, czas rozpoczęcia sprzedawania biletów, cenę biletu, liczbę sprzedanych biletów.

### Funkcjonalność dodawania użytkownika

Dodawanie użytkownika jest obsługiwane przez ApplicationController - ma on dostęp do serwisu użytkowników PersonService. Po wybraniu przycisku dodania/edycji użytkownika wyświetla się formularz
obsługiwany przez EditUserController, w którym trzeba wprowadzić poprawne dane użytkownika. W miarę rozwoju projektu ulegnie to zapewne zmianie, ponieważ lista użytkowników nie będzie raczej wyświetlana w widoku głównym. 

